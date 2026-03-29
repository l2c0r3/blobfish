package ch.hslu.cas.msed.blobfish.game.player;

import ch.hslu.cas.msed.blobfish.base.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.minimax.cached.MiniMaxParallelWithCache;

public class MiniMaxParallelWithCacheAlgorithmProvider implements BotAlgorithmProvider {
    private static final String ALGORITHM_NAME = "parallel-cache";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public MiniMaxParallelWithCache create(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor) {
        return new MiniMaxParallelWithCache(calculationDepth, evaluationStrategy, ownPlayerColor);
    }
}
