package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import com.github.bhlangonijr.chesslib.move.Move;


public class MiniMaxSequential extends MiniMaxAlgo {
    public MiniMaxSequential(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    public String getNextBestMove(ChessBoard chessBoard) {
        var bestPath = calcBestPath(chessBoard, getCalculationDepth(), getOwnPlayerColor(), null);

        if (bestPath == null || bestPath.history() == null) {
            return null;
        }

        return bestPath.firstMove();
    }

    private MoveNode calcBestPath(ChessBoard chessBoard, int depth, PlayerColor playerAtTurn, MoveHistoryNode history) {
        if (depth <= 0 || chessBoard.isGameOver()) {
            var eval = getEvalStrategy().getEvaluation(chessBoard.getFen());
            return new MoveNode(eval, history);
        }

        var bestNextNode = PlayerColor.WHITE.equals(playerAtTurn) ? new MoveNode(Double.NEGATIVE_INFINITY, history) : new MoveNode(Double.POSITIVE_INFINITY, history);
        var hasToMaximizingEvalBar = PlayerColor.WHITE.equals(playerAtTurn);
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

        for (var move : chessBoard.legalMoves()) {
            var newPosition = chessBoard.doMove(getSanOfMove(move));
            var newHistory = new MoveHistoryNode(move.toString(), history);
            var nextNode = calcBestPath(newPosition, depth - 1, nextPlayerColor, newHistory);

            boolean isBetter = hasToMaximizingEvalBar ?
                    nextNode.eval() > bestNextNode.eval() :
                    nextNode.eval() < bestNextNode.eval();
            boolean isEqualButShorter = nextNode.eval() == bestNextNode.eval() &&
                    nextNode.history().depth() < bestNextNode.history().depth();

            if (isBetter || isEqualButShorter) {
                bestNextNode = nextNode;
            }
        }

        return bestNextNode;
    }

    private static String getSanOfMove(Move move) {
        return move.toString();
    }
}
