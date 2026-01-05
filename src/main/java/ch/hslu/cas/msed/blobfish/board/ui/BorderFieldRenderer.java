package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;

public interface BorderFieldRenderer {
    String renderRow(PlayerColor perspective);

    String renderColumnField(int colNumber);
}
