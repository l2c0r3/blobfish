package ch.hslu.cas.msed.blobfish.game.player;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.player.BotAlgorithm;

public interface BotAlgorithmProvider {

    String getAlgorithmName();

    BotAlgorithm create(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor);
}