package ch.hslu.cas.msed.blobfish.player.bot.minimax.base;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class MiniMaxRecursiveTask extends RecursiveTask<MoveNode> {
    private final ChessBoard chessBoard;
    private final int depth;
    private final PlayerColor playerAtTurn;
    private final MoveHistoryNode history;
    private final EvalStrategy evalStrategy;
    private final int depthThreshold;
    private final int moveThreshold;

    public MiniMaxRecursiveTask(final EvalStrategy evalStrategy, final ChessBoard chessBoard, final int depth, final PlayerColor playerAtTurn, final MoveHistoryNode history, final int depthThreshold, final int moveThreshold) {
        if (depth < 0) throw new IllegalArgumentException("depth cannot be negative");

        this.evalStrategy = evalStrategy;
        this.chessBoard = chessBoard;
        this.depth = depth;
        this.playerAtTurn = playerAtTurn;
        this.history = history;
        this.depthThreshold = depthThreshold;
        this.moveThreshold = moveThreshold;
    }

    @Override
    protected MoveNode compute() {
        if (depth <= 0 || chessBoard.isGameOver()) {
            return getEvaluation();
        }

        var tasks = createSubTasks();

        if (depth <= depthThreshold || tasks.size() <= moveThreshold) {
            return tasks.stream()
                    .map(MiniMaxRecursiveTask::compute)
                    .min(getMoveNodeComparator())
                    .orElseGet(this::getEvaluation);
        } else {
            return ForkJoinTask.invokeAll(tasks)
                    .stream()
                    .map(ForkJoinTask::join)
                    .min(getMoveNodeComparator())
                    .orElse(null);
        }
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

    private List<MiniMaxRecursiveTask> createSubTasks() {
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

        return chessBoard.legalMoves().stream()
                .map(move -> {
                    var newPosition = chessBoard.doMove(move.toString());
                    var newHistory = new MoveHistoryNode(move.toString(), history);
                    return new MiniMaxRecursiveTask(evalStrategy, newPosition, depth - 1, nextPlayerColor, newHistory, depthThreshold, moveThreshold);
                }).toList();
    }
}
