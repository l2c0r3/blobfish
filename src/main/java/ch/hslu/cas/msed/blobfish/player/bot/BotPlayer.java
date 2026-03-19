package ch.hslu.cas.msed.blobfish.player.bot;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.player.AbstractPlayer;

public abstract class BotPlayer extends AbstractPlayer {

    protected final BotAlgorithm botAlgorithm;

    public BotPlayer(final PlayerColor playerColor, final BotAlgorithm botAlgorithm) {
        super(playerColor);
        this.botAlgorithm = botAlgorithm;
    }

}
