package ch.hslu.cas.msed.blobfish.game.player;

import ch.hslu.cas.msed.blobfish.base.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.minimax.cached.MiniMaxSequentialWithCache;

public class MiniMaxSequentialWithCacheAlgorithmProvider implements BotAlgorithmProvider {

    private static final String ALGORITHM_NAME = "sequential-cache";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public MiniMaxSequentialWithCache create(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor) {
        return new MiniMaxSequentialWithCache(calculationDepth, evaluationStrategy, ownPlayerColor);
    }
}
