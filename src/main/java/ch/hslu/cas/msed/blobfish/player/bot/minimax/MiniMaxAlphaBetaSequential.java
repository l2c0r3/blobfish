package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.PathEvaluation;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MiniMaxAlgo;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MoveHistoryNode;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MoveNode;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MoveNodeMapper;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.Comparator;


public class MiniMaxAlphaBetaSequential extends MiniMaxAlgo {

    private final MoveNodeMapper moveNodeMapper = new MoveNodeMapper();

    public MiniMaxAlphaBetaSequential(final int calculationDepth, final EvalStrategy evalStrategy, final PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    public PathEvaluation getBestPath(final ChessBoard chessBoard) {
        var bestPath = calcBestPath(chessBoard, getCalculationDepth(), getOwnPlayerColor(), null, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return moveNodeMapper.mapToPathEvaluation(bestPath);
    }

    private MoveNode calcBestPath(final ChessBoard chessBoard, final int depth, final PlayerColor playerAtTurn, final MoveHistoryNode history, final int alpha, final int beta) {
        if (depth <= 0 || chessBoard.isGameOver()) {
            var eval = getEvalStrategy().getEvaluation(chessBoard);
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
            boolean isEqualButShorter = nextNode.eval() == bestNextNode.eval() && nextDepth < bestDepth;

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

        return bestNextNode;
    }

    private static String getSanOfMove(final Move move) {
        return move.toString();
    }
}
