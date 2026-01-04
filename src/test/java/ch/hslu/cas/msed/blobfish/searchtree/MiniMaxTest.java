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
            Arguments.of(new ChessBoard("6k1/1R6/2R5/8/8/8/8/K7 w - - 0 1"), "c6c8"),  // mateInOne
            Arguments.of(new ChessBoard("8/8/8/1rk5/8/4R3/8/K7 w - - 0 1"), "e3e5")  // dont now how its called
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