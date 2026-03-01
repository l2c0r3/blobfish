package ch.hslu.cas.msed.blobfish.base;

import lombok.Getter;

@Getter
public enum PieceType {

    // values are multiplied by 100, to allow positional fine-tuning
    KING(9900, 'k'),
    QUEEN(900, 'q'),
    ROOK(500, 'r'),
    BISHOP(300, 'b'),
    KNIGHT(300, 'n'),
    PAWN(100, 'p');

    private final int points;
    private final char fen;

    PieceType(int points, char fen) {
        this.points = points;
        this.fen = fen;
    }

    public static PieceType fromFen(final char fenCode) {
        if (!Character.isAlphabetic(fenCode)) {
            throw new IllegalArgumentException("FEN has to be alphabetic");
        }
        char f = Character.toLowerCase(fenCode);
        for (PieceType t : values()) {
            if (t.fen == f) return t;
        }
        throw new IllegalArgumentException("Unknown FEN: " + fenCode);
    }
}

