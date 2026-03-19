package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.FirstMoveEvaluation;
import ch.hslu.cas.msed.blobfish.player.bot.PathEvaluation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

public class MiniMaxParallelWithCache extends MiniMaxCachedAlgo {

    private final MoveNodeMapper moveNodeMapper = new MoveNodeMapper();

    private static final int MIN_DEPTH_THRESHOLD = 1;
    private static final int DEPTH_THRESHOLD_PERCENTAGE = 33;
    private static final int MOVE_THRESHOLD = 6;

    public MiniMaxParallelWithCache(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }

    @Override
    protected Map<String, EvaluationCacheEntry> createCache() {
        return new ConcurrentHashMap<>();
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
        var task = new MiniMaxRecursiveWithCacheTask(getEvalStrategy(), chessBoard, getCalculationDepth(), getOwnPlayerColor(), null, calculateDepthThreshold(), MOVE_THRESHOLD, cache);

        @SuppressWarnings("resource")
        var forkJoinPool = ForkJoinPool.commonPool();
        var resultNode = forkJoinPool.invoke(task);
        clearCache();

        return mapper.apply(resultNode);
    }

    private int calculateDepthThreshold() {
        return Math.max(MIN_DEPTH_THRESHOLD, (int) Math.round(getCalculationDepth() * DEPTH_THRESHOLD_PERCENTAGE / 100.0));
    }
}

