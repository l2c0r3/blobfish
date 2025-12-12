package ch.hslu.cas.msed.blobfish.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static ch.hslu.cas.msed.blobfish.board.ChessBoardAsciiPrinter.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChessBoardAsciiPrinterTest {


    public static final char WHITE_KING = '♚';
    public static final char WHITE_QUEEN = '♛';
    public static final char WHITE_ROOK = '♜';
    public static final char WHITE_BISHOP = '♝';
    public static final char WHITE_KNIGHT = '♞';
    public static final char WHITE_PAWN = '♟';
    public static final char WHITE_SQUARE = '■';

    public static final char BLACK_KING = '♔';
    public static final char BLACK_QUEEN = '♕';
    public static final char BLACK_ROOK = '♖';
    public static final char BLACK_BISHOP = '♗';
    public static final char BLACK_KNIGHT = '♘';
    public static final char BLACK_PAWN = '♙';
    public static final char BLACK_SQUARE = '□';


    @Test
    void displayBoardAscii_nullPosition() {
        // Arrange
        var position = "8/8/8/8/8/8/8/8";

        // Act
        var result = ChessBoardAsciiPrinter.displayBoardAscii(position);

        // Assert
        var expected = new StringBuilder()
                .append("" + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + "\n")
                .append("" + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + "\n")
                .append("" + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + "\n")
                .append("" + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + "\n")
                .append("" + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + "\n")
                .append("" + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + "\n")
                .append("" + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + "\n")
                .append("" + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE)
                .toString();
        assertEquals(expected, result);
    }

    @Test
    void displayBoardAscii_startPosition() {
        // Arrange
        var position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        // Act
        var result = ChessBoardAsciiPrinter.displayBoardAscii(position);

        // Assert
        var expected = new StringBuilder()
                .append("" + BLACK_ROOK + BLACK_KNIGHT + BLACK_BISHOP + BLACK_QUEEN + BLACK_KING + BLACK_BISHOP + BLACK_KNIGHT + BLACK_ROOK + "\n")
                .append("" + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + BLACK_PAWN + "\n")
                .append("" + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + "\n")
                .append("" + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + "\n")
                .append("" + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + "\n")
                .append("" + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + "\n")
                .append("" + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + WHITE_PAWN + "\n")
                .append("" + WHITE_ROOK + WHITE_KNIGHT + WHITE_BISHOP + WHITE_QUEEN + WHITE_KING + WHITE_BISHOP + WHITE_KNIGHT + WHITE_ROOK)
                .toString();
        assertEquals(expected, result);
    }

    @Test
    void displayBoardAscii_randomPos1() {
        // Arrange
        var position = "r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1";

        // Act
        var result = ChessBoardAsciiPrinter.displayBoardAscii(position);

        // Assert
        var expected = new StringBuilder()
                .append("" + BLACK_ROOK + BLACK_SQUARE + BLACK_BISHOP + BLACK_KING + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_ROOK + "\n")
                .append("" + BLACK_PAWN + WHITE_SQUARE + BLACK_SQUARE + BLACK_PAWN + WHITE_BISHOP + BLACK_PAWN + WHITE_KNIGHT + BLACK_PAWN + "\n")
                .append("" + BLACK_KNIGHT + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_KNIGHT + WHITE_SQUARE + BLACK_SQUARE + "\n")
                .append("" + BLACK_SQUARE + BLACK_PAWN + BLACK_SQUARE + WHITE_KNIGHT + WHITE_PAWN + WHITE_SQUARE + BLACK_SQUARE + WHITE_PAWN + "\n")
                .append("" + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_PAWN + BLACK_SQUARE + "\n")
                .append("" + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_PAWN + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + "\n")
                .append("" + WHITE_PAWN + BLACK_SQUARE + WHITE_PAWN + BLACK_SQUARE + WHITE_KING + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + "\n")
                .append("" + BLACK_QUEEN + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_SQUARE + WHITE_SQUARE + BLACK_BISHOP + WHITE_SQUARE)
                .toString();
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1",             // invalid character (first char)
            "9/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1",                  // too high number
            "6pppp/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1",              // mix of pieces and number is too high
            "1pp/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1",                // mix of pieces and number is too little
            "p/p2pBpNp/8/8/8/8/PPPPPPPP/RNBQKBNR",                         // too little pieces
            "ppppppppp/p2pBpNp/8/8/8/8/PPPPPPPP/RNBQKBNR",                 // too many pieces
            "pppppppp/p2pBpNp/8/8/8/8/PPPPPPPP",                           // too little blocks
            "ppppppppp/p2pBpNp/8/8/8/8/PPPPPPPP/RNBQKBNR/ppppppppp/",      // too much blocks
    })
    @NullAndEmptySource
    void displayBoardAscii_invalidFenString_throwsException(String fenString) {
        // Act & assert
        assertThrows(IllegalArgumentException.class, () -> ChessBoardAsciiPrinter.displayBoardAscii(fenString));
    }
}