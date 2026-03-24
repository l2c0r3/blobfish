package ch.hslu.cas.msed.blobfish.minimax.base;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.base.BotAlgorithm;

public abstract class MiniMaxAlgo extends BotAlgorithm {
    public MiniMaxAlgo(final int calculationDepth, final EvaluationStrategy evaluationStrategy, final PlayerColor ownPlayerColor) {
        super(calculationDepth, evaluationStrategy, ownPlayerColor);
    }
}
