package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveNodeMapperTest {

    MoveNodeMapper testee = new MoveNodeMapper();

    @Test
    void mapToFirstMoveEvaluation_null_returns_null() {
        assertNull(testee.mapToFirstMoveEvaluation(null));
    }

    @Test
    void mapToFirstMoveEvaluation_noHistory_returnsNull() {
        // Arrange
        var moveNode = new MoveNode(1, null);

        // Act
        var result = testee.mapToFirstMoveEvaluation(moveNode);

        // Assert
        assertNull(result);
    }

    @Test
    void mapToFirstMoveEvaluation_oneHistory_returnsCorrect() {
        // Arrange
        var moveHistoryNode = new MoveHistoryNode("e2", null);
        var moveNode = new MoveNode(1, moveHistoryNode);

        // Act
        var result = testee.mapToFirstMoveEvaluation(moveNode);

        // Assert
        assertEquals("e2", result.move());
        assertEquals(1, result.eval());
    }

    @Test
    void mapToFirstMoveEvaluation_multipleHistory_returnsCorrect() {
        // Arrange
        var parent = new MoveHistoryNode("e2", null);
        var moveHistoryNode = new MoveHistoryNode("e3", parent);
        var moveNode = new MoveNode(1, moveHistoryNode);

        // Act
        var result = testee.mapToFirstMoveEvaluation(moveNode);

        // Assert
        assertEquals("e2", result.move());
        assertEquals(1, result.eval());
    }

}