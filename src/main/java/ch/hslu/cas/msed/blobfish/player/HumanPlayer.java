package ch.hslu.cas.msed.blobfish.player;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;

public class HumanPlayer extends AbstractPlayer {

    public HumanPlayer(PlayerColor playerColor) {
        super(playerColor);
    }

    @Override
    public String getNextMove(ChessBoard board) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
