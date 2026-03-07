package ch.hslu.cas.msed.blobfish.eval;

import java.util.List;

public class EvalWrapper implements EvalStrategy {

    private final List<EvalStrategy> strategies;

    public EvalWrapper(List<EvalStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public double getEvaluation(String positionFen) {
        return strategies.stream().parallel()
                .map(s -> s.getEvaluation(positionFen))
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
