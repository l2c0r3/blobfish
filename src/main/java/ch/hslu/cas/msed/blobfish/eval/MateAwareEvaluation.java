package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;


public class MateAwareEvaluation implements EvaluationStrategy {

    private static final int MAX_NUMBER = 1_000_000;

    @Override
    public int getEvaluation(final ChessBoard board) {
        if (board.isMated()) {
            return PlayerColor.WHITE.equals(board.getSideToMove()) ? -MAX_NUMBER : MAX_NUMBER;
        }

        return 0;
    }

}
