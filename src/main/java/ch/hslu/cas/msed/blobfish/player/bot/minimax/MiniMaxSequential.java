package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.LinkedList;


public class MiniMaxSequential extends MiniMaxAlgo {

    private record BottomNode(double eval, LinkedList<Move> history) {}

    public MiniMaxSequential(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    public String getBestNextMove(ChessBoard chessBoard) {
        var bestPath = calcBestPath(chessBoard, super.getCalculationDepth(), super.getOwnPlayerColor(), new LinkedList<>());

        if (bestPath == null || bestPath.history.isEmpty()) {
            return null;
        }
        return bestPath.history().getFirst().toString();
    }

    private BottomNode calcBestPath(ChessBoard chessBoard, int depth, PlayerColor playerAtTurn, LinkedList<Move> history) {
        if (depth <= 0 || chessBoard.isGameOver()) {
            var eval = super.getEvalStrategy().getEvaluation(chessBoard.getFen());
            return new BottomNode(eval, history);
        }

        var bestNextNode = PlayerColor.WHITE.equals(playerAtTurn) ? new BottomNode(Double.NEGATIVE_INFINITY, history) : new BottomNode(Double.POSITIVE_INFINITY, history);
        var hasToMaximizingEvalBar = PlayerColor.WHITE.equals(playerAtTurn);
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

        for (var move : chessBoard.legalMoves()) {
            var newPosition = chessBoard.doMove(getSanOfMove(move));
            var newHistory = copyAndAddToHistory(history, move);
            var nextNode = calcBestPath(newPosition, depth - 1, nextPlayerColor, newHistory);

            boolean isBetter = hasToMaximizingEvalBar ?
                    nextNode.eval() > bestNextNode.eval() :
                    nextNode.eval() < bestNextNode.eval();
            boolean isEqualButShorter = nextNode.eval() == bestNextNode.eval() &&
                    nextNode.history().size() < bestNextNode.history().size();

            if (isBetter || isEqualButShorter) {
                bestNextNode = nextNode;
            }
        }

        return bestNextNode;
    }

    private static String getSanOfMove(Move move) {
        return move.toString();
    }

    private LinkedList<Move> copyAndAddToHistory(LinkedList<Move> history, Move move) {
        LinkedList<Move> newHistory = new LinkedList<>(history);
        newHistory.add(move);
        return newHistory;
    }
}
