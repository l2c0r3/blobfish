package ch.hslu.cas.msed.blobfish.board;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessBoardPrinterTest {

    @Test
    void displayBoardAscii_startPosition() {
        // Arrange
        var position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        // Act
        var result = ChessBoardPrinter.displayBoardAscii(position, PlayerColor.WHITE);

        // Assert
        assertEquals("U+2654", result);


    }
}