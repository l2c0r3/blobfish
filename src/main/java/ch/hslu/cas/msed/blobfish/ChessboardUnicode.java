package ch.hslu.cas.msed.blobfish;

public class ChessboardUnicode {

    // Background colors
    private static final String LIGHT_BG = "\u001B[48;5;180m";
    private static final String DARK_BG  = "\u001B[48;5;95m";
    private static final String RESET    = "\u001B[0m";

    // Piece foreground colors
    private static final String WHITE_PIECE = "\u001B[38;5;255m"; // white
    private static final String BLACK_PIECE = "\u001B[38;5;0m";  // black

    // Piece foreground colors of the invisible pawns
    private static final String INVISIBLE_WHITE_PIECE = "\u001B[38;5;180m";
    private static final String INVISIBLE_BLACK_PIECE = "\u001B[38;5;95m";

    // Chessboard setup
    private static final String[][] BOARD = {
            {"♜","♞","♝","♛","♚","♝","♞","♜"},
            {"♟","♟","♟","♟","♟","♟","♟","♟"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"♟","♟","♟","♟","♟","♟","♟","♟"},
            {"♜","♞","♝","♛","♚","♝","♞","♜"}
    };

    private static String cell(String piece, boolean lightSquare, boolean lightPiece) {
        String bg = lightSquare ? LIGHT_BG : DARK_BG;

        if(piece.equals(" ")) {
            // Use pawn as invisible filler (foreground same as background)
            String invisiblePawn = lightSquare ? INVISIBLE_WHITE_PIECE + "♟" : INVISIBLE_BLACK_PIECE + "♟";
            return bg + " " + invisiblePawn + " " + RESET;
        } else {
            String fg = lightPiece ? WHITE_PIECE : BLACK_PIECE;
            return bg + fg + " " + piece + " " + RESET;
        }
    }

    public static void main(String[] args) {
        for(int r = 0; r < 8; r++) {
            StringBuilder row = new StringBuilder();
            boolean lightPiece = r > 4;
            for(int c = 0; c < 8; c++) {
                boolean lightSquare = (r + c) % 2 == 0;
                row.append(cell(BOARD[r][c], lightSquare, lightPiece));
            }
            System.out.println(row);
        }
    }
}