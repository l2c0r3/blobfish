package ch.hslu.cas.msed.blobfish.game.providers;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.BotAlgorithm;

public interface BotAlgorithmProvider {

    String getAlgorithmName();

    BotAlgorithm create(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor);
}