package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;
import ch.hslu.cas.msed.blobfish.util.MeasurementUtil;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class PerformanceTest {

    @FunctionalInterface
    private interface MiniMaxAlgoConstructor {
        MiniMaxAlgo create(int depth, EvalStrategy strategy, PlayerColor playerToMove);
    }

    private record PossibleStrategy(EvalStrategy strategy, String description) {
    }

    private record PositionToTest(String fen, PlayerColor playerToMove, String description) {
    }

    private record MeasurementOfDepth(MeasurementUtil.MeasurementResult<String> measurementResult, int depth) {
    }

    private record AlgorithmStrategy(String algorithm, PossibleStrategy strategy) {
    }

    private record StrategyDepth(PossibleStrategy strategy, int depth) {
    }

    private static final List<PossibleStrategy> possibleStrategies = List.of(
            new PossibleStrategy(new MaterialEval(), "Simple material evaluation."),
            new PossibleStrategy(new MateAwareEval(new MaterialEval()), "Mate aware material evaluation.")
    );

    private static Stream<Arguments> positionProvider() {
        return Stream.of(
                Arguments.of(new PositionToTest("1r4r1/5p1k/p2p1q1p/2b1nPQ1/p7/6RP/B1R2PPK/2B5 b - - 0 1", PlayerColor.BLACK, "Complex position with many options")),
                Arguments.of(new PositionToTest("5rk1/5q1p/p2PR1p1/4p1b1/1pP5/1P1Q4/P5PP/1K2R3 b - - 0 31", PlayerColor.BLACK, "Mid game - deflection - short")),
                Arguments.of(new PositionToTest("r3k2r/pNp1ppbp/2nq2pn/7P/2B3b1/3P1N2/PPP2PP1/R1BQK2R b KQkq - 0 11", PlayerColor.BLACK, "Mid game - fork - short")),
                Arguments.of(new PositionToTest("rn1q1r1k/pbpp1p1p/1p2p3/4P3/3P2Qp/P2B4/1PP2PPP/R4RK1 w - - 0 17", PlayerColor.WHITE, "Mid game - en passant - long")),
                Arguments.of(new PositionToTest("r5k1/7p/1p1Qp1p1/p1np1r2/1q3P1P/1P6/2P3P1/RB3RK1 w - - 3 26", PlayerColor.WHITE, "Mid game - discovery - short")),
                Arguments.of(new PositionToTest("8/5ppk/4p1p1/3pq3/3Q4/1B2r2P/P5P1/3R3K b - - 8 42", PlayerColor.BLACK, "End game - deflection - short")),
                Arguments.of(new PositionToTest("5r1k/1pqnbr1P/p2p1pQp/2p5/3PP2P/1PN5/1PP3R1/R5K1 w - - 0 24", PlayerColor.WHITE, "Mid game - promotion - mate in 2 - short")),
                Arguments.of(new PositionToTest("Q7/p1pk3p/2p2qp1/3p1b2/8/1PN1P3/P1PP2PP/R4KNR b - - 4 15", PlayerColor.BLACK, "Mid game - discovery - mate in 2 - short"))
        );
    }


    @ParameterizedTest
    @MethodSource(value = "positionProvider")
    void measure_startPos(PositionToTest positionToTest) {
        var maxDepth = 4;
        var numberOfMeasurements = 10;
        var chessboard = new ChessBoard(positionToTest.fen());

        Map<AlgorithmStrategy, List<MeasurementOfDepth>> results = new HashMap<>();

        getAllMiniMaxConstructors().forEach(miniMaxAlgoConstructor ->
                possibleStrategies.forEach(strategy ->
                        IntStream.range(1, maxDepth + 1).forEach(depth -> {
                            var miniMaxAlgoToTest = miniMaxAlgoConstructor.create(depth, strategy.strategy(), positionToTest.playerToMove);
                            var key = new AlgorithmStrategy(miniMaxAlgoToTest.getClass().getSimpleName(), strategy);

                            // do multiple measurements and calculate the average
                            var measurements = IntStream.range(0, numberOfMeasurements).mapToObj(_ ->
                                    MeasurementUtil.measureOperation(() -> miniMaxAlgoToTest.getNextBestMove(chessboard))
                            ).toList();

                            assertSameMovesAcrossMeasurements(measurements);
                            var averageDuration = MeasurementUtil.calcAverageDuration(measurements);
                            var measurementResult = new MeasurementUtil.MeasurementResult<>(averageDuration, measurements.getFirst().result());

                            results.putIfAbsent(key, new ArrayList<>());
                            results.get(key).add(new MeasurementOfDepth(measurementResult, depth));
                        })
                )
        );

        assertSameMovesAcrossAlgorithms(results);

        printResultFile(positionToTest, results);
    }


    /**
     * ComplexPositionWithManyOptions.csv
     * <p>
     * 1r4r1/5p1k/p2p1qQp/2b1nPp1/p7/6RP/B1R2PPK/2B5                 Depth 1, Depth 2, Depth 3, Depth 4, Depth 5
     * MiniMaxSequential (Simple material evaluation)
     * MiniMaxSequential (Mate aware material evaluation)
     * MiniMaxParallel(Simple material evaluation) MiniMaxParallel
     * (Mate aware material evaluation)
     *
     */
    private void printResultFile(PositionToTest positionToTest, Map<AlgorithmStrategy, List<MeasurementOfDepth>> results) {
        // get header depths dynamically
        List<String> depthHeaders = results.values()
                .stream()
                .max(Comparator.comparingInt(List::size))
                .orElse(List.of())
                .stream()
                .map(m -> m.depth)
                .map(d -> "Depth " + d)
                .toList();

        var headers = new ArrayList<>(List.of(positionToTest.fen()));
        headers.addAll(depthHeaders);

        var fileName = getFileNameOfPosition(positionToTest);
        File file = new File(fileName + ".csv");

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setTrailingDelimiter(false)
                .setIgnoreSurroundingSpaces(true)
                .setNullString("")
                .setHeader(headers.toArray(new String[0]))
                .get())) {


            results.forEach((key, measurementsList) -> {
                try {
                    printer.print(getAlgorithmName(key));
                    measurementsList.stream()
                            .map(MeasurementOfDepth::measurementResult)
                            .map(MeasurementUtil.MeasurementResult::duration)
                            .map(d -> d.toMillis() + " ms")
                            .forEach(m -> {
                                try {
                                    printer.print(m);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                    printer.println();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileNameOfPosition(PositionToTest position) {
        return WordUtils.capitalizeFully(position.description()).replaceAll(" ", "");
    }

    private static void assertSameMovesAcrossMeasurements(List<MeasurementUtil.MeasurementResult<String>> measurements) {
        long distinctMoves = measurements.stream()
                .map(MeasurementUtil.MeasurementResult::result)
                .distinct()
                .count();

        Assertions.assertEquals(1, distinctMoves, "Moves of the repeating executions do not match.");
    }

    private static void assertSameMovesAcrossAlgorithms(Map<AlgorithmStrategy, List<MeasurementOfDepth>> measurements) {
        var groupedResults = measurements.entrySet().stream()
                .flatMap(entry ->
                        entry.getValue().stream()
                                .map(m -> Map.entry(
                                        new StrategyDepth(
                                                entry.getKey().strategy(),
                                                m.depth()
                                        ),
                                        Map.entry(
                                                entry.getKey().algorithm(),
                                                m.measurementResult().result()
                                        )
                                ))
                ).collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.toMap(
                                e -> e.getValue().getKey(),   // algorithm
                                e -> e.getValue().getValue()  // result
                        )
                ));

        groupedResults.forEach((key, algorithmResults) -> {
            var distinctResults = new HashSet<>(algorithmResults.values());

            if (distinctResults.size() > 1) {
                var mismatchDetail = algorithmResults.entrySet().stream()
                        .map(e -> getAlgorithmName(new AlgorithmStrategy(e.getKey(), key.strategy())) + "=" + e.getValue())
                        .collect(Collectors.joining(System.lineSeparator()));

                Assertions.fail(String.format("Algorithms return different moves at depth %s: %s%s", key.depth(), System.lineSeparator(), mismatchDetail));
            }
        });
    }

    private static String getAlgorithmName(AlgorithmStrategy algorithmStrategy) {
        return String.format("%s (%s)", algorithmStrategy.algorithm(), algorithmStrategy.strategy().description());
    }

    private static List<MiniMaxAlgoConstructor> getAllMiniMaxConstructors() {
        Class<?> base = MiniMaxAlgo.class;

        try (ScanResult scan = new ClassGraph()
                .enableClassInfo()
                .acceptPackages("ch.hslu.cas.msed.blobfish")
                .scan()) {

            return scan.getSubclasses(base.getName()).loadClasses()
                    .stream().map(clazz -> {
                        try {
                            var constructor = clazz.getDeclaredConstructor(int.class, EvalStrategy.class, PlayerColor.class);
                            return (MiniMaxAlgoConstructor) (
                                    int depth,
                                    EvalStrategy strategy,
                                    PlayerColor playerToMove
                            ) -> instantiateAlgorithm(constructor, depth, strategy, playerToMove);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();
        }
    }

    private static MiniMaxAlgo instantiateAlgorithm(
            Constructor<?> constructor,
            int depth,
            EvalStrategy strategy,
            PlayerColor playerToMove
    ) {
        try {
            return (MiniMaxAlgo) constructor.newInstance(depth, strategy, playerToMove);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
