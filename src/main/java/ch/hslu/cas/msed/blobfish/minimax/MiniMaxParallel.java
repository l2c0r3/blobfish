package ch.hslu.cas.msed.blobfish.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.base.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.base.PathEvaluation;
import ch.hslu.cas.msed.blobfish.minimax.base.MiniMaxAlgo;
import ch.hslu.cas.msed.blobfish.minimax.base.MiniMaxRecursiveTask;
import ch.hslu.cas.msed.blobfish.minimax.base.MoveNodeMapper;

import java.util.concurrent.ForkJoinPool;

public class MiniMaxParallel extends MiniMaxAlgo {

    private final MoveNodeMapper moveNodeMapper = new MoveNodeMapper();

    private static final int MIN_DEPTH_THRESHOLD = 1;
    private static final int DEPTH_THRESHOLD_PERCENTAGE = 33;
    private static final int MOVE_THRESHOLD = 6;

    public MiniMaxParallel(final int calculationDepth, final EvaluationStrategy evaluationStrategy, final PlayerColor ownPlayerColor) {
        super(calculationDepth, evaluationStrategy, ownPlayerColor);
    }

    @Override
    public PathEvaluation getBestPath(final ChessBoard chessBoard) {
        var task = new MiniMaxRecursiveTask(getEvaluationStrategy(), chessBoard, getCalculationDepth(), getOwnPlayerColor(), null, calculateDepthThreshold(), MOVE_THRESHOLD);

        @SuppressWarnings("resource")
        var forkJoinPool = ForkJoinPool.commonPool();
        var resultNode = forkJoinPool.invoke(task);

        return moveNodeMapper.mapToPathEvaluation(resultNode);
    }

    private int calculateDepthThreshold() {
        return Math.max(MIN_DEPTH_THRESHOLD, (int) Math.round(getCalculationDepth() * DEPTH_THRESHOLD_PERCENTAGE / 100.0));
    }
}

