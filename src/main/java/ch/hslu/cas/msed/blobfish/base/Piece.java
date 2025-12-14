package ch.hslu.cas.msed.blobfish.base;

public record Piece(PieceType type, PlayerColor color) {

    public Piece(Character fen) {
        var pieceType = PieceType.fromFen(fen);
        var color = Character.isUpperCase(fen) ? PlayerColor.WHITE : PlayerColor.BLACK;
        this(pieceType, color);
    }

    public Character fen() {
        char character = this.type.getFen();
        return switch (this.color) {
            case WHITE -> Character.toUpperCase(character);
            case BLACK -> Character.toLowerCase(character);
        };
    }
}
