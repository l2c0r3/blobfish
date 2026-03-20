package ch.hslu.cas.msed.blobfish.player.bot.minimax.base;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.BotAlgorithm;

public abstract class MiniMaxAlgo extends BotAlgorithm {
    public MiniMaxAlgo(final int calculationDepth, final EvalStrategy evalStrategy, final PlayerColor ownPlayerColor) {
        super(calculationDepth, evalStrategy, ownPlayerColor);
    }
}
