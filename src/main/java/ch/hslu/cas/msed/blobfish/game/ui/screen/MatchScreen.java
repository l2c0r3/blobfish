package ch.hslu.cas.msed.blobfish.game.ui.screen;

import ch.hslu.cas.msed.blobfish.base.ThreadLocalUtils;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.game.ui.board.ChessBoardRenderer;
import ch.hslu.cas.msed.blobfish.game.OutputWriter;
import ch.hslu.cas.msed.blobfish.game.exception.GameAbortedException;
import ch.hslu.cas.msed.blobfish.game.exception.MatchAbortedException;
import ch.hslu.cas.msed.blobfish.game.player.AbstractPlayer;
import ch.hslu.cas.msed.blobfish.game.player.HumanCliPlayer;
import ch.hslu.cas.msed.blobfish.game.player.BotPlayer;
import ch.hslu.cas.msed.blobfish.base.exception.InvalidMoveException;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class MatchScreen {
    private final OutputWriter writer;

    private final AbstractPlayer white;
    private final AbstractPlayer black;
    private final ChessBoardRenderer chessBoardRenderer = new ChessBoardRenderer();
    private ChessBoard chessboard;
    private AbstractPlayer currentPlayer;


    public MatchScreen(
            final AbstractPlayer white,
            final AbstractPlayer black,
            final ChessBoard chessboard,
            final OutputWriter writer
    ) {
        this.writer = writer;
        this.chessboard = chessboard;
        this.white = white;
        this.black = black;

        currentPlayer = white;
    }


    public void start() throws GameAbortedException {
        resetInternalBoard();
        while (!chessboard.isGameOver()) {
            if (shouldDisplayBoard(currentPlayer)) {
                printPosition(currentPlayer.getPlayerColor());
            }

            var move = getNextMove();
            this.chessboard = chessboard.doMove(move);
            currentPlayer = currentPlayer == white ? black : white;
        }

        writer.printlnAndFlush("Game over.");
    }

    private String getNextMove() throws GameAbortedException, MatchAbortedException {
        String move = null;

        do {
            try {
                move = currentPlayer.getNextMove(chessboard);
            } catch (final InvalidMoveException e) {
                if ("quit".equalsIgnoreCase(e.getOriginalMove())) {
                    throw new MatchAbortedException("Match aborted by user.");
                } else {
                    writer.printlnAndFlush(e.getOriginalMove() + " is not a valid move.");
                }
            }
        } while (move == null);
        return move;
    }

    private boolean shouldDisplayBoard(AbstractPlayer currentPlayer) {
        if (white instanceof HumanCliPlayer && black instanceof HumanCliPlayer) {
            return true;
        } else if (white instanceof BotPlayer && black instanceof BotPlayer) {
            return true;
        } else return currentPlayer instanceof HumanCliPlayer;
    }

    private void printPosition(PlayerColor playerColor) {
        var fen = chessboard.getFen();
        var uiStr = chessBoardRenderer.render(fen, playerColor);
        writer.printlnAndFlush(uiStr);
    }

    private void resetInternalBoard() {
        // for some reason (probably performance), the MoveList class saves the board in a ThreadLocal
        // variable and reuses it in new instantiations. This is supposed to reset that variable before
        // instantiating the class, to get a clean slate.
        try {
            ThreadLocalUtils.resetStaticThreadLocal(MoveList.class, "boardHolder");
        } catch (ReflectiveOperationException _) {
        }
    }
}
