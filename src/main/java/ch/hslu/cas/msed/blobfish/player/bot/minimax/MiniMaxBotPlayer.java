package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.BotPlayer;
import ch.hslu.cas.msed.blobfish.player.bot.MiniMaxAlgo;
import ch.hslu.cas.msed.blobfish.player.exceptions.InvalidMoveException;

public class MiniMaxBotPlayer extends BotPlayer {

    private final MiniMaxAlgo miniMaxAlgo;

    public MiniMaxBotPlayer(PlayerColor playerColor, MiniMaxAlgo miniMaxAlgo) {
        super(playerColor);
        this.miniMaxAlgo = miniMaxAlgo;
    }

    @Override
    public String getNextMove(ChessBoard board) throws InvalidMoveException {
        return miniMaxAlgo.getBestNextMove(board);
    }

}
