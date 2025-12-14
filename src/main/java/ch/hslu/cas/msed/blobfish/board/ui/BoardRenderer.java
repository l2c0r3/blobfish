package ch.hslu.cas.msed.blobfish.board.ui;

public class BoardRenderer {

    private final FieldRenderer render;

    public BoardRenderer(FieldRenderer fieldRenderer) {
        this.render = fieldRenderer;
    }

    String render(UiBoard board) {
        var uiString = new StringBuilder();
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            for (int cellIndex = 0; cellIndex < 8; cellIndex++) {
                var field = board.get(rowIndex, cellIndex);
                var fieldStr = render.render(field);
                uiString.append(fieldStr);
            }
            uiString.append("\n");
        }
        return uiString.toString();
    }
}
