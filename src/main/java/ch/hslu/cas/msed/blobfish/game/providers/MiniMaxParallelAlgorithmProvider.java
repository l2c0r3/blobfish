package ch.hslu.cas.msed.blobfish.game.providers;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxParallel;

public class MiniMaxParallelAlgorithmProvider implements BotAlgorithmProvider {
    private static final String ALGORITHM_NAME = "parallel";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public MiniMaxParallel create(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        return new MiniMaxParallel(calculationDepth, evalStrategy, ownPlayerColor);
    }
}
