package ch.hslu.cas.msed.blobfish.evaluation;

import ch.hslu.cas.msed.blobfish.base.ChessBoard;

import java.util.concurrent.ThreadLocalRandom;

public class RandomEval implements EvalStrategy {

    @Override
    public int getEvaluation(final ChessBoard board) {
        return ThreadLocalRandom.current().nextInt(-10, 11);
    }
}
