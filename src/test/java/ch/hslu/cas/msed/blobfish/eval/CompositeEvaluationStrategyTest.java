package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompositeEvaluationStrategyTest {

    @Test
    void getEvaluation_strategiesAreEvaluatedInCorrectOrder() {
        // Arrange
        List<String> callOrder = new ArrayList<>();

        EvaluationStrategy s1 = _ -> {
            callOrder.add("s1");
            return 1;
        };

        EvaluationStrategy s2 = _ -> {
            callOrder.add("s2");
            return 2;
        };

        EvaluationStrategy s3 = _ -> {
            callOrder.add("s3");
            return 3;
        };

        // Act
        EvaluationStrategy composite = CompositeEvaluationStrategy.builder()
                .add(s1)
                .add(s2)
                .add(s3)
                .build();

        composite.getEvaluation(new ChessBoard());

        // Assert
        assertEquals(List.of("s1", "s2", "s3"), callOrder);
    }

    @Test
    void getEvaluation() {
        // Arrange
        EvaluationStrategy s1 = _ -> 100;
        EvaluationStrategy s2 = _ -> 250;
        EvaluationStrategy s3 = _ -> 310;

        var expectedEvaluation = 660;

        // Act
        EvaluationStrategy composite = CompositeEvaluationStrategy.builder()
                .add(s1)
                .add(s2)
                .add(s3)
                .build();

        var evaluation = composite.getEvaluation(new ChessBoard());

        // Assert
        assertEquals(expectedEvaluation, evaluation);
    }

    @Test
    void getStrategies() {
        // Arrange
        EvaluationStrategy s1 = _ -> 100;
        EvaluationStrategy s2 = _ -> 250;
        EvaluationStrategy s3 = _ -> 310;

        var expectedStrategies = List.of(s1.getClass(), s2.getClass(), s3.getClass());

        // Act
        var composite = CompositeEvaluationStrategy.builder()
                .add(s1)
                .add(s2)
                .add(s3)
                .build();

        var actualStrategies = composite.getStrategies();

        // Assert
        assertEquals(expectedStrategies, actualStrategies);
    }
}