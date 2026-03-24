package ch.hslu.cas.msed.blobfish.game.player;

import ch.hslu.cas.msed.blobfish.base.BotAlgorithm;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.EvaluationStrategy;

public interface BotAlgorithmProvider {

    String getAlgorithmName();

    BotAlgorithm create(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor);
}