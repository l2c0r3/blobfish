package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PieceType;

class AnsiiFieldRenderer implements FieldRenderer {

    // Background colors
    public static final String LIGHT_BG = "\u001B[48;5;180m";
    public static final String DARK_BG = "\u001B[48;5;95m";
    public static final String RESET = "\u001B[0m";

    // Piece foreground colors
    public static final String WHITE_PIECE = "\u001B[38;5;255m";
    public static final String BLACK_PIECE = "\u001B[38;5;0m";

    // Piece foreground colors of the invisible pawns
    private static final String INVISIBLE_LIGHT_PIECE = "\u001B[38;5;180m";
    private static final String INVISIBLE_DARK_PIECE = "\u001B[38;5;95m";

    @Override
    public String render(UiField uiField) {
        if (uiField.piece() == null) {
            return renderEmptyField(uiField.squareColor());
        }

        return renderPiece(uiField.piece(), uiField.squareColor());
    }

    private static String renderEmptyField(SquareColor color) {
        var backgroundColor = getBackgroundColor(color);
        var placeholderCharacter = getCharacterOfPieceType(PieceType.PAWN);
        var invisiblePawn = switch (color) {
            case LIGHT -> INVISIBLE_LIGHT_PIECE;
            case DARK -> INVISIBLE_DARK_PIECE;
        };
        return backgroundColor + " " + invisiblePawn + placeholderCharacter + " " + RESET;
    }

    private static String renderPiece(Piece piece, SquareColor squareColor) {
        var backgroundColor = getBackgroundColor(squareColor);
        var pieceCharacter = getCharacterOfPieceType(piece.type());
        var foregroundColor = switch (piece.color()) {
            case WHITE -> WHITE_PIECE;
            case BLACK -> BLACK_PIECE;
        };

        return backgroundColor + foregroundColor + " " + pieceCharacter + " " + RESET;
    }

    private static String getBackgroundColor(SquareColor squareColor) {
        return switch (squareColor) {
            case LIGHT -> LIGHT_BG;
            case DARK -> DARK_BG;
        };
    }

    private static Character getCharacterOfPieceType(PieceType pieceType) {
        return switch (pieceType) {
            case KING -> '♚';
            case QUEEN -> '♛';
            case ROOK -> '♜';
            case BISHOP -> '♝';
            case KNIGHT -> '♞';
            case PAWN -> '♟';
        };
    }
}
