package ch.hslu.cas.msed.blobfish.board;

import org.junit.jupiter.api.Test;

import static ch.hslu.cas.msed.blobfish.board.ChessBoardPrinter.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChessBoardPrinterTest {

    @Test
    void displayBoardAscii_nullPosition() {
        // Act
        var result = ChessBoardPrinter.displayBoardAscii(null);

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
        var result = ChessBoardPrinter.displayBoardAscii(position);

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
        var result = ChessBoardPrinter.displayBoardAscii(position);

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
}