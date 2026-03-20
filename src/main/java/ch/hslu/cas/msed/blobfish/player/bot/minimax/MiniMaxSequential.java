package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.PathEvaluation;
import com.github.bhlangonijr.chesslib.move.Move;


public class MiniMaxSequential extends MiniMaxAlgo {

    private final MoveNodeMapper moveNodeMapper = new MoveNodeMapper();

    public MiniMaxSequential(final int calculationDepth, final EvalStrategy evalStrategy, final PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    public PathEvaluation getBestPath(final ChessBoard chessBoard) {
        var bestPath = calcBestPath(chessBoard, getCalculationDepth(), getOwnPlayerColor(), null);
        return moveNodeMapper.mapToPathEvaluation(bestPath);
    }

    private MoveNode calcBestPath(final ChessBoard chessBoard, final int depth, final PlayerColor playerAtTurn, final MoveHistoryNode history) {
        if (depth <= 0 || chessBoard.isGameOver()) {
            var eval = getEvalStrategy().getEvaluation(chessBoard);
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

        return bestNextNode;
    }

    private static String getSanOfMove(final Move move) {
        return move.toString();
    }
}
