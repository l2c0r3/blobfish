package ch.hslu.cas.msed.blobfish.game.player;

import ch.hslu.cas.msed.blobfish.base.BotAlgorithm;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;

public interface BotPlayerProvider<A extends BotAlgorithm> {

    String getBotName();

    Class<A> algorithmType();

    BotPlayer create(PlayerColor playerColor, A botAlgorithm);
}