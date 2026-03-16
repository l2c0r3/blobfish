package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.player.bot.BotPlayer;
import ch.hslu.cas.msed.blobfish.player.exceptions.InvalidMoveException;

public class MiniMaxBotPlayer extends BotPlayer {

    public MiniMaxBotPlayer(final PlayerColor playerColor, final MiniMaxAlgo botAlgorithm) {
        super(playerColor, botAlgorithm);
    }

    @Override
    public String getNextMove(final ChessBoard board) throws InvalidMoveException {
        var result = botAlgorithm.getNextBestMove(board);
        return result != null ? result.move() : null;
    }

}
