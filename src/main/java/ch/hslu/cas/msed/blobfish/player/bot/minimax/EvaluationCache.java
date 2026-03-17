package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import java.util.Map;

public class EvaluationCache {
    private final Map<String, EvaluationCacheEntry> transpositionTable;

    public EvaluationCache(final Map<String, EvaluationCacheEntry> transpositionTable) {
        this.transpositionTable = transpositionTable;
    }

    public void put(final String key, final EvaluationCacheEntry entry) {
        transpositionTable.compute(key, (_, v) -> {
            if (v == null || entry.depth() > v.depth()) {
                return entry;
            } else {
                return v;
            }
        });
    }

    public EvaluationCacheEntry get(final String key, final int depth) {
        var entry = transpositionTable.get(key);
        if (entry == null || entry.depth() < depth) return null;

        return entry;
    }

    public void clear() {
        transpositionTable.clear();
    }
}
