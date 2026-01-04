package ch.hslu.cas.msed.blobfish.player.bot;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.Collections;
import java.util.LinkedList;


/**
 * Optimize Options
 * - shortest Path to Mate e.g (6k1/1R6/2R5/8/8/8/8/K7 w - - 0 1)
 * - Threading
 * - add max Time (and also calc first with depth 1, then with depth 2)
 * - add calculated tree in return value
 * - everything in one method
 */
public class MiniMax {

    private final int calculationDepth;
    private final EvalStrategy evalStrategy;
    private final PlayerColor ownPlayerColor;

    private record ButtomNode(double eval, LinkedList<Move> history) {}

    public MiniMax(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor){
        this.calculationDepth = calculationDepth;
        this.evalStrategy = evalStrategy;
        this.ownPlayerColor = ownPlayerColor;
    }

    public String getBestNextMove(ChessBoard chessBoard) {
        var history = new LinkedList<Move>();
        var bestNextNode = PlayerColor.WHITE.equals(this.ownPlayerColor) ? new ButtomNode(Double.NEGATIVE_INFINITY, history) : new ButtomNode(Double.POSITIVE_INFINITY, history);
        var hasToMaximizingEvalBar = PlayerColor.WHITE.equals(this.ownPlayerColor);
        var nextPlayerColor = PlayerColor.WHITE.equals(this.ownPlayerColor) ? PlayerColor.BLACK : PlayerColor.WHITE;

        for (var move : chessBoard.legalMoves()) {
            var newPosition = chessBoard.doMove(getSanOfMove(move));
            var newHistory = copyAndAddToHistory(history, move);
            var nextNode = calcBestPath(newPosition, calculationDepth - 1, nextPlayerColor, newHistory);

            if (hasToMaximizingEvalBar) {
                if (nextNode.eval() > bestNextNode.eval()) {
                    bestNextNode = nextNode;
                }
            } else {
                if (nextNode.eval() < bestNextNode.eval()) {
                    bestNextNode = nextNode;
                }
            }
        }

        return bestNextNode.history().getFirst().toString();
    }

    private ButtomNode calcBestPath(ChessBoard chessBoard, int depth, PlayerColor playerAtTurn, LinkedList<Move> history) {
        if (depth <= 0 || chessBoard.isGameOver()) {
            var eval = evalStrategy.getEvaluation(chessBoard.getFen());
            return new ButtomNode(eval, history);
        }

        var bestNextNode = PlayerColor.WHITE.equals(playerAtTurn) ? new ButtomNode(Double.NEGATIVE_INFINITY, history) : new ButtomNode(Double.POSITIVE_INFINITY, history);
        var hasToMaximizingEvalBar = PlayerColor.WHITE.equals(playerAtTurn);
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

        for (var move : chessBoard.legalMoves()) {
            var newPosition = chessBoard.doMove(getSanOfMove(move));
            var newHistory = copyAndAddToHistory(history, move);
            var nextNode = calcBestPath(newPosition, depth - 1, nextPlayerColor, newHistory);

            if (hasToMaximizingEvalBar) {
                if (nextNode.eval() > bestNextNode.eval()) {
                    bestNextNode = nextNode;
                }
            } else {
                if (nextNode.eval() < bestNextNode.eval()) {
                    bestNextNode = nextNode;
                }
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
