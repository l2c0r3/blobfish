package ch.hslu.cas.msed.blobfish.eval;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;

/**
 * Wrapper Class
 */
public class MateAwareEval implements EvalStrategy {

    private final double MAX_NUMBER = 1_000;
    private final EvalStrategy evalStrategy;

    public MateAwareEval(EvalStrategy evalStrategy) {
        this.evalStrategy = evalStrategy;
    }

    @Override
    public double getEvaluation(String positionFen) {
        var board = new Board();
        board.loadFromFen(positionFen);

        if (board.isMated()) {
            return Side.WHITE.equals(board.getSideToMove()) ? -MAX_NUMBER : MAX_NUMBER;
        }

        return evalStrategy.getEvaluation(positionFen);
    }

}
