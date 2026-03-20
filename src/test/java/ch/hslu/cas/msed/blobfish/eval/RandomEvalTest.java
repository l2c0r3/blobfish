package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomEvalTest {

    private final RandomEval testee = new RandomEval();

    @Test
    void getEvaluation_isWithinBoundsAndNotAlwaysSame() {
        // Assert
        var startPosition = new ChessBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        var evalList = new HashSet<Integer>();

        for (int i = 0; i < 1000; i++) {
            // Act
            var result = testee.getEvaluation(startPosition);
            evalList.add(result);

            // Assert
            assertTrue(-10 <= result && result <= 10);
        }

        assertTrue(evalList.size() > 1);
    }
}