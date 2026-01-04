package ch.hslu.cas.msed.blobfish.player.bot;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxSequential;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniMaxSequentialTest {

    private static Stream<Arguments> oneMovePositionsProviderWhite() {
        return Stream.of(
            Arguments.of(new ChessBoard("k7/8/8/3r4/8/3R4/8/K7 w - - 0 1"), "d3d5"),  // eat free rook
                Arguments.of(new ChessBoard("7k/8/5NPK/8/8/8/8/8 w - - 0 1"), "g6g7"),        // mateInOne
                Arguments.of(new ChessBoard("6k1/1R6/2R5/8/8/8/8/K7 w - - 0 1"), "c6c8"),     // shortes mate
                Arguments.of(new ChessBoard("8/8/8/1r2k3/8/7R/8/K7 w - - 0 1"), "h3h5"),       // don't know how its called
                Arguments.of(new ChessBoard("r5k1/8/8/8/8/p7/8/R1B1K3 w - - 0 1"), "c1a3")       // do best trade
        );
    }

    @ParameterizedTest
    @MethodSource("oneMovePositionsProviderWhite")
    void getBestNextMove_asWhite_materialStrategy_depth3_returnsExpected(ChessBoard board, String expectedNextMove) {
        // Arrange
        var testee = new MiniMaxSequential(3, new MateAwareEval(new MaterialEval()), PlayerColor.WHITE);

        // Act
        String result = testee.getBestNextMove(board);

        // Assert
        assertEquals(expectedNextMove, result);
    }

    private static Stream<Arguments> oneMovePositionsProviderBlack() {
        return Stream.of(
                Arguments.of(new ChessBoard("7k/8/8/8/8/4p3/3R4/K7 b - - 0 1"), "e3d2"),  // eat free rook
                Arguments.of(new ChessBoard("7k/8/8/8/8/pq6/8/K7 b - - 0 1"), "b3b2"),  // mateInOne
                Arguments.of(new ChessBoard("7k/8/8/8/1r6/1q6/8/K7 b - - 0 1"), "b4a4"),  // shortest mate
                Arguments.of(new ChessBoard("8/8/5R2/8/3K4/8/2q5/k7 b - - 0 1"), "c2b2")  // don't know how its called
        );
    }

    @ParameterizedTest
    @MethodSource("oneMovePositionsProviderBlack")
    void getBestNextMove_asBlack_materialStrategy_depth3_returnsExpected(ChessBoard board, String expectedNextMove) {
        // Arrange
        var testee = new MiniMaxSequential(4, new MateAwareEval(new MaterialEval()), PlayerColor.BLACK);

        // Act
        String result = testee.getBestNextMove(board);

        // Assert
        assertEquals(expectedNextMove, result);
    }

    @Test
    @Disabled("Look a further time")
    void hello() {
        var testee = new MiniMaxSequential(4, new MateAwareEval(new MaterialEval()), PlayerColor.BLACK);
        var result = testee.getBestNextMove(new ChessBoard("r1b5/6k1/P7/8/8/8/6K1/R7 b - - 0 1"));

        assertEquals("c8a6", result);
    }

}