package ch.hslu.cas.msed.blobfish.game.player;

import ch.hslu.cas.msed.blobfish.base.BotAlgorithm;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;

public abstract class BotPlayer extends AbstractPlayer {

    protected final BotAlgorithm botAlgorithm;

    public BotPlayer(final PlayerColor playerColor, final BotAlgorithm botAlgorithm) {
        super(playerColor);
        this.botAlgorithm = botAlgorithm;
    }

}
