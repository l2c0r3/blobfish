package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.board.ChessBoard;

import java.util.Map;

public class EvaluationCache {
    private final Map<String, EvaluationCacheEntry> transpositionTable;

    public EvaluationCache(final Map<String, EvaluationCacheEntry> transpositionTable) {
        this.transpositionTable = transpositionTable;
    }

    public void put(final String key, final EvaluationCacheEntry entry) {
        transpositionTable.compute(key, (_, v) -> {
            if (v == null ||
                    entry.depth() > v.depth() ||
                    // prefer EXACT matches
                    (entry.depth() == v.depth() &&
                            v.type() != EvaluationCacheEntry.BoundType.EXACT &&
                            entry.type() == EvaluationCacheEntry.BoundType.EXACT
                    )
            ) {
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

    public MoveHistoryNode buildPrincipalVariation(final ChessBoard board, final MoveHistoryNode baseHistory, final int depth) {
        MoveHistoryNode history = baseHistory;
        var currentBoard = board;

        for (int i = 0; i < depth; i++) {
            var entry = get(currentBoard.getFen(), depth - i);

            if (entry == null || entry.bestMove() == null || entry.type() != EvaluationCacheEntry.BoundType.EXACT) {
                break;
            }

            history = new MoveHistoryNode(entry.bestMove(), history);
            currentBoard = currentBoard.doMove(entry.bestMove());
        }

        return history;
    }

    public void clear() {
        transpositionTable.clear();
    }
}
