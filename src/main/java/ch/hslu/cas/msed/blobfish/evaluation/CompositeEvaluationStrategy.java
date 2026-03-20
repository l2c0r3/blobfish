package ch.hslu.cas.msed.blobfish.evaluation;

import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CompositeEvaluationStrategy implements EvaluationStrategy {

    private final List<EvaluationStrategy> strategies;

    private CompositeEvaluationStrategy(final List<EvaluationStrategy> strategies) {
        this.strategies = List.copyOf(strategies);
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Class<? extends EvaluationStrategy>> getStrategies() {
        return strategies.stream().map(EvaluationStrategy::getClass).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public int getEvaluation(final ChessBoard board) {
        int evaluation = 0;

        for (EvaluationStrategy strategy : strategies) {
            evaluation += strategy.getEvaluation(board);
        }

        return evaluation;
    }

    public static class Builder {

        private final List<EvaluationStrategy> strategies = new ArrayList<>();

        public Builder add(@NonNull final EvaluationStrategy strategy) {
            strategies.add(Objects.requireNonNull(strategy, "strategy must not be null"));
            return this;
        }

        public CompositeEvaluationStrategy build() {
            if (strategies.isEmpty()) {
                throw new IllegalStateException("At least one evaluation strategy is required");
            }
            return new CompositeEvaluationStrategy(strategies);
        }
    }
}