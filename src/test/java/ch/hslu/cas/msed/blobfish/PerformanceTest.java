package ch.hslu.cas.msed.blobfish;

import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.base.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.base.PathEvaluation;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.CompositeEvaluationStrategy;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEvaluation;
import ch.hslu.cas.msed.blobfish.eval.MaterialEvaluation;
import ch.hslu.cas.msed.blobfish.eval.PieceSquareEvaluation;
import ch.hslu.cas.msed.blobfish.minimax.MiniMaxAlphaBetaSequential;
import ch.hslu.cas.msed.blobfish.minimax.MiniMaxSequential;
import ch.hslu.cas.msed.blobfish.minimax.base.MiniMaxAlgo;
import ch.hslu.cas.msed.blobfish.minimax.cached.MiniMaxAlphaBetaSequentialWithCache;
import ch.hslu.cas.msed.blobfish.util.FileUtil;
import ch.hslu.cas.msed.blobfish.util.MeasurementUtil;
import ch.hslu.cas.msed.blobfish.util.PlantUmlUtil;
import ch.hslu.cas.msed.blobfish.util.TexTableUtil;
import com.google.common.collect.ImmutableMap;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Tag(value = "performance")
public class PerformanceTest {

    private static File rootFolderForMeasurements = null;
    private static File globalMeasurementsFile = null;

    private enum DURATION_RESULT_TYPE {
        MEDIAN, DEVIATION
    }

    @FunctionalInterface
    private interface MiniMaxAlgoConstructor {
        MiniMaxAlgo create(int depth, EvaluationStrategy strategy, PlayerColor playerToMove);
    }

    private record PossibleStrategy(CompositeEvaluationStrategy strategy, String description) {
    }

    private record PositionToTest(String fen, PlayerColor playerToMove, String description) {
    }

    private record MeasurementOfDepth(MeasurementUtil.MeasurementWithDeviationResult<PathEvaluation> measurementResult,
                                      int depth) {
    }

    private record AlgorithmStrategy(String algorithm, PossibleStrategy strategy) {
    }

    private record StrategyDepth(PossibleStrategy strategy, int depth) {
    }

    private static final List<PossibleStrategy> possibleStrategies = List.of(
            new PossibleStrategy(CompositeEvaluationStrategy.builder().add(new MateAwareEvaluation()).add(new MaterialEvaluation()).build(), "Mate aware material evaluation"),
            new PossibleStrategy(CompositeEvaluationStrategy.builder().add(new MateAwareEvaluation()).add(new PieceSquareEvaluation()).build(), "Mate aware piece square evaluation"),
            new PossibleStrategy(CompositeEvaluationStrategy.builder().add(new MateAwareEvaluation()).add(new MaterialEvaluation()).add(new PieceSquareEvaluation()).build(), "Mate aware piece square material evaluation")
    );

    private record ExecutionConfigKey(
            Class<? extends MiniMaxAlgo> algorithm,
            List<Class<? extends EvaluationStrategy>> strategies
    ) {
    }

    private record ExecutionConfig(int depth) {
    }

    private static final int DEFAULT_CALCULATION_DEPTH = 5;
    private static final Map<ExecutionConfigKey, ExecutionConfig> executionConfig = ImmutableMap.of(
            new ExecutionConfigKey(MiniMaxAlphaBetaSequential.class, Collections.emptyList()), new ExecutionConfig(7),
            new ExecutionConfigKey(MiniMaxAlphaBetaSequentialWithCache.class, Collections.emptyList()), new ExecutionConfig(7)
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
                Arguments.of(new PositionToTest("Q7/p1pk3p/2p2qp1/3p1b2/8/1PN1P3/P1PP2PP/R4KNR b - - 4 15", PlayerColor.BLACK, "Mid game - discovery - mate in 2 - short")),
                Arguments.of(new PositionToTest("rnbqkbnr/ppp2ppp/8/3pp3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 4", PlayerColor.WHITE, "Simple tactical position")),
                Arguments.of(new PositionToTest("r1bqkbnr/pppp1ppp/2n5/4p3/3P4/5N2/PPP2PPP/RNBQKB1R w KQkq - 2 4", PlayerColor.WHITE, "Fork opportunity")),
                Arguments.of(new PositionToTest("r2q1rk1/ppp2ppp/2npbn2/4p3/2B1P3/2NP1N2/PPP2PPP/R1BQ1RK1 w - - 0 8", PlayerColor.WHITE, "Midgame tactical cluster")),
                Arguments.of(new PositionToTest("r1bq1rk1/ppp1ppbp/2np1np1/4N3/2B1P3/2P2Q1P/PP3PP1/RNB1K2R w KQ - 2 10", PlayerColor.WHITE, "Complex tactical middle game"))
        );
    }

    @BeforeAll
    static void setup() {
        // allow for bigger pumls
        System.setProperty("PLANTUML_LIMIT_SIZE", "12288");
        if (rootFolderForMeasurements == null) {
            rootFolderForMeasurements = createMeasurementFolder();
        }
        if (globalMeasurementsFile == null) {
            globalMeasurementsFile = createGlobalMeasurementFile(rootFolderForMeasurements);
        }
    }

    @AfterAll
    static void afterAll() {
        var measurements = readGlobalMeasurementEntries();
        TexTableUtil.generateMeasurementPerformanceGainTable(MiniMaxSequential.class.getSimpleName(), measurements, rootFolderForMeasurements);
        createMeasurementsAtCommonMaxDepthPuml(measurements, rootFolderForMeasurements);
    }

    @ParameterizedTest
    @MethodSource(value = "positionProvider")
    void measure_startPos(PositionToTest positionToTest) {
        var numberOfMeasurements = 10;
        var chessboard = new ChessBoard(positionToTest.fen());
        var folderToSaveMeasurements = getFolderOfPosition(positionToTest, rootFolderForMeasurements);
        folderToSaveMeasurements.mkdirs();

        Map<AlgorithmStrategy, List<MeasurementOfDepth>> results = new HashMap<>();

        getAllMiniMaxConstructors().forEach(miniMaxAlgoConstructor ->
                possibleStrategies.forEach(strategy -> {
                            // instantiate algorithm, so we can extract the class for the config
                            var algoClass = miniMaxAlgoConstructor.create(0, strategy.strategy(), positionToTest.playerToMove()).getClass();
                            var config = executionConfig.entrySet().stream().filter(entry ->
                                    entry.getKey().algorithm() == algoClass &&
                                            (entry.getKey().strategies().isEmpty() || entry.getKey().strategies().equals(strategy.strategy().getStrategies()))
                            ).map(Map.Entry::getValue).findFirst().orElse(null);
                            var maxDepth = config != null ? config.depth : DEFAULT_CALCULATION_DEPTH;

                            IntStream.range(1, maxDepth + 1).forEach(depth -> {
                                var miniMaxAlgoToTest = miniMaxAlgoConstructor.create(depth, strategy.strategy(), positionToTest.playerToMove());
                                var key = new AlgorithmStrategy(miniMaxAlgoToTest.getClass().getSimpleName(), strategy);

                                // do multiple measurements and calculate the duration
                                var measurements = IntStream.range(0, numberOfMeasurements).mapToObj(_ ->
                                        MeasurementUtil.measureOperation(() -> miniMaxAlgoToTest.getBestPath(chessboard))
                                ).toList();

                                assertSameMovesAcrossMeasurements(measurements);
                                var durationList = measurements.stream().map(MeasurementUtil.MeasurementResult::duration).toList();

                                saveRawMeasurements(positionToTest, key, depth, durationList, folderToSaveMeasurements);

                                var medianDuration = MeasurementUtil.calcMedianDuration(durationList);
                                var medianDeviation = MeasurementUtil.calcMedianOfAbsoluteDeviationsDuration(durationList);
                                var measurementResult = new MeasurementUtil.MeasurementWithDeviationResult<>(medianDuration, medianDeviation, measurements.getFirst().result());

                                results.putIfAbsent(key, new ArrayList<>());
                                results.get(key).add(new MeasurementOfDepth(measurementResult, depth));
                            });
                        }
                )
        );

        assertSameMoveEvaluationsAcrossAlgorithms(results);

        var fileName = getFileNameOfPosition(positionToTest);

        var medianCsv = createCsvFile(positionToTest, results, DURATION_RESULT_TYPE.MEDIAN);
        var medianPuml = createPlantUml(positionToTest, results, DURATION_RESULT_TYPE.MEDIAN);
        var medianPng = PlantUmlUtil.convertPlantUmlToPng(medianPuml);

        var medianDeviationCsv = createCsvFile(positionToTest, results, DURATION_RESULT_TYPE.DEVIATION);
        var medianDeviationTable = writeTexTableToFile(positionToTest, results, DURATION_RESULT_TYPE.DEVIATION);

        try {
            Files.move(medianCsv.toPath(), folderToSaveMeasurements.toPath().resolve(fileName + ".csv"), StandardCopyOption.REPLACE_EXISTING);
            Files.move(medianPuml.toPath(), folderToSaveMeasurements.toPath().resolve(fileName + ".puml"), StandardCopyOption.REPLACE_EXISTING);
            Files.move(medianPng.toPath(), folderToSaveMeasurements.toPath().resolve(fileName + ".png"), StandardCopyOption.REPLACE_EXISTING);

            Files.move(medianDeviationCsv.toPath(), folderToSaveMeasurements.toPath().resolve(fileName + "-deviation.csv"), StandardCopyOption.REPLACE_EXISTING);
            Files.move(medianDeviationTable.toPath(), folderToSaveMeasurements.toPath().resolve(fileName + "-deviation.tex"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        addGlobalMeasurementEntry(positionToTest, results);
    }

    private void saveRawMeasurements(PositionToTest positionToTest, AlgorithmStrategy algorithmStrategy, int depth, List<Duration> durationList, File folderToSave) {
        var posCon = WordUtils.capitalizeFully(positionToTest.description()).replaceAll(" ", "");
        var algo = WordUtils.capitalizeFully(algorithmStrategy.algorithm()).replaceAll(" ", "");
        var stratCon = WordUtils.capitalizeFully(algorithmStrategy.strategy().description()).replaceAll(" ", "");
        var fileName = posCon + "_" + algo + "_" + stratCon + "_depth_" + depth + "_raw.txt";
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
     * @return the created file
     *
     */
    private File createCsvFile(PositionToTest positionToTest, Map<AlgorithmStrategy, List<MeasurementOfDepth>> results, DURATION_RESULT_TYPE resultType) {
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
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(resultFile), getCSVFormat(headers.toArray(new String[0])))) {


            results.forEach((key, measurementsList) -> {
                try {
                    printer.print(getAlgorithmName(key));
                    measurementsList.stream()
                            .map(MeasurementOfDepth::measurementResult)
                            .map(r -> switch (resultType) {
                                case MEDIAN -> r.median();
                                case DEVIATION -> r.deviation();
                            })
                            .map(PerformanceTest::mapDurationToValue)
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

    private File createPlantUml(PerformanceTest.PositionToTest positionToTest, Map<AlgorithmStrategy, List<MeasurementOfDepth>> results, DURATION_RESULT_TYPE resultType) {

        var chartTitle = "FEN: " + positionToTest.fen();
        var maxAmountOfResults = results.values().stream()
                .mapToInt(List::size)
                .max().orElse(0);
        var hAxisTitle = IntStream.range(1, maxAmountOfResults + 1)
                .mapToObj(i -> "Depth " + i)
                .toList();
        var vAxisTitle = "Calculation time [ms]";
        var barResults = results.keySet().stream()
                .sorted((a1, a2) -> {
                    var nameA1 = getAlgorithmName(a1);
                    var nameA2 = getAlgorithmName(a2);
                    return String.CASE_INSENSITIVE_ORDER.reversed().compare(nameA1, nameA2);
                })
                .map(strategy -> {
                    var barDescription = getAlgorithmName(strategy);
                    var measurements = results.get(strategy).stream()
                            .mapToDouble(m -> mapMeasurementDepthToValue(m, resultType, true))
                            .boxed()
                            .toList();
                    return new PlantUmlUtil.ChartBar(barDescription, measurements);
                })
                .toList();

        return PlantUmlUtil.createBarChart(chartTitle, hAxisTitle, vAxisTitle, barResults, false);
    }

    private File writeTexTableToFile(final PerformanceTest.PositionToTest positionToTest, final Map<AlgorithmStrategy, List<MeasurementOfDepth>> results, final DURATION_RESULT_TYPE resultType) {
        var tableTitle = switch (resultType) {
            case MEDIAN -> "Median [ms]";
            case DEVIATION -> "Median of absolute deviation (MAD) [ms]";
        };

        var positionTitle = positionToTest.description();

        int maxDepth = results.values().stream()
                .flatMap(List::stream)
                .mapToInt(MeasurementOfDepth::depth)
                .max()
                .orElse(0);

        // get header depths dynamically
        var depthHeaders = IntStream.range(1, maxDepth + 1)
                .mapToObj(d -> "Depth " + d)
                .map("\\multicolumn{1}{r|}{\\textbf{%s}}"::formatted)
                .collect(Collectors.joining(" & "));

        var rowCounter = new AtomicInteger(0);
        var tableRows = results.entrySet()
                .stream()
                .map(entry -> {
                    var isEven = rowCounter.incrementAndGet() % 2 == 0;
                    var values = new ArrayList<>(entry.getValue()
                            .stream()
                            .sorted(Comparator.comparingInt(MeasurementOfDepth::depth))
                            .map(e -> mapMeasurementDepthToValue(e, resultType, false))
                            .toList());
                    // for entries which have a lower depth calculation
                    values.addAll(Collections.nCopies(maxDepth - values.size(), null));

                    return TexTableUtil.createTableRow(getAlgorithmName(entry.getKey()), values, isEven);
                })
                .collect(Collectors.joining(System.lineSeparator()));

        var tableCaption = "%s: %s".formatted(resultType.name(), positionToTest.fen());
        var tableLabel = "%s-%s".formatted(positionTitle.replaceAll(" ", "-"), resultType.name()).toLowerCase();

        var tableContent = TexTableUtil.createTexTable(maxDepth, positionTitle, tableTitle, depthHeaders, tableRows, tableCaption, tableLabel);

        var resultFile = FileUtil.createTmpFile(resultType.name() + "-table", "tex");
        try (var fileWriter = new FileWriter(resultFile)) {
            fileWriter.write(tableContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resultFile;
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


    private static void assertSameMovesAcrossMeasurements(List<MeasurementUtil.MeasurementResult<PathEvaluation>> measurements) {
        long distinctMoves = measurements.stream()
                .map(MeasurementUtil.MeasurementResult::result)
                .distinct()
                .count();

        Assertions.assertEquals(1, distinctMoves, "Moves of the repeating executions do not match.");
    }

    private static void assertSameMoveEvaluationsAcrossAlgorithms(Map<AlgorithmStrategy, List<MeasurementOfDepth>> measurements) {
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
                                                m.measurementResult().result().eval()
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
                    .stream()
                    .filter(clazz -> !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()))
                    .map(clazz -> {
                        try {
                            var constructor = clazz.getDeclaredConstructor(int.class, EvaluationStrategy.class, PlayerColor.class);
                            return (MiniMaxAlgoConstructor) (
                                    int depth,
                                    EvaluationStrategy strategy,
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
            EvaluationStrategy strategy,
            PlayerColor playerToMove
    ) {
        try {
            return (MiniMaxAlgo) constructor.newInstance(depth, strategy, playerToMove);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Double mapMeasurementDepthToValue(MeasurementOfDepth measurementOfDepth, DURATION_RESULT_TYPE resultType, boolean logarithmicValue) {
        var valueToFormat = switch (resultType) {
            case MEDIAN -> measurementOfDepth.measurementResult.median();
            case DEVIATION -> measurementOfDepth.measurementResult.deviation();
        };

        return logarithmicValue ? mapDurationToLogValue(valueToFormat) : mapDurationToValue(valueToFormat);
    }

    private static Double mapDurationToLogValue(Duration duration) {
        double millis = duration.toMillis();
        if (millis == 0) return 0.0;
        return Math.round(Math.log10(millis) * 1_000.0) / 1_000.0;
    }

    private static Double mapDurationToValue(Duration duration) {
        double micros = duration.toNanos() / 1_000.0;
        return Math.round(micros) / 1_000.0;
    }

    private static File createGlobalMeasurementFile(File rootfile) {
        var file = new File(rootfile, "measurements.csv");
        try {
            Files.write(file.toPath(), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private void addGlobalMeasurementEntry(final PositionToTest position, final Map<AlgorithmStrategy, List<MeasurementOfDepth>> measurements) {
        var shouldWriteHeaders = globalMeasurementsFile.length() <= 0;

        var headers = shouldWriteHeaders ? List.of(
                "Position FEN",
                "Position description",
                "Algorithm",
                "Strategy",
                "Depth",
                "Duration [ms]"
        ).toArray(new String[0]) : null;


        try (CSVPrinter printer = new CSVPrinter(new FileWriter(globalMeasurementsFile, true), getCSVFormat(headers))) {
            measurements.forEach((key, value) -> value
                    .forEach(measurement -> {
                        try {
                            printer.print(position.fen());
                            printer.print(position.description());
                            printer.print(key.algorithm());
                            printer.print(key.strategy().description());
                            printer.print(measurement.depth());
                            printer.print(measurement.measurementResult.median().toString());

                            printer.println();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static List<TexTableUtil.GlobalMeasurementEntry> readGlobalMeasurementEntries() {
        try (Reader reader = new FileReader(globalMeasurementsFile);
             CSVParser parser = CSVParser.builder().setReader(reader).setFormat(getCSVFormat()).get()) {

            return parser.stream().map(TexTableUtil.GlobalMeasurementEntry::new).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createMeasurementsAtCommonMaxDepthPuml(final List<TexTableUtil.GlobalMeasurementEntry> measurements, final File rootFolderForMeasurements) {
        int commonMaxDepth = measurements.stream()
                .collect(Collectors.toMap(
                        TexTableUtil.GlobalMeasurementEntry::algorithm,
                        TexTableUtil.GlobalMeasurementEntry::depth,
                        BinaryOperator.maxBy(Integer::compare))
                )
                .values().stream()
                .min(Integer::compare)
                .orElse(0);

        var algorithms = measurements.stream()
                .map(TexTableUtil.GlobalMeasurementEntry::algorithm)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        var rowValues = measurements.stream()
                .filter(e -> e.depth() == commonMaxDepth)
                .collect(Collectors.groupingBy(TexTableUtil.GlobalMeasurementEntry::positionFen))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        positionEntry -> algorithms.stream()
                                .map(algo ->
                                        MeasurementUtil.calcMedianDuration(
                                                positionEntry.getValue().stream()
                                                        .filter(e -> e.algorithm().equals(algo))
                                                        .map(TexTableUtil.GlobalMeasurementEntry::duration)
                                                        .toList()
                                        )
                                )
                                .toList()
                ));

        var barResults = rowValues.entrySet()
                .stream()
                .map(e -> {
                    var values = e.getValue()
                            .stream()
                            .map(PerformanceTest::mapDurationToLogValue)
                            .toList();

                    return new PlantUmlUtil.ChartBar(e.getKey(), values);
                })
                .toList();

        var chartTitle = "Median calculation time at depth %s".formatted(commonMaxDepth);
        var vAxisTitle = "Calculation time [ms]";

        var fileName = "performance-at-depth-%d".formatted(commonMaxDepth);
        var pumlFile = PlantUmlUtil.createBarChart(chartTitle, algorithms, vAxisTitle, barResults, false);
        var pngFile = PlantUmlUtil.convertPlantUmlToPng(pumlFile);

        try {
            Files.move(pumlFile.toPath(), rootFolderForMeasurements.toPath().resolve(fileName + ".puml"), StandardCopyOption.REPLACE_EXISTING);
            Files.move(pngFile.toPath(), rootFolderForMeasurements.toPath().resolve(fileName + ".png"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CSVFormat getCSVFormat(final String... header) {
        return CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setTrailingDelimiter(false)
                .setIgnoreSurroundingSpaces(true)
                .setNullString("")
                .setHeader(header)
                .get();
    }
}
