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

    public MiniMaxParallel(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    public FirstMoveEvaluation getNextBestMove(ChessBoard chessBoard) {
        return calculate(chessBoard, moveNodeMapper::mapToFirstMoveEvaluation);
    }

    @Override
    public PathEvaluation getBestPath(ChessBoard chessBoard) {
        return calculate(chessBoard, moveNodeMapper::mapToPathEvaluation);
    }

    private <T> T calculate(ChessBoard chessBoard, Function<MoveNode, T> mapper) {
        var task = new MiniMaxRecursiveTask(getEvalStrategy(), chessBoard, getCalculationDepth(), getOwnPlayerColor(), null);

        @SuppressWarnings("resource")
        var forkJoinPool = ForkJoinPool.commonPool();
        var resultNode = forkJoinPool.invoke(task);

        return mapper.apply(resultNode);
    }
}

