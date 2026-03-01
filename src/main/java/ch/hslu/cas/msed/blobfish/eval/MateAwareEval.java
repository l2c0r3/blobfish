package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;

/**
 * Wrapper Class
 */
public class MateAwareEval implements EvalStrategy {

    private static final int MAX_NUMBER = 1_000_000;
    private final EvalStrategy evalStrategy;

    public MateAwareEval(EvalStrategy evalStrategy) {
        this.evalStrategy = evalStrategy;
    }

    @Override
    public int getEvaluation(ChessBoard board) {
        if (board.isMated()) {
            return PlayerColor.WHITE.equals(board.getSideToMove()) ? -MAX_NUMBER : MAX_NUMBER;
        }

        return evalStrategy.getEvaluation(board);
    }

}
