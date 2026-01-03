package ch.hslu.cas.msed.blobfish.searchtree;

import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import com.github.bhlangonijr.chesslib.move.Move;

public class MiniMax {

    private final int calculationDepth;
    private final EvalStrategy evalStrategy;

    public MiniMax(int calculationDepth, EvalStrategy evalStrategy) {
        this.calculationDepth = calculationDepth;
        this.evalStrategy = evalStrategy;
    }

    public String getBestNextMove(ChessBoard chessPosition) {
        Move bestMove = null;
        var maxEval = Double.MIN_VALUE;

        for (var move : chessPosition.legalMoves()) {
            var newPosition = chessPosition.doMove(move.getSan());
            var eval = calcMiniMax(newPosition, calculationDepth - 1, false);
            if (eval > maxEval) {
                bestMove = move;
                maxEval = eval;
            }
        }

        return bestMove.getSan();
    }

    private double calcMiniMax(ChessBoard position, int depth, boolean maximizingPlayer) {
        if (depth <= 0 || position.isGameOver()) {
            return evalStrategy.getEvaluation(position.getFen());
        }

        if (maximizingPlayer) {
            var maxEval = Double.MIN_VALUE;
            for (var move : position.legalMoves()) {
                var newPosition = position.doMove(move.getSan());
                var eval = calcMiniMax(newPosition, depth - 1, false);
                maxEval = Double.max(maxEval, eval);
            }
            return maxEval;
        } else {
            var minEval = Double.MAX_VALUE;
            for (var move : position.legalMoves()) {
                var newPosition = position.doMove(move.getSan());
                var eval = calcMiniMax(newPosition, depth - 1, true);
                minEval = Double.min(minEval, eval);
            }
            return minEval;
        }
    }
}
