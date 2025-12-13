package ch.hslu.cas.msed.blobfish.player;

import ch.hslu.cas.msed.blobfish.base.Color;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;

public class BotPlayer extends AbstractPlayer {

    public BotPlayer(Color color) {
        super(color);
    }

    @Override
    public String getNextMove(ChessBoard board) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
