package ch.hslu.cas.msed.blobfish.player.bot.minimax.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MoveNodeMapperTest {

    MoveNodeMapper testee = new MoveNodeMapper();

    @Test
    void mapToPathEvaluation_null_returns_null() {
        assertNull(testee.mapToPathEvaluation(null));
    }

    @Test
    void mapToPathEvaluation_noHistory_returnsNull() {
        // Arrange
        var moveNode = new MoveNode(1, null);

        // Act
        var result = testee.mapToPathEvaluation(moveNode);

        // Assert
        assertNull(result);
    }

    @Test
    void mapToPathEvaluation_oneHistory_returnsCorrect() {
        // Arrange
        var moveHistoryNode = new MoveHistoryNode("e2", null);
        var moveNode = new MoveNode(1, moveHistoryNode);

        // Act
        var result = testee.mapToPathEvaluation(moveNode);

        // Assert
        assertEquals("e2", result.move().getFirst());
        assertEquals(1, result.eval());
    }

    @Test
    void mapToPathEvaluation_multipleHistory_returnsCorrect() {
        // Arrange
        var parent = new MoveHistoryNode("e2", null);
        var moveHistoryNode1 = new MoveHistoryNode("e3", parent);
        var moveHistoryNode2 = new MoveHistoryNode("e4", moveHistoryNode1);
        var moveNode = new MoveNode(1, moveHistoryNode2);

        // Act
        var result = testee.mapToPathEvaluation(moveNode);

        // Assert
        assertEquals("e2", result.move().getFirst());
        assertEquals("e3", result.move().get(1));
        assertEquals("e4", result.move().get(2));
        assertEquals(1, result.eval());
    }

}