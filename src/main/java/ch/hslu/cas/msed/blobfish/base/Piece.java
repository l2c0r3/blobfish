package ch.hslu.cas.msed.blobfish.base;

public record Piece(PieceType type, Color color) {

    public Piece(Character fen) {
        var pieceType = PieceType.fromFen(fen);
        var color = Character.isUpperCase(fen) ? Color.WHITE : Color.BLACK;
        this(pieceType, color);
    }

}
