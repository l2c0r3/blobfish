package ch.hslu.cas.msed.blobfish.game.providers;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.player.bot.BotAlgorithm;
import ch.hslu.cas.msed.blobfish.player.bot.BotPlayer;

public interface BotPlayerProvider<A extends BotAlgorithm> {

    String getBotName();

    Class<A> algorithmType();

    BotPlayer create(PlayerColor playerColor, A botAlgorithm);
}