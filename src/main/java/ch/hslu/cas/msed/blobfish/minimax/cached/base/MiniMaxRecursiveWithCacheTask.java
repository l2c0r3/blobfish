package ch.hslu.cas.msed.blobfish.minimax.cached.base;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.minimax.base.MoveHistoryNode;
import ch.hslu.cas.msed.blobfish.minimax.base.MoveNode;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class MiniMaxRecursiveWithCacheTask extends RecursiveTask<MoveNode> {
    private final ChessBoard chessBoard;
    private final int depth;
    private final PlayerColor playerAtTurn;
    private final MoveHistoryNode history;
    private final EvaluationStrategy evaluationStrategy;
    private final int depthThreshold;
    private final int moveThreshold;
    private final EvaluationCache cache;

    public MiniMaxRecursiveWithCacheTask(final EvaluationStrategy evaluationStrategy, final ChessBoard chessBoard, final int depth, final PlayerColor playerAtTurn, final MoveHistoryNode history, final int depthThreshold, final int moveThreshold, final EvaluationCache cache) {
        if (depth < 0) throw new IllegalArgumentException("depth cannot be negative");

        this.evaluationStrategy = evaluationStrategy;
        this.chessBoard = chessBoard;
        this.depth = depth;
        this.playerAtTurn = playerAtTurn;
        this.history = history;
        this.depthThreshold = depthThreshold;
        this.moveThreshold = moveThreshold;
        this.cache = cache;
    }

    @Override
    protected MoveNode compute() {
        // Check cache first
        var position = chessBoard.getFen();
        var cached = cache.get(position, depth);
        if (cached != null) {
            var newHistory = cache.buildPrincipalVariation(chessBoard, history, depth);
            return new MoveNode(cached.value(), newHistory);
        }

        if (depth <= 0 || chessBoard.isGameOver()) {
            var moveNode = getEvaluation();
            cache.put(position, new EvaluationCacheEntry(moveNode.eval(), null, depth));
            return moveNode;
        }

        var legalMoves = chessBoard.legalMoves()
                .stream()
                .map(Move::toString)
                .toList();
        var tasks = createSubTasks(legalMoves);

        List<MoveNode> results;
        if (depth <= depthThreshold || tasks.size() <= moveThreshold) {
            results = tasks.stream()
                    .map(MiniMaxRecursiveWithCacheTask::compute)
                    .toList();
        } else {
            results = ForkJoinTask.invokeAll(tasks)
                    .stream()
                    .map(ForkJoinTask::join)
                    .toList();
        }

        var comparator = getMoveNodeComparator();
        // determine the best node and track best move
        var bestPair = IntStream.range(0, results.size())
                .mapToObj(i -> new Object() {
                    final MoveNode node = results.get(i);
                    final String move = legalMoves.get(i);
                })
                .min((a, b) -> comparator.compare(a.node, b.node))
                .orElse(null);

        assert bestPair != null;
        cache.put(position, new EvaluationCacheEntry(bestPair.node.eval(), bestPair.move, depth));

        return bestPair.node;
    }

    private MoveNode getEvaluation() {
        var eval = evaluationStrategy.getEvaluation(chessBoard);
        return new MoveNode(eval, history);
    }

    private Comparator<MoveNode> getMoveNodeComparator() {
        var hasToMax = PlayerColor.WHITE.equals(playerAtTurn);
        var evalComparator = Comparator.comparingDouble(MoveNode::eval);
        // the eval comparison needs to change between min and max, depending on player color
        if (hasToMax) evalComparator = evalComparator.reversed();

        // the history size always needs to be min
        var historyComparator = Comparator.comparingInt((MoveNode n) -> n.history() == null ? Integer.MAX_VALUE : n.history().depth());
        return evalComparator.thenComparing(historyComparator);
    }

    private List<MiniMaxRecursiveWithCacheTask> createSubTasks(List<String> legalMoves) {
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

        return legalMoves.stream()
                .map(move -> {
                    var newPosition = chessBoard.doMove(move);
                    var newHistory = new MoveHistoryNode(move, history);
                    return new MiniMaxRecursiveWithCacheTask(evaluationStrategy, newPosition, depth - 1, nextPlayerColor, newHistory, depthThreshold, moveThreshold, cache);
                }).toList();
    }
}
