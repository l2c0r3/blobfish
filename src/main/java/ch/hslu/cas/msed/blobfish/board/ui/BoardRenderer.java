package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;

public class BoardRenderer {
    private final FieldRenderer fieldRenderer;
    private final BorderFieldRenderer borderFieldRenderer;

    public BoardRenderer(FieldRenderer fieldRenderer, BorderFieldRenderer borderFieldRenderer) {
        this.fieldRenderer = fieldRenderer;
        this.borderFieldRenderer = borderFieldRenderer;
    }

    String render(UiBoard board, PlayerColor perspective) {
        var sb = new StringBuilder();

        int rowStart = (perspective == PlayerColor.BLACK) ? 7 : 0;
        int rowEnd = (perspective == PlayerColor.BLACK) ? -1 : 8;
        int rowStep = (perspective == PlayerColor.BLACK) ? -1 : 1;

        int colStart = (perspective == PlayerColor.BLACK) ? 7 : 0;
        int colEnd = (perspective == PlayerColor.BLACK) ? -1 : 8;
        int colStep = (perspective == PlayerColor.BLACK) ? -1 : 1;

        sb.append(borderFieldRenderer.renderRow(perspective));
        sb.append("\n");
        for (int r = rowStart; r != rowEnd; r += rowStep) {
            var colNumber = 8 - r;
            sb.append(borderFieldRenderer.renderColumnField(colNumber));
            for (int c = colStart; c != colEnd; c += colStep) {
                sb.append(fieldRenderer.render(board.get(r, c)));
            }
            sb.append(borderFieldRenderer.renderColumnField(colNumber));
            sb.append('\n');
        }
        sb.append(borderFieldRenderer.renderRow(perspective));
        sb.append("\n");
        return sb.toString();
    }
}
