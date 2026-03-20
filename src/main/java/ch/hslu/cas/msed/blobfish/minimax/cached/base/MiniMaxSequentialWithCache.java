package ch.hslu.cas.msed.blobfish.minimax.cached.base;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.evaluation.EvalStrategy;
import ch.hslu.cas.msed.blobfish.minimax.base.PathEvaluation;
import ch.hslu.cas.msed.blobfish.minimax.base.MoveHistoryNode;
import ch.hslu.cas.msed.blobfish.minimax.base.MoveNode;
import ch.hslu.cas.msed.blobfish.minimax.base.MoveNodeMapper;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.HashMap;
import java.util.Map;


public class MiniMaxSequentialWithCache extends MiniMaxCachedAlgo {

    private final MoveNodeMapper moveNodeMapper = new MoveNodeMapper();

    public MiniMaxSequentialWithCache(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    protected Map<String, EvaluationCacheEntry> createCache() {
        return new HashMap<>();
    }

    @Override
    public PathEvaluation getBestPath(ChessBoard chessBoard) {
        var bestPath = calcBestPath(chessBoard, getCalculationDepth(), getOwnPlayerColor(), null);
        clearCache();
        return moveNodeMapper.mapToPathEvaluation(bestPath);
    }

    private MoveNode calcBestPath(ChessBoard chessBoard, int depth, PlayerColor playerAtTurn, MoveHistoryNode history) {
        // Check cache first
        var position = chessBoard.getFen();
        var cached = cache.get(position, depth);
        if (cached != null) {
            var newHistory = cache.buildPrincipalVariation(chessBoard, history, depth);
            return new MoveNode(cached.value(), newHistory);
        }

        if (depth <= 0 || chessBoard.isGameOver()) {
            var eval = getEvalStrategy().getEvaluation(chessBoard);
            cache.put(position, new EvaluationCacheEntry(eval, null, depth));
            return new MoveNode(eval, history);
        }

        var bestNextNode = PlayerColor.WHITE.equals(playerAtTurn) ? new MoveNode(Integer.MIN_VALUE, history) : new MoveNode(Integer.MAX_VALUE, history);
        var hasToMaximizingEvalBar = PlayerColor.WHITE.equals(playerAtTurn);
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

        String bestMove = null;
        for (var move : chessBoard.legalMoves()) {
            var newPosition = chessBoard.doMove(getSanOfMove(move));
            var newHistory = new MoveHistoryNode(move.toString(), history);
            var nextNode = calcBestPath(newPosition, depth - 1, nextPlayerColor, newHistory);

            boolean isBetter = hasToMaximizingEvalBar ?
                    nextNode.eval() > bestNextNode.eval() :
                    nextNode.eval() < bestNextNode.eval();

            int nextDepth = nextNode.history() == null ? Integer.MAX_VALUE : nextNode.history().depth();
            int bestDepth = bestNextNode.history() == null ? Integer.MAX_VALUE : bestNextNode.history().depth();
            boolean isEqualButShorter = Double.compare(nextNode.eval(), bestNextNode.eval()) == 0 && nextDepth < bestDepth;

            if (isBetter || isEqualButShorter) {
                bestNextNode = nextNode;
                bestMove = move.toString();
            }
        }

        assert bestNextNode.history() != null;
        cache.put(position, new EvaluationCacheEntry(bestNextNode.eval(), bestMove, depth));

        return bestNextNode;
    }

    private static String getSanOfMove(Move move) {
        return move.toString();
    }
}
