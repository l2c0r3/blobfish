package ch.hslu.cas.msed.blobfish.board;

record EvalBar(String positionFen) {

    /**
     * A positive number (e.g., +1.5) means White has an advantage equal to about one and a half pawns;
     * a negative number (e.g., -2.0) means Black has the edge.
     */
    int getEvaluation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
