package ch.hslu.cas.msed.blobfish.game.providers;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.evaluation.EvalStrategy;
import ch.hslu.cas.msed.blobfish.minimax.MiniMaxParallel;

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
