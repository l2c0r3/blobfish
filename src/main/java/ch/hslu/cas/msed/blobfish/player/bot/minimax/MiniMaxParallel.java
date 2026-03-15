package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.FirstMoveEvaluation;
import ch.hslu.cas.msed.blobfish.player.bot.PathEvaluation;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

public class MiniMaxParallel extends MiniMaxAlgo {

    private final MoveNodeMapper moveNodeMapper = new MoveNodeMapper();

    public MiniMaxParallel(final int calculationDepth, final EvalStrategy evalStrategy, final PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    public FirstMoveEvaluation getNextBestMove(final ChessBoard chessBoard) {
        return calculate(chessBoard, moveNodeMapper::mapToFirstMoveEvaluation);
    }

    @Override
    public PathEvaluation getBestPath(final ChessBoard chessBoard) {
        return calculate(chessBoard, moveNodeMapper::mapToPathEvaluation);
    }

    private <T> T calculate(final ChessBoard chessBoard, final Function<MoveNode, T> mapper) {
        var task = new MiniMaxRecursiveTask(getEvalStrategy(), chessBoard, getCalculationDepth(), getOwnPlayerColor(), null);

        @SuppressWarnings("resource")
        var forkJoinPool = ForkJoinPool.commonPool();
        var resultNode = forkJoinPool.invoke(task);

        return mapper.apply(resultNode);
    }
}

