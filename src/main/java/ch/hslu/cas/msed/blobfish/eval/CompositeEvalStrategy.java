package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CompositeEvalStrategy implements EvalStrategy {

    private final List<EvalStrategy> strategies;

    private CompositeEvalStrategy(final List<EvalStrategy> strategies) {
        this.strategies = List.copyOf(strategies);
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<Class<? extends EvalStrategy>> getStrategies() {
        return strategies.stream().map(EvalStrategy::getClass).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public int getEvaluation(final ChessBoard board) {
        int evaluation = 0;

        for (EvalStrategy strategy : strategies) {
            evaluation += strategy.getEvaluation(board);
        }

        return evaluation;
    }

    public static class Builder {

        private final List<EvalStrategy> strategies = new ArrayList<>();

        public Builder add(@NonNull final EvalStrategy strategy) {
            strategies.add(Objects.requireNonNull(strategy, "strategy must not be null"));
            return this;
        }

        public CompositeEvalStrategy build() {
            if (strategies.isEmpty()) {
                throw new IllegalStateException("At least one evaluation strategy is required");
            }
            return new CompositeEvalStrategy(strategies);
        }
    }
}