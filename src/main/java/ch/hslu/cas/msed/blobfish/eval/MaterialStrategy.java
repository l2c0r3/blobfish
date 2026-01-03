package ch.hslu.cas.msed.blobfish.eval;

public class MaterialStrategy implements EvalStrategy {

    /**
     * A positive number (e.g., +1.0) means White has an advantage equal of a pawn;
     * a negative number (e.g., -2.0) means Black has the edge.
     */
    @Override
    public double getEvaluation(String positionFen) {
        return 0;
    }


}
