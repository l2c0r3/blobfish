package ch.hslu.cas.msed.blobfish.searchtree;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniMaxTest {

    private static Stream<Arguments> oneMovePositionsProviderWhite() {
        return Stream.of(
            Arguments.of(new ChessBoard("k7/8/8/3r4/8/3R4/8/K7 w - - 0 1"), "d3d5"),  // eat free rook
            Arguments.of(new ChessBoard("7k/8/5NPK/8/8/8/8/8 w - - 0 1"), "g6g7"),  // mateInOne
            Arguments.of(new ChessBoard("8/8/8/1r2k3/8/7R/8/K7 w - - 0 1"), "h3h5")  // dont now how its called
        );
    }

    @ParameterizedTest
    @MethodSource("oneMovePositionsProviderWhite")
    void getBestNextMove_asWhite_materialStrategy_depth3_returnsExpected(ChessBoard board, String expectedNextMove) {
        // Arrange
        var testee = new MiniMax(3, new MateAwareEval(new MaterialEval()), PlayerColor.WHITE);

        // Act
        String result = testee.getBestNextMove(board);

        // Assert
        assertEquals(expectedNextMove, result);
    }

    private static Stream<Arguments> oneMovePositionsProviderBlack() {
        return Stream.of(
                Arguments.of(new ChessBoard("7k/8/8/8/8/4p3/3R4/K7 b - - 0 1"), "e3d2"),  // eat free rook
                Arguments.of(new ChessBoard("7k/8/8/8/8/pq6/8/K7 b - - 0 1"), "b3b2"),  // mateInOne
                Arguments.of(new ChessBoard("8/8/5R2/8/3K4/8/2q5/k7 b - - 0 1"), "c2b2")  // dont now how its called
        );
    }

    @ParameterizedTest
    @MethodSource("oneMovePositionsProviderBlack")
    void getBestNextMove_asBlack_materialStrategy_depth3_returnsExpected(ChessBoard board, String expectedNextMove) {
        // Arrange
        var testee = new MiniMax(3, new MateAwareEval(new MaterialEval()), PlayerColor.BLACK);

        // Act
        String result = testee.getBestNextMove(board);

        // Assert
        assertEquals(expectedNextMove, result);
    }

}