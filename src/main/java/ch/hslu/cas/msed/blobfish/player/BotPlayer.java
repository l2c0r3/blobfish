package ch.hslu.cas.msed.blobfish.player;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;

public abstract class BotPlayer extends AbstractPlayer {

    protected final BotAlgorithm botAlgorithm;

    public BotPlayer(final PlayerColor playerColor, final BotAlgorithm botAlgorithm) {
        super(playerColor);
        this.botAlgorithm = botAlgorithm;
    }

}
