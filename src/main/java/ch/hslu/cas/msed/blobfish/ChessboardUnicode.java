package ch.hslu.cas.msed.blobfish;

public class ChessboardUnicode {

    // ANSI escape background colors
// Background colors
    private static final String LIGHT_BG = "\u001B[48;5;137m"; // brown
    private static final String DARK_BG  = "\u001B[48;5;235m"; // dark charcoal
    private static final String RESET    = "\u001B[0m";

    // Piece foreground colors
    private static final String WHITE_PIECE = "\u001B[38;5;137m"; // white
    private static final String BLACK_PIECE = "\u001B[38;5;235m";  // black

    // Chessboard setup
    private static final String[][] BOARD = {
            {"♜","♞","♝","♛","♚","♝","♞","♜"},
            {"♟","♟","♟","♟","♟","♟","♟","♟"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"♙","♙","♙","♙","♙","♙","♙","♙"},
            {"♖","♘","♗","♕","♔","♗","♘","♖"}
    };

    private static String cell(String piece, boolean lightSquare) {
        String bg = lightSquare ? LIGHT_BG : DARK_BG;

        if(piece.equals(" ")) {
            // Use pawn as invisible filler (foreground same as background)
            String invisiblePawn = lightSquare ? WHITE_PIECE + "♟" : BLACK_PIECE + "♟";
            return bg + " " + invisiblePawn + " " + RESET;
        } else {
            return bg + " " + piece + " " + RESET;
        }
    }

    public static void main(String[] args) {
        for(int r = 0; r < 8; r++) {
            StringBuilder row = new StringBuilder();
            for(int c = 0; c < 8; c++) {
                boolean light = (r + c) % 2 == 0;
                row.append(cell(BOARD[r][c], light));
            }
            System.out.println(row);
        }
    }
}