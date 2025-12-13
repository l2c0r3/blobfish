package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.Color;
import ch.hslu.cas.msed.blobfish.base.Piece;

import java.util.Map;

record UiFieldPrinter(Piece piece, Color pieceColor, Color squareColor) {

    // Background colors
    private static final String LIGHT_BG = "\u001B[48;5;180m";
    private static final String DARK_BG = "\u001B[48;5;95m";
    private static final String RESET = "\u001B[0m";

    // Piece foreground colors
    private static final String WHITE_PIECE = "\u001B[38;5;255m"; // white
    private static final String BLACK_PIECE = "\u001B[38;5;0m";  // black

    // Piece foreground colors of the invisible pawns
    private static final String INVISIBLE_WHITE_PIECE = "\u001B[38;5;180m";
    private static final String INVISIBLE_BLACK_PIECE = "\u001B[38;5;95m";

    private static final Map<Piece, Character> CHARACTER_MAP = Map.ofEntries(
            Map.entry(Piece.KING, '♚'),
            Map.entry(Piece.QUEEN, '♛'),
            Map.entry(Piece.ROOK, '♜'),
            Map.entry(Piece.BISHOP, '♝'),
            Map.entry(Piece.KNIGHT, '♞'),
            Map.entry(Piece.PAWN, '♟')
    );

    public UiFieldPrinter(char fenCode, Color squareColor) {
        Piece piece = null;
        Color pieceColor = null;
        if (!Character.isDigit(fenCode)) {
            piece = Piece.fromFen(fenCode);
            pieceColor = piece.isBlack() ? Color.BLACK : Color.WHITE;
        }
        this(piece, pieceColor, squareColor);
    }

    public String print() {
        if (piece == null) {
            return getEmptyField();
        }
        return getPieceField();
    }

    private String getEmptyField() {
        return "";
    }

    private String getPieceField() {
        return CHARACTER_MAP.get(piece).toString();
    }
}
