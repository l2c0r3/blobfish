package ch.hslu.cas.msed.blobfish.game.provider;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.minimax.MiniMaxSequential;

public class MiniMaxSequentialAlgorithmProvider implements BotAlgorithmProvider {

    private static final String ALGORITHM_NAME = "sequential";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public MiniMaxSequential create(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor) {
        return new MiniMaxSequential(calculationDepth, evaluationStrategy, ownPlayerColor);
    }
}
