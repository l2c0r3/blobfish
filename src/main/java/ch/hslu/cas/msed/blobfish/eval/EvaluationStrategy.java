package ch.hslu.cas.msed.blobfish.eval;


import ch.hslu.cas.msed.blobfish.base.ChessBoard;

public interface EvaluationStrategy {

    /**
     * A positive number (e.g., +1.5) means White has an advantage;
     * a negative number (e.g., -2.0) means Black has the edge.
     */
    int getEvaluation(final ChessBoard board);
}
