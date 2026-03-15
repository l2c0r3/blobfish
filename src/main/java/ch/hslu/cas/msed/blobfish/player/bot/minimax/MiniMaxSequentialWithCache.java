package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.FirstMoveEvaluation;
import ch.hslu.cas.msed.blobfish.player.bot.PathEvaluation;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.HashMap;
import java.util.Map;


public class MiniMaxSequentialWithCache extends MiniMaxCachedAlgo {

    private final MoveNodeMapper moveNodeMapper = new MoveNodeMapper();

    public MiniMaxSequentialWithCache(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    protected Map<Long, EvaluationCacheEntry> createCache() {
        return new HashMap<>();
    }

    @Override
    public FirstMoveEvaluation getNextBestMove(ChessBoard chessBoard) {
        var bestPath = calcBestPath(chessBoard, getCalculationDepth(), getOwnPlayerColor(), null);
        clearCache();
        return moveNodeMapper.mapToFirstMoveEvaluation(bestPath);
    }

    @Override
    public PathEvaluation getBestPath(ChessBoard chessBoard) {
        var bestPath = calcBestPath(chessBoard, getCalculationDepth(), getOwnPlayerColor(), null);
        clearCache();
        return moveNodeMapper.mapToPathEvaluation(bestPath);
    }

    private MoveNode calcBestPath(ChessBoard chessBoard, int depth, PlayerColor playerAtTurn, MoveHistoryNode history) {
        // Check cache first
        long cacheHash = chessBoard.getFen().hashCode();
        var cached = cache.get(cacheHash, depth);
        if (cached != null) {
            return new MoveNode(cached.value(), history);
        }

        if (depth <= 0 || chessBoard.isGameOver()) {
            var eval = getEvalStrategy().getEvaluation(chessBoard);
            cache.put(cacheHash, new EvaluationCacheEntry(eval, depth));
            return new MoveNode(eval, history);
        }

        var bestNextNode = PlayerColor.WHITE.equals(playerAtTurn) ? new MoveNode(Integer.MIN_VALUE, history) : new MoveNode(Integer.MAX_VALUE, history);
        var hasToMaximizingEvalBar = PlayerColor.WHITE.equals(playerAtTurn);
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

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
            }
        }

        cache.put(cacheHash, new EvaluationCacheEntry(bestNextNode.eval(), depth));

        return bestNextNode;
    }

    private static String getSanOfMove(Move move) {
        return move.toString();
    }
}
