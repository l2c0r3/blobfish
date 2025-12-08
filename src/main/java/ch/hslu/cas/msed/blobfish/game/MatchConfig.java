package ch.hslu.cas.msed.blobfish.game;

import ch.hslu.cas.msed.blobfish.player.AbstractPlayer;

public record MatchConfig(AbstractPlayer white, AbstractPlayer black) {
}
