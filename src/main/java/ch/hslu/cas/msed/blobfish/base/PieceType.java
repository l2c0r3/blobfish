package ch.hslu.cas.msed.blobfish.base;

import lombok.Getter;

@Getter
public enum PieceType {

    KING(99, 'k'),
    QUEEN(9, 'q'),
    ROOK(5, 'r'),
    BISHOP(3, 'b'),
    KNIGHT(3, 'n'),
    PAWN(1, 'p');

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

