package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.board.ChessBoard;

import java.util.concurrent.ThreadLocalRandom;

public class RandomEval implements EvalStrategy {

    @Override
    public int getEvaluation(ChessBoard board) {
        return ThreadLocalRandom.current().nextInt(-10, 11);
    }
}
