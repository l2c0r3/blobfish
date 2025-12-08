package ch.hslu.cas.msed.blobfish.player;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.game.exceptions.GameAbortedException;
import ch.hslu.cas.msed.blobfish.game.exceptions.MatchAbortedException;

public abstract class AbstractPlayer {

    private final PlayerColor playerColor;

    public AbstractPlayer(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    /**
     * @param board The chessboard
     * @return returns next move in SAN annotation
     */
    public abstract String getNextMove(ChessBoard board) throws MatchAbortedException, GameAbortedException;

}
