package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;

public class BoardRenderer {

    private final FieldRenderer render;

    public BoardRenderer(FieldRenderer fieldRenderer) {
        this.render = fieldRenderer;
    }

    String render(UiBoard board, PlayerColor perspective) {
        var sb = new StringBuilder();

        int rowStart = (perspective == PlayerColor.BLACK) ? 7 : 0;
        int rowEnd = (perspective == PlayerColor.BLACK) ? -1 : 8;
        int rowStep = (perspective == PlayerColor.BLACK) ? -1 : 1;

        int colStart = (perspective == PlayerColor.BLACK) ? 7 : 0;
        int colEnd = (perspective == PlayerColor.BLACK) ? -1 : 8;
        int colStep = (perspective == PlayerColor.BLACK) ? -1 : 1;

        for (int r = rowStart; r != rowEnd; r += rowStep) {
            for (int c = colStart; c != colEnd; c += colStep) {
                sb.append(render.render(board.get(r, c)));
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
