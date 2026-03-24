package ch.hslu.cas.msed.blobfish.minimax.base;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.player.BotPlayer;
import ch.hslu.cas.msed.blobfish.base.exception.InvalidMoveException;

public class MiniMaxBotPlayer extends BotPlayer {

    public MiniMaxBotPlayer(final PlayerColor playerColor, final MiniMaxAlgo botAlgorithm) {
        super(playerColor, botAlgorithm);
    }

    @Override
    public String getNextMove(final ChessBoard board) throws InvalidMoveException {
        var result = botAlgorithm.getBestPath(board);
        return result != null && result.move() != null && !result.move().isEmpty() ? result.move().getFirst() : null;
    }

}
