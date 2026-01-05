package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;

/**
 * Wrapper Class
 */
public class MateAwareEval implements EvalStrategy {

    private static final double MAX_NUMBER = 1_000;
    private final EvalStrategy evalStrategy;

    public MateAwareEval(EvalStrategy evalStrategy) {
        this.evalStrategy = evalStrategy;
    }

    @Override
    public double getEvaluation(String positionFen) {
        var board = new ChessBoard(positionFen);

        if (board.isMated()) {
            return PlayerColor.WHITE.equals(board.getSideToMove()) ? -MAX_NUMBER : MAX_NUMBER;
        }

        return evalStrategy.getEvaluation(positionFen);
    }

}
