package ch.hslu.cas.msed.blobfish.player;

import ch.hslu.cas.msed.blobfish.base.Color;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.game.exceptions.GameAbortedException;
import ch.hslu.cas.msed.blobfish.game.exceptions.MatchAbortedException;

public abstract class AbstractPlayer {

    private final Color color;

    public AbstractPlayer(Color color) {
        this.color = color;
    }

    public Color getPlayerColor() {
        return color;
    }

    /**
     * @param board The chessboard
     * @return returns next move in SAN annotation
     */
    public abstract String getNextMove(ChessBoard board) throws MatchAbortedException, GameAbortedException;

}
