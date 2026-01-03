package ch.hslu.cas.msed.blobfish.searchtree;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import com.github.bhlangonijr.chesslib.move.Move;

public class MiniMax {

    private final int calculationDepth;
    private final EvalStrategy evalStrategy;
    private final PlayerColor colorToMaximize;

    public MiniMax(int calculationDepth, EvalStrategy evalStrategy, PlayerColor player){
        this.calculationDepth = calculationDepth;
        this.evalStrategy = evalStrategy;
        this.colorToMaximize = player;
    }

    public String getBestNextMove(ChessBoard chessPosition) {
        Move bestMove = null;

        boolean maximizingAtRoot = colorToMaximize.equals(chessPosition.getSideToMove());
        double bestEval = maximizingAtRoot ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        for (var move : chessPosition.legalMoves()) {
            var newPosition = chessPosition.doMove(move.getSan());
            var eval = calcMiniMax(newPosition, calculationDepth - 1);

            if (maximizingAtRoot) {
                if (eval > bestEval) {
                    bestEval = eval;
                    bestMove = move;
                }
            } else {
                if (eval < bestEval) {
                    bestEval = eval;
                    bestMove = move;
                }
            }
        }

        return bestMove != null ? bestMove.getSan() : null;
    }

    private double calcMiniMax(ChessBoard position, int depth) {
        if (depth <= 0 || position.isGameOver()) {
            return evaluateForMaximizer(position);
        }

        boolean maximizingPlayer = colorToMaximize.equals(position.getSideToMove());

        if (maximizingPlayer) {
            double maxEval = Double.NEGATIVE_INFINITY;
            for (var move : position.legalMoves()) {
                var newPosition = position.doMove(move.getSan());
                maxEval = Math.max(maxEval, calcMiniMax(newPosition, depth - 1));
            }
            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            for (var move : position.legalMoves()) {
                var newPosition = position.doMove(move.getSan());
                minEval = Math.min(minEval, calcMiniMax(newPosition, depth - 1));
            }
            return minEval;
        }
    }

    private double evaluateForMaximizer(ChessBoard position) {
        double whitePositiveEval = evalStrategy.getEvaluation(position.getFen());
        // eval bar returns in favor of white
        return (colorToMaximize == PlayerColor.WHITE) ? whitePositiveEval : -whitePositiveEval;
    }
}
