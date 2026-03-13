package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.FirstMoveEvaluation;

import java.util.concurrent.ForkJoinPool;

public class MiniMaxParallel extends MiniMaxAlgo {

    private final MoveNodeMapper moveNodeMapper = new MoveNodeMapper();

    public MiniMaxParallel(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    public FirstMoveEvaluation getNextBestMove(ChessBoard chessBoard) {
        var task = new MiniMaxRecursiveTask(getEvalStrategy(), chessBoard, getCalculationDepth(), getOwnPlayerColor(), null);

        @SuppressWarnings("resource")
        var forkJoinPool = ForkJoinPool.commonPool();
        var resultNode = forkJoinPool.invoke(task);

        return moveNodeMapper.mapToFirstMoveEvaluation(resultNode);
    }
}

