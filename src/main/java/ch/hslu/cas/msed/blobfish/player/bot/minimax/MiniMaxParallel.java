package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;

import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;

public class MiniMaxParallel extends MiniMaxAlgo {
    public MiniMaxParallel(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    public String getNextBestMove(ChessBoard chessBoard) {
        var task = new MiniMaxRecursiveTask(getEvalStrategy(), chessBoard, getCalculationDepth(), getOwnPlayerColor(), new LinkedList<>());

        @SuppressWarnings("resource")
        var forkJoinPool = ForkJoinPool.commonPool();
        var resultNode = forkJoinPool.invoke(task);

        if (resultNode == null || resultNode.history().isEmpty()) {
            return null;
        } else {
            return resultNode.history().getFirst().toString();
        }
    }
}

