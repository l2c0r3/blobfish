package ch.hslu.cas.msed.blobfish.game.player;

import ch.hslu.cas.msed.blobfish.base.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.minimax.MiniMaxAlphaBetaSequential;

public class MiniMaxAlphaBetaSequentialAlgorithmProvider implements BotAlgorithmProvider {

    private static final String ALGORITHM_NAME = "sequential-ab";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public MiniMaxAlphaBetaSequential create(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor) {
        return new MiniMaxAlphaBetaSequential(calculationDepth, evaluationStrategy, ownPlayerColor);
    }
}
