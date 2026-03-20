package ch.hslu.cas.msed.blobfish.minimax.cached.base;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.evaluation.EvalStrategy;
import ch.hslu.cas.msed.blobfish.minimax.base.MiniMaxAlgo;

import java.util.Map;

public abstract class MiniMaxCachedAlgo extends MiniMaxAlgo {

    protected final EvaluationCache cache = new EvaluationCache(createCache());

    public MiniMaxCachedAlgo(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    abstract protected Map<String, EvaluationCacheEntry> createCache();

    protected void clearCache() {
        cache.clear();
    }
}