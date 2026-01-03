package ch.hslu.cas.msed.blobfish.eval;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RandomEvalTest {

    private final RandomEval testee = new RandomEval();

    @Test
    void getEvaluation_isAlwaysDifferent() {
        // Assert
        var startPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        var evalList = new ArrayList<Double>();

        for (int i = 0; i < 25; i++) {
            // Act
            var result = testee.getEvaluation(startPosition);

            // Assert
            assertFalse(evalList.contains(result));
            evalList.add(result);
        }
    }
}