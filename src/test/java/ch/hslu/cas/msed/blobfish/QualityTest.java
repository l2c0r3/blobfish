package ch.hslu.cas.msed.blobfish;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxAlphaBetaSequential;
import ch.hslu.cas.msed.blobfish.stockfish.StockFishService;
import ch.hslu.cas.msed.blobfish.stockfish.junit.InjectStockfish;
import ch.hslu.cas.msed.blobfish.stockfish.junit.StockfishExtension;
import ch.hslu.cas.msed.blobfish.util.EvaluationUtil;
import ch.hslu.cas.msed.blobfish.util.EvaluationUtil.EvalConfig;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(StockfishExtension.class)
// TODO: introduce Tag
class QualityTest {

    @InjectStockfish
    StockFishService stockFishService;

    @Getter
    private enum TestCategory {

        TACTICS("TACTICS"),
        DYNAMIC_PLAY("Dynamic play / initiative / sacrifices"),
        POSITIONAL_PLAY("Positional play / prophylaxis"),
        ENDGAMES_TECHNIQUE("Endgames / technique / zugzwang / fortress");


        private final String description;

        TestCategory(String description) {
            this.description = description;
        }

    }

    private final Map<TestCategory, List<String>> positionsToTest = Map.ofEntries(
            Map.entry(TestCategory.TACTICS, List.of(
                    // Direct mate attack.
                    "6k1/5ppp/5r2/8/8/5Q2/5PPP/6K1 w - - 0 1"
                    // Tests tactical sequencing.
//                    "r2q1rk1/ppp2ppp/2npbn2/3Np3/2B1P3/2N5/PPP2PPP/R1BQ1RK1 w - - 0 1",
//                    // Kingside attack motifs.
//                    "r1bq1rk1/ppp2ppp/2np1n2/4p3/2B1P3/2NP1N2/PPP2PPP/R1BQ1RK1 w - - 0 1",
//                    // Converts a won tactic.
//                    "6k1/5ppp/8/8/8/5q2/5PPP/6K1 b - - 0 1",
//                    // Tests underpromotion ideas.
//                    "4k3/P7/8/8/8/8/7p/4K3 w - - 0 1",
//                    // Precise pawn race calculation.
//                    "8/3k4/8/2P5/8/8/4K2p/8 w - - 0 1"
            ))

//            Map.entry(TestCategory.DYNAMIC_PLAY, List.of(
//                    // Dynamic compensation test.
//                    "r3r1k1/pp1n1ppp/2pbpn2/q1p5/3P4/2N1PN2/PPQ1BPPP/2RR2K1 w - - 0 1",
//                    // Attack vs material choice.
//                    "r1b2rk1/pp1n1ppp/2p1pn2/q2p4/3P4/2NBPN2/PPQ2PPP/R1B2RK1 w - - 0 1",
//                    // Finds best defense.
//                    "r4rk1/ppp2ppp/2np1q2/4p3/2B1P3/2NP1Q2/PPP2PPP/R4RK1 b - - 0 1",
//                    // Quiet move over tactics.
//                    "r1bq1rk1/ppp2ppp/2np1n2/4p3/2B1P3/2NP1N2/PPP2PPP/R1BQ1RK1 b - - 0 1",
//                    // Best long-term plan.
//                    "r2q1rk1/pp2bppp/2npbn2/2p1p3/2P1P3/2NP1N2/PP2BPPP/R1BQ1RK1 w - - 0 1"
//            )),
//
//            Map.entry(TestCategory.POSITIONAL_PLAY, List.of(
//                    // Positional evaluation test.
//                    "2r3k1/5pp1/p3p2p/1p1pP3/3P1P2/1P3N1P/P5P1/2R3K1 w - - 0 1",
//                    // Prophylaxis and restraint.
//                    "2r2rk1/1bq1bppp/p2ppn2/1pn5/4P3/1NN1BP2/PPQ2PBP/2RR2K1 w - - 0 1",
//                    // Best long-term plan.
//                    "r2q1rk1/pp2bppp/2npbn2/2p1p3/2P1P3/2NP1N2/PP2BPPP/R1BQ1RK1 w - - 0 1"
//            )),
//
//            Map.entry(TestCategory.ENDGAMES_TECHNIQUE, List.of(
//                    // Basic opposition endgame.
//                    "8/8/8/3k4/8/4K3/4P3/8 w - - 0 1",
//                    // Distant passed pawn race.
//                    "8/2k5/8/2P5/8/5K2/6p1/8 w - - 0 1",
//                    // Rook activity and technique.
//                    "8/5pk1/6p1/8/3R4/6P1/5P1P/6K1 w - - 0 1",
//                    // Fortress recognition.
//                    "8/8/2p5/2Pp4/3P4/3K4/8/3k4 w - - 0 1",
//                    // Zugzwang and opposition.
//                    "8/8/8/2k5/2p5/2P5/3K4/8 w - - 0 1",
//                    // Queen endgame precision.
//                    "8/8/2k5/8/8/2K5/5Q2/6q1 w - - 0 1",
//                    // Bishop endgame evaluation.
//                    "8/5k2/6p1/5p2/3B1P2/6P1/5K2/8 w - - 0 1"
//            ))
    );

    private final int[] pointDistribution = {5, 3, 2, 1};
    private final int DEPTH_TO_CALC = 5;

    private record EvalResult(EvalConfig strategy, String move, int pointWon){}
    private record TestResult(TestCategory testCategory, String fen, List<String> stockfishMove, List<EvalResult> evalResult){}

    @Test
    void compareEvals() {
        // Arrange
        var evalStrategies = EvaluationUtil.getAllEvalStrategiesCombinations();
        List<TestResult> testResults = new ArrayList<>();
        stockFishService.setMultiPV(pointDistribution.length);

        // Act - calculate moves and poitns
        positionsToTest.forEach((TestCategory testCategory, List<String> positions) -> {
            positions.forEach(position -> {

                stockFishService.newGame();
                stockFishService.setPosition(position);
                var stockFishResult = stockFishService.go(DEPTH_TO_CALC);

                List<EvalResult> evalResults = new ArrayList<>();
                for (var evalStrategy : evalStrategies) {
                    var bot = new MiniMaxAlphaBetaSequential(DEPTH_TO_CALC, evalStrategy.strategy(), PlayerColor.WHITE);
                    var botResult = bot.getNextBestMove(new ChessBoard(position)).move();
                    var botPoints = this.calcPoints(stockFishResult, botResult);
                    var evalResult = new EvalResult(evalStrategy, botResult, botPoints);
                    evalResults.add(evalResult);
                }

                var testResult = new TestResult(testCategory, position, stockFishResult, evalResults);
                testResults.add(testResult);
            });
        });

        // Act

        System.out.println("blub");
    }

    private int calcPoints(List<String> stockfishMoves, String evalMove) {
        if (!stockfishMoves.contains(evalMove)) {
            return 0;
        }

        var idx = stockfishMoves.indexOf(evalMove);
        return pointDistribution[idx];
    }
}
