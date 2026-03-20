package ch.hslu.cas.msed.blobfish.game.provider;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.evaluation.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.BotAlgorithm;

public interface BotAlgorithmProvider {

    String getAlgorithmName();

    BotAlgorithm create(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor);
}