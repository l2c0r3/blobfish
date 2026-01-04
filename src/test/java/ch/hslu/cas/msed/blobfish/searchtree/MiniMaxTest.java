package ch.hslu.cas.msed.blobfish.searchtree;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniMaxTest {

    private static Stream<Arguments> oneMovePositionsProvider() {
        return Stream.of(
            Arguments.of(new ChessBoard("k7/8/8/3r4/8/3R4/8/K7 w - - 0 1"), "d3d5")
        );
    }

    @ParameterizedTest
    @MethodSource("oneMovePositionsProvider")
    void getBestNextMove_materialStrategy_depth2_returnsExpected(ChessBoard board, String expectedNextMove) {
        // Arrange
        var testee = new MiniMax(2, new MaterialEval(), PlayerColor.WHITE);

        // Act
        String result = testee.getBestNextMove(board);

        // Assert
        assertEquals(expectedNextMove, result);
    }



}