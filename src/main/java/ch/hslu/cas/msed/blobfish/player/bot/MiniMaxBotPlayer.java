package ch.hslu.cas.msed.blobfish.player.bot;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.exceptions.InvalidMoveException;

public class MiniMaxBotPlayer extends BotPlayer {

    private final MiniMax miniMax;

    public MiniMaxBotPlayer(PlayerColor playerColor, EvalStrategy evalStrategy, int calculationDepth) {
        super(playerColor);
        this.miniMax = new MiniMax(calculationDepth, evalStrategy, playerColor);
    }

    @Override
    public String getNextMove(ChessBoard board) throws InvalidMoveException {
        return miniMax.getBestNextMove(board);
    }

}
