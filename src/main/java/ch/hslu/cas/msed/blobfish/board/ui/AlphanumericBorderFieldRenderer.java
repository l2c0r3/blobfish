package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import org.apache.commons.lang3.StringUtils;

public class AlphanumericBorderFieldRenderer implements BorderFieldRenderer {
    public static final String BG_COLOR = "\u001B[48;5;0m";
    public static final String FG_COLOR = "\u001B[38;5;255m";
    public static final String RESET = "\u001B[0m";

    public static final String PLACEHOLDER_COLOR = "\u001B[38;5;0m";
    public static final char PLACEHOLDER_CHARACTER = '0';

    // the uneven spaces are the only way I could at least kind of align the letters to the fields
    public static final String ROW_STRING_WHITE = " A   B  C   D   E   F  G   H ";

    @Override
    public String renderRow(PlayerColor perspective) {
        return renderEmptyField() + renderRowString(perspective) + renderEmptyField();
    }

    @Override
    public String renderColumnField(int colNumber) {
        return BG_COLOR + FG_COLOR + " " + colNumber + " " + RESET;
    }

    private static String renderEmptyField() {
        return BG_COLOR + PLACEHOLDER_COLOR + " " + PLACEHOLDER_CHARACTER + " " + RESET;
    }

    private static String renderRowString(PlayerColor perspective) {
        var rowString = switch (perspective) {
            case BLACK -> StringUtils.reverse(ROW_STRING_WHITE);
            case WHITE -> ROW_STRING_WHITE;
        };

        return BG_COLOR + FG_COLOR + rowString + RESET;
    }
}
