package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import java.util.Map;

public class EvaluationCache {
    private final Map<Long, EvaluationCacheEntry> transpositionTable;

    public EvaluationCache(final Map<Long, EvaluationCacheEntry> transpositionTable) {
        this.transpositionTable = transpositionTable;
    }

    public void put(final Long key, final EvaluationCacheEntry entry) {
        transpositionTable.compute(key, (_, v) -> {
            if (v == null || entry.depth() > v.depth()) {
                return entry;
            } else {
                return v;
            }
        });
    }

    public EvaluationCacheEntry get(final Long key, final int depth) {
        var entry = transpositionTable.get(key);
        if (entry == null || entry.depth() < depth) return null;

        return entry;
    }

    public void clear() {
        transpositionTable.clear();
    }
}
