package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.FirstMoveEvaluation;
import ch.hslu.cas.msed.blobfish.player.bot.PathEvaluation;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class MiniMaxAlphaBetaSequentialWithCache extends MiniMaxCachedAlgo {

    private final MoveNodeMapper moveNodeMapper = new MoveNodeMapper();

    public MiniMaxAlphaBetaSequentialWithCache(final int calculationDepth, final EvalStrategy evalStrategy, final PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    protected Map<String, EvaluationCacheEntry> createCache() {
        return new HashMap<>();
    }

    @Override
    public FirstMoveEvaluation getNextBestMove(final ChessBoard chessBoard) {
        var bestPath = calcBestPath(chessBoard, getCalculationDepth(), getOwnPlayerColor(), null, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        clearCache();
        return moveNodeMapper.mapToFirstMoveEvaluation(bestPath);
    }

    @Override
    public PathEvaluation getBestPath(ChessBoard chessBoard) {
        var bestPath = calcBestPath(chessBoard, getCalculationDepth(), getOwnPlayerColor(), null, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        clearCache();
        return moveNodeMapper.mapToPathEvaluation(bestPath);
    }

    private MoveNode calcBestPath(final ChessBoard chessBoard, final int depth, final PlayerColor playerAtTurn, final MoveHistoryNode history, final double alpha, final double beta) {
        // Check cache first
        var position = chessBoard.getFen();
        var cached = cache.get(position, depth);
        if (cached != null) {
            return new MoveNode(cached.value(), history);
        }

        if (depth <= 0 || chessBoard.isGameOver()) {
            var eval = getEvalStrategy().getEvaluation(chessBoard);
            cache.put(position, new EvaluationCacheEntry(eval, depth));
            return new MoveNode(eval, history);
        }

        var currentAlpha = alpha;
        var currentBeta = beta;

        var bestNextNode = PlayerColor.WHITE.equals(playerAtTurn) ? new MoveNode(Integer.MIN_VALUE, history) : new MoveNode(Integer.MAX_VALUE, history);
        var hasToMaximizingEvalBar = PlayerColor.WHITE.equals(playerAtTurn);
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

        // sorting the moves should make pruning more reliable
        // this can be improved upon - the better the move ordering the better the alpha beta pruning is too
        var moves = chessBoard.legalMoves();
        moves.sort(Comparator.comparing(chessBoard::isCapture).reversed());

        for (var move : moves) {
            var newPosition = chessBoard.doMove(getSanOfMove(move));
            var newHistory = new MoveHistoryNode(move.toString(), history);
            var nextNode = calcBestPath(newPosition, depth - 1, nextPlayerColor, newHistory, currentAlpha, currentBeta);

            boolean isBetter = hasToMaximizingEvalBar ?
                    nextNode.eval() > bestNextNode.eval() :
                    nextNode.eval() < bestNextNode.eval();

            int nextDepth = nextNode.history() == null ? Integer.MAX_VALUE : nextNode.history().depth();
            int bestDepth = bestNextNode.history() == null ? Integer.MAX_VALUE : bestNextNode.history().depth();
            boolean isEqualButShorter = Double.compare(nextNode.eval(), bestNextNode.eval()) == 0 && nextDepth < bestDepth;

            if (isBetter || isEqualButShorter) {
                bestNextNode = nextNode;
            }

            // Update alpha / beta
            if (hasToMaximizingEvalBar) {
                currentAlpha = Math.max(currentAlpha, bestNextNode.eval());
            } else {
                currentBeta = Math.min(currentBeta, bestNextNode.eval());
            }

            if (currentBeta <= currentAlpha) {
                break;
            }
        }

        cache.put(position, new EvaluationCacheEntry(bestNextNode.eval(), depth));

        return bestNextNode;
    }

    private static String getSanOfMove(final Move move) {
        return move.toString();
    }
}
