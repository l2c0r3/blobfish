package ch.hslu.cas.msed.blobfish.base;

public record Piece(PieceType type, PlayerColor color) {

    public Piece(Character fen) {
        var pieceType = PieceType.fromFen(fen);
        var color = Character.isUpperCase(fen) ? PlayerColor.WHITE : PlayerColor.BLACK;
        this(pieceType, color);
    }

}
