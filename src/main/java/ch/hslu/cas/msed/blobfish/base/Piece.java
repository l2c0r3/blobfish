package ch.hslu.cas.msed.blobfish.base;

import lombok.Getter;

import javax.annotation.processing.Generated;

@Getter
public enum Piece {

    KING(99, 'k'),
    QUEEN(9, 'q'),
    ROOK(5, 'r'),
    BISHOP(3, 'b'),
    KNIGHT(3, 'n'),
    PAWN(1, 'p');

    private final int points;
    private final char fen; // immer lowercase speichern

    Piece(int points, char fen) {
        this.points = points;
        this.fen = fen;
    }

    public static Piece fromFen(char fenCode) {
        char f = Character.toLowerCase(fenCode);
        for (Piece t : values()) {
            if (t.fen == f) return t;
        }
        throw new IllegalArgumentException("Unknown FEN: " + fenCode);
    }

    public boolean isWhite() {
        return Character.isUpperCase(fen);
    }

    public boolean isBlack() {
        return !isWhite();
    }
}

