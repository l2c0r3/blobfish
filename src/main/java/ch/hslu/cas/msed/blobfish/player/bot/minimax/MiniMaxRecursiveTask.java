package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class MiniMaxRecursiveTask extends RecursiveTask<MoveNode> {
    private final ChessBoard chessBoard;
    private final int depth;
    private final PlayerColor playerAtTurn;
    private final MoveHistoryNode history;
    private final EvalStrategy evalStrategy;

    public MiniMaxRecursiveTask(EvalStrategy evalStrategy, ChessBoard chessBoard, int depth, PlayerColor playerAtTurn, MoveHistoryNode history) {
        if (depth < 0) throw new IllegalArgumentException("depth cannot be negative");

        this.evalStrategy = evalStrategy;
        this.chessBoard = chessBoard;
        this.depth = depth;
        this.playerAtTurn = playerAtTurn;
        this.history = history;
    }

    @Override
    protected MoveNode compute() {
        if (depth <= 0 || chessBoard.isGameOver()) {
            var eval = evalStrategy.getEvaluation(chessBoard.getFen());
            return new MoveNode(eval, history);
        }

        var nodeComparator = getMoveNodeComparator();

        return ForkJoinTask.invokeAll(createSubTasks())
                .stream()
                .map(ForkJoinTask::join)
                .min(nodeComparator)
                .orElse(null);
    }

    private Comparator<MoveNode> getMoveNodeComparator() {
        var hasToMax = PlayerColor.WHITE.equals(playerAtTurn);
        var evalComparator = Comparator.comparingDouble(MoveNode::eval);
        // the eval comparison needs to change between min and max, depending on player color
        if (hasToMax) evalComparator = evalComparator.reversed();

        // the history size always needs to be min
        var historyComparator = Comparator.comparingInt((MoveNode n) -> n.history().depth());
        return evalComparator.thenComparing(historyComparator);
    }

    private Collection<MiniMaxRecursiveTask> createSubTasks() {
        var nextPlayerColor = PlayerColor.WHITE.equals(playerAtTurn) ? PlayerColor.BLACK : PlayerColor.WHITE;

        return chessBoard.legalMoves().stream()
                .map(move -> {
                    var newPosition = chessBoard.doMove(move.toString());
                    var newHistory = new MoveHistoryNode(move.toString(), history);
                    return new MiniMaxRecursiveTask(evalStrategy, newPosition, depth - 1, nextPlayerColor, newHistory);
                }).toList();
    }
}
