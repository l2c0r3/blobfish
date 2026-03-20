package ch.hslu.cas.msed.blobfish.player.bot.minimax.cached;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.PathEvaluation;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MoveHistoryNode;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MoveNode;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MoveNodeMapper;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.cached.base.EvaluationCacheEntry;
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
    public PathEvaluation getBestPath(ChessBoard chessBoard) {
        var bestPath = calcBestPath(chessBoard, getCalculationDepth(), getOwnPlayerColor(), null, Integer.MIN_VALUE, Integer.MAX_VALUE);
        clearCache();
        return moveNodeMapper.mapToPathEvaluation(bestPath);
    }

    private MoveNode calcBestPath(final ChessBoard chessBoard, final int depth, final PlayerColor playerAtTurn, final MoveHistoryNode history, final int alpha, final int beta) {
        // Check cache first
        var position = chessBoard.getFen();
        var cached = cache.get(position, depth);
        if (cached != null && isCacheWithinBounds(cached, alpha, beta)) {
            var newHistory = cache.buildPrincipalVariation(chessBoard, history, depth);
            return new MoveNode(cached.value(), newHistory);
        }

        if (depth <= 0 || chessBoard.isGameOver()) {
            var eval = getEvalStrategy().getEvaluation(chessBoard);
            cache.put(position, new EvaluationCacheEntry(eval, null, depth));
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
        if (cached != null) {
            moves.sort(Comparator
                    .comparing((Move m) -> !m.toString().equals(cached.bestMove()))
                    .thenComparing(chessBoard::isCapture, Comparator.reverseOrder())
            );
        } else {
            moves.sort(Comparator.comparing(chessBoard::isCapture).reversed());
        }

        String bestMove = null;
        for (var move : moves) {
            var newPosition = chessBoard.doMove(getSanOfMove(move));
            var newHistory = new MoveHistoryNode(move.toString(), history);
            var nextNode = calcBestPath(newPosition, depth - 1, nextPlayerColor, newHistory, currentAlpha, currentBeta);

            boolean isBetter = hasToMaximizingEvalBar ?
                    nextNode.eval() > bestNextNode.eval() :
                    nextNode.eval() < bestNextNode.eval();

            int nextDepth = nextNode.history() == null ? Integer.MAX_VALUE : nextNode.history().depth();
            int bestDepth = bestNextNode.history() == null ? Integer.MAX_VALUE : bestNextNode.history().depth();
            boolean isEqualButShorter = nextNode.eval() == bestNextNode.eval() && nextDepth < bestDepth;

            if (isBetter || isEqualButShorter) {
                bestNextNode = nextNode;
                bestMove = move.toString();
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

        var boundType = determineBoundType(bestNextNode.eval(), alpha, beta);
        assert bestNextNode.history() != null;
        cache.put(position, new EvaluationCacheEntry(bestNextNode.eval(), depth, bestMove, boundType));

        return bestNextNode;
    }

    private EvaluationCacheEntry.BoundType determineBoundType(final int eval, final int alpha, final int beta) {
        if (eval <= alpha) {
            return EvaluationCacheEntry.BoundType.UPPER_BOUND;
        } else if (eval >= beta) {
            return EvaluationCacheEntry.BoundType.LOWER_BOUND;
        } else {
            return EvaluationCacheEntry.BoundType.EXACT;
        }
    }

    private boolean isCacheWithinBounds(final EvaluationCacheEntry entry, final int alpha, final int beta) {
        return switch (entry.type()) {
            case EXACT -> true;
            case LOWER_BOUND -> entry.value() >= beta;
            case UPPER_BOUND -> entry.value() <= alpha;
        };
    }

    private static String getSanOfMove(final Move move) {
        return move.toString();
    }
}
