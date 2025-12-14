package ch.hslu.cas.msed.blobfish.board.ui;

public class ChessBoardRenderer {

    private final FenBoardParser parser;
    private final BoardRenderer boardRenderer;

    public ChessBoardRenderer() {
        this.parser = new FenBoardParser();

        var fieldRenderer = new AnsiiFieldRenderer();
        this.boardRenderer = new BoardRenderer(fieldRenderer);
    }

    public String render(String fen) {
        var board = parser.parse(fen);
        return boardRenderer.render(board);
    }
}
