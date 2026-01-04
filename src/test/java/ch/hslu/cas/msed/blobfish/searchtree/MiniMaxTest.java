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

    private static Stream<Arguments> oneMovePositionsProvider() {
        return Stream.of(
            Arguments.of(new ChessBoard("k7/8/8/3r4/8/3R4/8/K7 w - - 0 1"), "d3d5"),  // eat free rook
            Arguments.of(new ChessBoard("7k/8/5NPK/8/8/8/8/8 w - - 0 1"), "g6g7"),  // mateInOne
            Arguments.of(new ChessBoard("8/8/8/1r2k3/8/7R/8/K7 w - - 0 1"), "h3h5")  // dont now how its called
        );
    }

    @ParameterizedTest
    @MethodSource("oneMovePositionsProvider")
    void getBestNextMove_asWhite_materialStrategy_depth2_returnsExpected(ChessBoard board, String expectedNextMove) {
        // Arrange
        var testee = new MiniMax(4, new MateAwareEval(new MaterialEval()), PlayerColor.WHITE);

        // Act
        String result = testee.getBestNextMove(board);

        // Assert
        assertEquals(expectedNextMove, result);
    }



}