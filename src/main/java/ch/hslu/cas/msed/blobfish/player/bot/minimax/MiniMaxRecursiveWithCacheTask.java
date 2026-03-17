package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class MiniMaxRecursiveWithCacheTask extends RecursiveTask<MoveNode> {
    private final ChessBoard chessBoard;
    private final int depth;
    private final PlayerColor playerAtTurn;
    private final MoveHistoryNode history;
    private final EvalStrategy evalStrategy;
    private final EvaluationCache cache;

    public MiniMaxRecursiveWithCacheTask(final EvalStrategy evalStrategy, final ChessBoard chessBoard, final int depth, final PlayerColor playerAtTurn, final MoveHistoryNode history, final EvaluationCache cache) {
        if (depth < 0) throw new IllegalArgumentException("depth cannot be negative");

        this.evalStrategy = evalStrategy;
        this.chessBoard = chessBoard;
        this.depth = depth;
        this.playerAtTurn = playerAtTurn;
        this.history = history;
        this.cache = cache;
    }

    @Override
    protected MoveNode compute() {
        // Check cache first
        var position = chessBoard.getFen();
        var cached = cache.get(position, depth);
        if (cached != null) {
            return new MoveNode(cached.value(), history);
        }

        if (depth <= 0 || chessBoard.isGameOver()) {
            var moveNode = getEvaluation();
            cache.put(position, new EvaluationCacheEntry(moveNode.eval(), depth));
            return moveNode;
        }

        var tasks = createSubTasks();

        return switch (tasks.size()) {
            case 0 -> getEvaluation();
            case 1 -> tasks.getFirst().compute();
            default -> ForkJoinTask.invokeAll(tasks)
                    .stream()
                    .map(ForkJoinTask::join)
                    .min(getMoveNodeComparator())
                    .map(moveNode -> {
                        cache.put(position, new EvaluationCacheEntry(moveNode.eval(), depth));
                        return moveNode;
                    })
                    .orElse(null);
        };
    }

    private MoveNode getEvaluation() {
        var eval = evalStrategy.getEvaluation(chessBoard);
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

    private List<MiniMaxRecursiveWithCacheTask> createSubTasks() {
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

        return chessBoard.legalMoves().stream()
                .map(move -> {
                    var newPosition = chessBoard.doMove(move.toString());
                    var newHistory = new MoveHistoryNode(move.toString(), history);
                    return new MiniMaxRecursiveWithCacheTask(evalStrategy, newPosition, depth - 1, nextPlayerColor, newHistory, cache);
                }).toList();
    }
}
