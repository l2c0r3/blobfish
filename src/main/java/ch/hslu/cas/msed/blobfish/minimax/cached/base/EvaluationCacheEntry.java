package ch.hslu.cas.msed.blobfish.minimax.cached.base;

public record EvaluationCacheEntry(int value, int depth, String bestMove, BoundType type) {

    /**
     * This constructor should be used in non-alpha-beta pruning implementations, because the BoundType is only relevant for alpha-beta pruning.
     *
     * @param value    The evaluation of the position
     * @param depth    The depth at which the evaluation was made
     * @param bestMove The bes move in the position
     */
    public EvaluationCacheEntry(final int value, final String bestMove, final int depth) {
        this(value, depth, bestMove, BoundType.EXACT);
    }

    public enum BoundType {
        EXACT,
        LOWER_BOUND,
        UPPER_BOUND
    }
}
