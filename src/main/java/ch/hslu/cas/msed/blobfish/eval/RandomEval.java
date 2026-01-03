package ch.hslu.cas.msed.blobfish.eval;

import java.util.concurrent.ThreadLocalRandom;

public class RandomEval implements EvalStrategy {

    @Override
    public double getEvaluation(String positionFen) {
        return ThreadLocalRandom.current().nextDouble(-10.0, 10.0);
    }
}
