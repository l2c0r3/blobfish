package ch.hslu.cas.msed.blobfish.game.player;

import ch.hslu.cas.msed.blobfish.base.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.minimax.cached.MiniMaxAlphaBetaSequentialWithCache;

public class MiniMaxAlphaBetaSequentialWithCacheAlgorithmProvider implements BotAlgorithmProvider {

    private static final String ALGORITHM_NAME = "sequential-ab-cache";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public MiniMaxAlphaBetaSequentialWithCache create(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor) {
        return new MiniMaxAlphaBetaSequentialWithCache(calculationDepth, evaluationStrategy, ownPlayerColor);
    }
}
