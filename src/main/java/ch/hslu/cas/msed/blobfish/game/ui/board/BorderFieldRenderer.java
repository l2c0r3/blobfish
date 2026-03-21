package ch.hslu.cas.msed.blobfish.game.ui.board;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;

public interface BorderFieldRenderer {
    String renderRow(PlayerColor perspective);

    String renderColumnField(int colNumber);
}
