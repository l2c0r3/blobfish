package ch.hslu.cas.msed.blobfish.game.providers;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxSequential;

public class MiniMaxSequentialAlgorithmProvider implements BotAlgorithmProvider {
    private static final String ALGORITHM_NAME = "sequential";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public MiniMaxSequential create(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        return new MiniMaxSequential(calculationDepth, evalStrategy, ownPlayerColor);
    }
}
