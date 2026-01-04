package ch.hslu.cas.msed.blobfish.searchtree;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import com.github.bhlangonijr.chesslib.move.Move;

public class MiniMax {

    private final int calculationDepth;
    private final EvalStrategy evalStrategy;
    private final PlayerColor ownPlayerColor;

    public MiniMax(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor){
        this.calculationDepth = calculationDepth;
        this.evalStrategy = evalStrategy;
        this.ownPlayerColor = ownPlayerColor;
    }

    public String getBestNextMove(ChessBoard chessPosition) {
        Move bestMoveNextMove = null;
        var bestEval = PlayerColor.WHITE.equals(this.ownPlayerColor) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        var hasToMaximizingEvalBar = PlayerColor.WHITE.equals(this.ownPlayerColor);
        var nextPlayerColor = PlayerColor.WHITE.equals(this.ownPlayerColor) ? PlayerColor.BLACK : PlayerColor.WHITE;

        for (var move : chessPosition.legalMoves()) {
            var newPosition = chessPosition.doMove(move.getSan());
            var eval = calcNextPosition(newPosition, calculationDepth - 1, nextPlayerColor);

            if (hasToMaximizingEvalBar) {
                if (eval > bestEval) {
                    bestEval = eval;
                    bestMoveNextMove = move;
                }
            } else {
                if (eval < bestEval) {
                    bestEval = eval;
                    bestMoveNextMove = move;
                }
            }
        }

        return bestMoveNextMove != null ? bestMoveNextMove.getSan() : null;
    }

    private double calcNextPosition(ChessBoard position, int depth, PlayerColor playerAtTurn) {
        if (depth <= 0 || position.isGameOver()) {
            return evalStrategy.getEvaluation(position.getFen());
        }

        var bestEval = PlayerColor.WHITE.equals(playerAtTurn) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        var hasToMaximizingEvalBar = PlayerColor.WHITE.equals(playerAtTurn);
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

        for (var move : position.legalMoves()) {
            var newPosition = position.doMove(move.getSan());
            var eval = calcNextPosition(newPosition, depth - 1, nextPlayerColor);

            if (hasToMaximizingEvalBar) {
                bestEval = Math.max(bestEval, eval);
            } else {
                bestEval = Math.min(bestEval, eval);
            }
        }

        return bestEval;
    }
}
