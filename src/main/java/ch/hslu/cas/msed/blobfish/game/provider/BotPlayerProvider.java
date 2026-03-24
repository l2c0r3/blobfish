package ch.hslu.cas.msed.blobfish.game.provider;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.player.BotAlgorithm;
import ch.hslu.cas.msed.blobfish.player.BotPlayer;

public interface BotPlayerProvider<A extends BotAlgorithm> {

    String getBotName();

    Class<A> algorithmType();

    BotPlayer create(PlayerColor playerColor, A botAlgorithm);
}