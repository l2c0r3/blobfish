package ch.hslu.cas.msed.blobfish.eval;


import ch.hslu.cas.msed.blobfish.board.ChessBoard;

public interface EvalStrategy {

    /**
     * A positive number (e.g., +1.5) means White has an advantage;
     * a negative number (e.g., -2.0) means Black has the edge.
     */
    int getEvaluation(ChessBoard board);
}
