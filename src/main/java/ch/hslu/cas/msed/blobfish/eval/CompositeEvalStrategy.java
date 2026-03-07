package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.board.ChessBoard;

import java.util.ArrayList;
import java.util.List;

public class CompositeEvalStrategy implements EvalStrategy {

    private final List<EvalStrategy> strategies;

    private CompositeEvalStrategy(final List<EvalStrategy> strategies) {
        this.strategies = List.copyOf(strategies);
    }

    public static Builder builder() {
        return new Builder();
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

        public Builder add(final EvalStrategy strategy) {
            strategies.add(strategy);
            return this;
        }

        public CompositeEvalStrategy build() {
            return new CompositeEvalStrategy(strategies);
        }
    }
}