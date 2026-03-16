package ch.hslu.cas.msed.blobfish;

import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.CompositeEvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.player.bot.PathEvaluation;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxAlphaBetaSequential;
import ch.hslu.cas.msed.blobfish.stockfish.StockFishService;
import ch.hslu.cas.msed.blobfish.stockfish.junit.InjectStockfish;
import ch.hslu.cas.msed.blobfish.stockfish.junit.StockfishExtension;
import ch.hslu.cas.msed.blobfish.util.EvaluationUtil;
import ch.hslu.cas.msed.blobfish.util.EvaluationUtil.EvalConfig;
import ch.hslu.cas.msed.blobfish.util.PlantUmlUtil;
import ch.hslu.cas.msed.blobfish.util.QualityTestLatexUtil;
import lombok.Getter;
import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ExtendWith(StockfishExtension.class)
@Tag(value = "quality")
public class QualityTest {

    @InjectStockfish
    StockFishService stockFishService;

    private static File rootFolderForQualityFiles = null;

    @BeforeAll
    static void setup() {
        if (rootFolderForQualityFiles == null) {
            rootFolderForQualityFiles = createQualityFolder();
        }
    }

    @Getter
    public enum QualityTestCategory {
        TACTICS("Tactics"),
        DYNAMIC_PLAY("Dynamic play / initiative / sacrifices"),
        POSITIONAL_PLAY("Positional play / prophylaxis"),
        ENDGAMES_TECHNIQUE("Endgames / technique / zugzwang / fortress");
        private final String description;

        QualityTestCategory(String description) {
            this.description = description;
        }
    }

    private final Map<QualityTestCategory, List<String>> positionsToTest = Map.ofEntries(
            Map.entry(QualityTestCategory.TACTICS, List.of(
                    // Tests tactical sequencing.
                    "r2q1rk1/ppp2ppp/2npbn2/3Np3/2B1P3/2N5/PPP2PPP/R1BQ1RK1 w - - 0 1",
                    // Kingside attack motifs.
                    "r1bq1rk1/ppp2ppp/2np1n2/4p3/2B1P3/2NP1N2/PPP2PPP/R1BQ1RK1 w - - 0 1",
                    // Tests underpromotion ideas.
                    "4k3/P7/8/8/8/8/7p/4K3 w - - 0 1"
            )),
            Map.entry(QualityTestCategory.DYNAMIC_PLAY, List.of(
                    // Dynamic compensation test.
                    "r3r1k1/pp1n1ppp/2pbpn2/q1p5/3P4/2N1PN2/PPQ1BPPP/2RR2K1 w - - 0 1",
                    // Attack vs material choice.
                    "r1b2rk1/pp1n1ppp/2p1pn2/q2p4/3P4/2NBPN2/PPQ2PPP/R1B2RK1 w - - 0 1",
                    // Finds best defense.
                    "r4rk1/ppp2ppp/2np1q2/4p3/2B1P3/2NP1Q2/PPP2PPP/R4RK1 b - - 0 1",
                    // Quiet move over tactics.
                    "r1bq1rk1/ppp2ppp/2np1n2/4p3/2B1P3/2NP1N2/PPP2PPP/R1BQ1RK1 b - - 0 1",
                    // Best long-term plan.
                    "r2q1rk1/pp2bppp/2npbn2/2p1p3/2P1P3/2NP1N2/PP2BPPP/R1BQ1RK1 w - - 0 1"
            )),
            Map.entry(QualityTestCategory.POSITIONAL_PLAY, List.of(
                    // Positional evaluation test.
                    "2r3k1/5pp1/p3p2p/1p1pP3/3P1P2/1P3N1P/P5P1/2R3K1 w - - 0 1",
                    // Prophylaxis and restraint.
                    "2r2rk1/1bq1bppp/p2ppn2/1pn5/4P3/1NN1BP2/PPQ2PBP/2RR2K1 w - - 0 1",
                    // Best long-term plan.
                    "r2q1rk1/pp2bppp/2npbn2/2p1p3/2P1P3/2NP1N2/PP2BPPP/R1BQ1RK1 w - - 0 1"
            )),
            Map.entry(QualityTestCategory.ENDGAMES_TECHNIQUE, List.of(
                    // Direct mate attack.
                    "6k1/5ppp/5r2/8/8/5Q2/5PPP/6K1 w - - 0 1",
                    // Basic opposition endgame.
                    "8/8/8/3k4/8/4K3/4P3/8 w - - 0 1",
                    // Distant passed pawn race.
                    "8/2k5/8/2P5/8/5K2/6p1/8 w - - 0 1",
                    // Rook activity and technique.
                    "8/5pk1/6p1/8/3R4/6P1/5P1P/6K1 w - - 0 1",
                    // Fortress recognition.
                    "8/8/2p5/2Pp4/3P4/3K4/8/3k4 w - - 0 1",
                    // Zugzwang and opposition.
                    "8/8/8/2k5/2p5/2P5/3K4/8 w - - 0 1",
                    // Queen endgame precision.
                    "8/8/2k5/8/8/2K5/5Q2/6q1 w - - 0 1",
                    // Bishop endgame evaluation.
                    "8/5k2/6p1/5p2/3B1P2/6P1/5K2/8 w - - 0 1"
            ))
    );

    private final int[] pointDistribution = {8, 6, 4, 2, 1};

    private final int DEPTH_TO_CALC = 8;

    public record EvalQualityResult(EvalConfig strategy, PathEvaluation pathEvaluation, int pointWon) {
    }

    public record QualityTestResult(QualityTestCategory qualityTestCategory, String fen, List<String> stockfishMove,
                                    List<EvalQualityResult> evalQualityResult) {
    }

    @Test
    void compareEvals() {
        // Arrange
        var evalStrategies = EvaluationUtil.getAllEvalStrategiesCombinations().stream()
                .filter(e -> CompositeEvalStrategy.class.equals(e.strategy().getClass()))
                .filter(e -> ((CompositeEvalStrategy) e.strategy()).getStrategies().contains(MateAwareEval.class))
                .toList();
        List<QualityTestResult> qualityTestResults = new ArrayList<>();
        stockFishService.setMultiPV(pointDistribution.length);

        // Act - calculate moves and points
        positionsToTest.forEach((QualityTestCategory qualityTestCategory, List<String> positions) -> {
            positions.forEach(position -> {

                var chessboard = new ChessBoard(position);
                var stockFishResult = getStockFishResult(chessboard, DEPTH_TO_CALC);

                List<EvalQualityResult> evalQualityResults = evalStrategies.parallelStream()
                .map(evalStrategy -> {
                    var strategyBestPath = getBestPathWithEvalStrategy(chessboard, evalStrategy, DEPTH_TO_CALC);
                    var strategyPoints = this.calcPoints(stockFishResult, strategyBestPath.move().getFirst());
                    return new EvalQualityResult(evalStrategy, strategyBestPath, strategyPoints);
                })
                .toList();

                var testResult = new QualityTestResult(qualityTestCategory, position, stockFishResult, evalQualityResults);
                qualityTestResults.add(testResult);
            });
        });

        // Act - generate data tables and diagrams
        for (QualityTestCategory qualityTestCategory : positionsToTest.keySet()) {
            var fileName = getFileNameOutOfCategory(qualityTestCategory);
            var maxPoints = positionsToTest.values().stream().mapToInt(List::size).sum() * pointDistribution[0];

            var tableFile = QualityTestLatexUtil.generateTableQualityMoves(qualityTestResults, qualityTestCategory);
            var sumFile = QualityTestLatexUtil.generateOverallSum(qualityTestResults, maxPoints);
            var sumDiagram = createPlantUml(qualityTestResults, qualityTestCategory);
            var svg = PlantUmlUtil.convertPlantUmlToSvg(sumDiagram);
            var png = PlantUmlUtil.convertPlantUmlToPng(sumDiagram);
            try {
                Files.move(tableFile.toPath(), rootFolderForQualityFiles.toPath().resolve(fileName + ".tex"), StandardCopyOption.REPLACE_EXISTING);
                Files.move(sumFile.toPath(), rootFolderForQualityFiles.toPath().resolve("Overall.tex"), StandardCopyOption.REPLACE_EXISTING);
                Files.move(sumDiagram.toPath(), rootFolderForQualityFiles.toPath().resolve(fileName + ".puml"), StandardCopyOption.REPLACE_EXISTING);
                Files.move(svg.toPath(), rootFolderForQualityFiles.toPath().resolve(fileName + ".svg"), StandardCopyOption.REPLACE_EXISTING);
                Files.move(png.toPath(), rootFolderForQualityFiles.toPath().resolve(fileName + ".png"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private PathEvaluation getBestPathWithEvalStrategy(ChessBoard chessBoard, EvalConfig evalConfig, int depthToCalc) {
        var playerColor = chessBoard.getSideToMove();
        var bot = new MiniMaxAlphaBetaSequential(depthToCalc, evalConfig.strategy(), playerColor);
        return bot.getBestPath(chessBoard);
    }

    private List<String> getStockFishResult(ChessBoard chessBoard, int depthToCalc) {
        stockFishService.newGame();
        stockFishService.setPosition(chessBoard.getFen());
        return stockFishService.go(depthToCalc);
    }

    private int calcPoints(List<String> stockfishMoves, String evalMove) {
        if (!stockfishMoves.contains(evalMove)) {
            return 0;
        }

        var idx = stockfishMoves.indexOf(evalMove);
        return pointDistribution[idx];
    }

    private String getFileNameOutOfCategory(QualityTestCategory category) {
        var desc = category.getDescription().replaceAll("/", "");
        return WordUtils.capitalizeFully(desc).replaceAll(" ", "");
    }

    private File createPlantUml(List<QualityTestResult> results, QualityTest.QualityTestCategory tacticTableToGenerate) {
        var description = tacticTableToGenerate.getDescription();
        List<String> horizontalAxisTitles = List.of("Evaluation functions");
        String verticalAxisTitle = "Points won";

        Map<EvalConfig,List<EvalQualityResult>> resultGroupByStrategy = results.stream()
                .filter(r -> tacticTableToGenerate.equals(r.qualityTestCategory()))
                .flatMap(r -> r.evalQualityResult().stream())
                .collect(Collectors.groupingBy(EvalQualityResult::strategy));

        List<PlantUmlUtil.ChartBar> bars = new ArrayList<>();

        resultGroupByStrategy.keySet().stream()
                .sorted(Comparator.comparing(EvalConfig::description))
                .forEach(config -> {
                    var pointsWon = resultGroupByStrategy.get(config).stream()
                            .mapToDouble(EvalQualityResult::pointWon)
                            .sum();
                    bars.add(new PlantUmlUtil.ChartBar(config.description(), List.of(pointsWon)));
                });

        return PlantUmlUtil.createBarChart(description, horizontalAxisTitles, verticalAxisTitle, bars);
    }

    private static File createQualityFolder() {
        var measurementFolder = "quality";
        var dateTimeFolder = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm").format(LocalDateTime.now());
        var rootFolder = measurementFolder + File.separator + dateTimeFolder;
        File folder = new File(rootFolder);
        folder.mkdirs();
        return folder;
    }
}
