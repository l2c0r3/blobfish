package ch.hslu.cas.msed.blobfish.player;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.game.exception.GameAbortedException;
import ch.hslu.cas.msed.blobfish.game.exception.MatchAbortedException;
import lombok.Getter;

@Getter
public abstract class AbstractPlayer {

    private final PlayerColor playerColor;

    public AbstractPlayer(final PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    /**
     * @param board The chessboard
     * @return returns next move in SAN annotation
     */
    public abstract String getNextMove(final ChessBoard board) throws MatchAbortedException, GameAbortedException;

}
