package ch.hslu.cas.msed.blobfish.player;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.game.InputReader;
import ch.hslu.cas.msed.blobfish.player.exceptions.InvalidMoveException;

public class HumanPlayer extends AbstractPlayer {

    InputReader reader;

    public HumanPlayer(PlayerColor playerColor, InputReader reader) {
        super(playerColor);
        this.reader = reader;
    }

    @Override
    public String getNextMove(ChessBoard board) throws InvalidMoveException {
        var potentialMove = reader.readLine("Your move:");

        if (board.isMoveLegal(potentialMove)) {
            return potentialMove;
        } else {
            throw new InvalidMoveException(potentialMove, "The given move is not valid.");
        }
    }
}
