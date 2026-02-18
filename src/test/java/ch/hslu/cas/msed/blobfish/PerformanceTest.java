package ch.hslu.cas.msed.blobfish;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxAlgo;
import ch.hslu.cas.msed.blobfish.util.FileUtil;
import ch.hslu.cas.msed.blobfish.util.MeasurementUtil;
import ch.hslu.cas.msed.blobfish.util.PlantUmlUtil;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Tag(value = "performance")
public class PerformanceTest {

    private static File rootFolderForMeasurements = null;

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
            new PossibleStrategy(new MaterialEval(), "Simple material evaluation"),
            new PossibleStrategy(new MateAwareEval(new MaterialEval()), "Mate aware material evaluation")
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

    @BeforeAll
    static void setup() {
        if (rootFolderForMeasurements == null) {
            rootFolderForMeasurements = createMeasurementFolder();
        }
    }

    @ParameterizedTest
    @MethodSource(value = "positionProvider")
    void measure_startPos(PositionToTest positionToTest) {
        var maxDepth = 4;
        var numberOfMeasurements = 10;
        var chessboard = new ChessBoard(positionToTest.fen());
        var folderToSaveMeasurements = getFolderOfPosition(positionToTest, rootFolderForMeasurements);
        folderToSaveMeasurements.mkdirs();

        Map<AlgorithmStrategy, List<MeasurementOfDepth>> results = new HashMap<>();

        getAllMiniMaxConstructors().forEach(miniMaxAlgoConstructor ->
                possibleStrategies.forEach(strategy ->
                        IntStream.range(1, maxDepth + 1).forEach(depth -> {


                            var miniMaxAlgoToTest = miniMaxAlgoConstructor.create(depth, strategy.strategy(), positionToTest.playerToMove);
                            var key = new AlgorithmStrategy(miniMaxAlgoToTest.getClass().getSimpleName(), strategy);

                            // do multiple measurements and calculate the median
                            var measurements = IntStream.range(0, numberOfMeasurements).mapToObj(_ ->
                                    MeasurementUtil.measureOperation(() -> miniMaxAlgoToTest.getNextBestMove(chessboard))
                            ).toList();

                            assertSameMovesAcrossMeasurements(measurements);
                            var durationList = measurements.stream().map(MeasurementUtil.MeasurementResult::duration).toList();

                            saveRawMeasurements(positionToTest, key, depth, durationList, folderToSaveMeasurements);

                            var medianDuration = MeasurementUtil.calcMedianDuration(durationList);
                            var measurementResult = new MeasurementUtil.MeasurementResult<>(medianDuration, measurements.getFirst().result());

                            results.putIfAbsent(key, new ArrayList<>());
                            results.get(key).add(new MeasurementOfDepth(measurementResult, depth));
                        })
                )
        );

        assertSameMovesAcrossAlgorithms(results);

        var fileName = getFileNameOfPosition(positionToTest);
        var resultFile = createResultFile(positionToTest, results);
        var plantuml = createPlantUml(positionToTest, results);
        var svg = PlantUmlUtil.convertPlantUmlToSvg(plantuml);
        try {
            Files.move(resultFile.toPath(), folderToSaveMeasurements.toPath().resolve(fileName + ".csv"), StandardCopyOption.REPLACE_EXISTING);
            Files.move(plantuml.toPath(), folderToSaveMeasurements.toPath().resolve(fileName + ".puml"), StandardCopyOption.REPLACE_EXISTING);
            Files.move(svg.toPath(), folderToSaveMeasurements.toPath().resolve(fileName + ".svg"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveRawMeasurements(PositionToTest positionToTest, AlgorithmStrategy algorithmStrategy, int depth, List<Duration> durationList, File folderToSave) {
        var posCon = WordUtils.capitalizeFully(positionToTest.description()).replaceAll(" ", "");
        var algo = WordUtils.capitalizeFully(algorithmStrategy.algorithm()).replaceAll(" ", "");
        var stratCon = WordUtils.capitalizeFully(algorithmStrategy.strategy().description()).replaceAll(" ", "");
        var fileName = posCon + "_" + algo + "_" +stratCon + "_depth_" + depth + "_raw.txt";
        try (FileWriter writer = new FileWriter(folderToSave + File.separator + fileName)) {
            durationList.forEach(duration -> {
                try {
                    writer.write(mapDurationToValue(duration) + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
     * @return
     *
     */
    private File createResultFile(PositionToTest positionToTest, Map<AlgorithmStrategy, List<MeasurementOfDepth>> results) {
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

        var resultFile = FileUtil.createTmpFile("resultFile", "csv");
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(resultFile), CSVFormat.DEFAULT.builder()
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
                            .map(this::mapDurationToValue)
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

        return resultFile;
    }

    private File createPlantUml(PerformanceTest.PositionToTest positionToTest, Map<PerformanceTest.AlgorithmStrategy, List<PerformanceTest.MeasurementOfDepth>> results) {

        var chartTitle = "FEN: " + positionToTest.fen();
        var maxAmountOfResults = results.values().stream()
                .mapToInt(List::size)
                .max().orElse(0);
        var hAxisTitle = IntStream.range(1, maxAmountOfResults + 1)
                .mapToObj(i -> "Depth " + i)
                .toList();
        var vAxisTitle = "Calculation time [s]";
        var barResults = results.keySet().stream()
                .sorted((a1, a2) -> {
                    var nameA1 = getAlgorithmName(a1);
                    var nameA2 = getAlgorithmName(a2);
                    return String.CASE_INSENSITIVE_ORDER.reversed().compare(nameA1, nameA2);
                })
                .map(strategy -> {
                    var barDescription = getAlgorithmName(strategy);
                    var measurements = results.get(strategy).stream()
                            .mapToDouble(this::mapMeasurementDepthToValue)
                            .boxed()
                            .toList();
                    return new PlantUmlUtil.ChartBar(barDescription, measurements);
                })
                .toList();

        return PlantUmlUtil.createBarChart(chartTitle, hAxisTitle, vAxisTitle, barResults);
    }

    private String getFileNameOfPosition(PositionToTest position) {
        return WordUtils.capitalizeFully(position.description()).replaceAll(" ", "");
    }

    private File getFolderOfPosition(PositionToTest position, File rootFolder) {
        var filename = getFileNameOfPosition(position)
                .replaceAll("-", "");
        filename = Character.toLowerCase(filename.charAt(0)) + filename.substring(1);
        return rootFolder.toPath().resolve(filename).toFile();
    }

    private static File createMeasurementFolder() {
        var measurementFolder = "measurements";
        var dateTimeFolder = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm").format(LocalDateTime.now());
        var rootFolder = measurementFolder + File.separator + dateTimeFolder;
        File folder = new File(rootFolder);
        folder.mkdirs();
        return folder;
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

    private Double mapMeasurementDepthToValue(MeasurementOfDepth measurementOfDepth) {
        return mapDurationToValue(measurementOfDepth.measurementResult.duration());
    }

    private Double mapDurationToValue(Duration duration) {
        double seconds = duration.toNanos() / 1_000_000_000.0;
        return Math.round(seconds * 1000.0) / 1000.0;
    }
}
