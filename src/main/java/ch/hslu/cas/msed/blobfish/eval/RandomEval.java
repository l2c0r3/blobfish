package ch.hslu.cas.msed.blobfish.eval;

import java.util.Random;

public class RandomEval implements EvalStrategy {

    private final Random r = new Random();

    @Override
    public double getEvaluation(String positionFen) {
        return r.nextDouble();
    }
}
