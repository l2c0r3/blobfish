package ch.hslu.cas.msed.blobfish.player.bot.minimax.cached;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MiniMaxAlgo;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.cached.base.EvaluationCache;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.cached.base.EvaluationCacheEntry;

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