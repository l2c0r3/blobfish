package ch.hslu.cas.msed.blobfish.game.screen;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.board.ui.ChessBoardRenderer;
import ch.hslu.cas.msed.blobfish.game.OutputWriter;
import ch.hslu.cas.msed.blobfish.game.exceptions.GameAbortedException;
import ch.hslu.cas.msed.blobfish.game.exceptions.MatchAbortedException;
import ch.hslu.cas.msed.blobfish.player.AbstractPlayer;

public class MatchScreen {
    OutputWriter writer;

    ChessBoard chessboard;
    AbstractPlayer white;
    AbstractPlayer black;
    AbstractPlayer currentPlayer;
    ChessBoardRenderer chessBoardRenderer = new ChessBoardRenderer();


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
        while (!chessboard.isGameOver()) {
            String move;
            try {
                move = currentPlayer.getNextMove(chessboard);
            } catch (final MatchAbortedException e) {
                writer.printlnAndFlush("Match aborted.");
                return;
            }

            // TODO: error handling
            chessboard.doMove(move);
            printPosition(currentPlayer.getPlayerColor());

            currentPlayer = currentPlayer == white ? black : white;
        }

        writer.printlnAndFlush("Game over.");
    }

    private void printPosition(PlayerColor playerColor) {
        var fen = chessboard.getFen();
        var uiStr = chessBoardRenderer.render(fen, playerColor);
        writer.printlnAndFlush(uiStr);
    }
}
