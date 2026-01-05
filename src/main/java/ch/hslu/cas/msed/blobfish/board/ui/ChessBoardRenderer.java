package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;

public class ChessBoardRenderer {

    private final FenUiBoardParser parser;
    private final BoardRenderer boardRenderer;

    public ChessBoardRenderer() {
        this.parser = new FenUiBoardParser();

        var fieldRenderer = new AnsiiFieldRenderer();
        var borderRenderer = new AlphanumericBorderFieldRenderer();
        this.boardRenderer = new BoardRenderer(fieldRenderer, borderRenderer);
    }

    public String render(String fen, PlayerColor playerPerspective) {
        var board = parser.parse(fen);
        return boardRenderer.render(board, playerPerspective);
    }
}
