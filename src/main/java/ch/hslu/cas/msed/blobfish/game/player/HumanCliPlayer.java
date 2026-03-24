package ch.hslu.cas.msed.blobfish.game.player;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.game.InputReader;
import ch.hslu.cas.msed.blobfish.base.exception.InvalidMoveException;
import ch.hslu.cas.msed.blobfish.player.AbstractPlayer;

public class HumanCliPlayer extends AbstractPlayer {

    private final InputReader reader;

    public HumanCliPlayer(final PlayerColor playerColor, final InputReader reader) {
        super(playerColor);
        this.reader = reader;
    }

    @Override
    public String getNextMove(final ChessBoard board) throws InvalidMoveException {
        var potentialMove = reader.readLine("Your move:");

        if (board.isMoveLegal(potentialMove)) {
            return potentialMove;
        } else {
            throw new InvalidMoveException(potentialMove, "The given move is not valid.");
        }
    }
}
