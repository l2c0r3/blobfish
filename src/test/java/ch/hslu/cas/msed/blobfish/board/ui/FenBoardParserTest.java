package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PieceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static ch.hslu.cas.msed.blobfish.base.PlayerColor.BLACK;
import static ch.hslu.cas.msed.blobfish.base.PlayerColor.WHITE;
import static ch.hslu.cas.msed.blobfish.base.PieceType.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FenBoardParserTest {

    private final FenBoardParser testee = new FenBoardParser();

    @Test
    void displayBoardAscii_nullPosition() {
        // Arrange
        var position = "8/8/8/8/8/8/8/8";

        // Act
        var result = testee.parse(position).getAllFields();

        // Assert
        assertArrayEquals(
                new UiField[][]{
                        new UiField[]{emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK)},
                        new UiField[]{emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE)},
                        new UiField[]{emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK)},
                        new UiField[]{emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE)},
                        new UiField[]{emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK)},
                        new UiField[]{emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE)},
                        new UiField[]{emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK)},
                        new UiField[]{emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE)},
                }, result);
    }

    @Test
    void displayBoardAscii_startPosition() {
        // Arrange
        var position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        // Act
        var result = testee.parse(position).getAllFields();

        // Assert
        assertArrayEquals(
                new UiField[][]{
                        new UiField[]{field(ROOK, BLACK, WHITE), field(KNIGHT, BLACK, BLACK), field(BISHOP, BLACK, WHITE), field(QUEEN, BLACK, BLACK), field(KING, BLACK, WHITE), field(BISHOP, BLACK, BLACK), field(KNIGHT, BLACK, WHITE), field(ROOK, BLACK, BLACK)},
                        new UiField[]{field(PAWN, BLACK, BLACK), field(PAWN, BLACK, WHITE), field(PAWN, BLACK, BLACK), field(PAWN, BLACK, WHITE), field(PAWN, BLACK, BLACK), field(PAWN, BLACK, WHITE), field(PAWN, BLACK, BLACK), field(PAWN, BLACK, WHITE)},
                        new UiField[]{emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK)},
                        new UiField[]{emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE)},
                        new UiField[]{emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK)},
                        new UiField[]{emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE)},
                        new UiField[]{field(PAWN, WHITE, WHITE), field(PAWN, WHITE, BLACK), field(PAWN, WHITE, WHITE), field(PAWN, WHITE, BLACK), field(PAWN, WHITE, WHITE), field(PAWN, WHITE, BLACK), field(PAWN, WHITE, WHITE), field(PAWN, WHITE, BLACK)},
                        new UiField[]{field(ROOK, WHITE, BLACK), field(KNIGHT, WHITE, WHITE), field(BISHOP, WHITE, BLACK), field(QUEEN, WHITE, WHITE), field(KING, WHITE, BLACK), field(BISHOP, WHITE, WHITE), field(KNIGHT, WHITE, BLACK), field(ROOK, WHITE, WHITE)}
                },
                result
        );
    }

    @Test
    void displayBoardAscii_randomPos1() {
        // Arrange
        var position = "r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1";

        // Act
        var result = testee.parse(position).getAllFields();

        // Assert
        assertArrayEquals(
                new UiField[][]{
                        new UiField[]{field(ROOK, BLACK, WHITE), emptyField(BLACK), field(BISHOP, BLACK, WHITE), field(KING, BLACK, BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), field(ROOK, BLACK, BLACK)},
                        new UiField[]{field(PAWN, BLACK, BLACK), emptyField(WHITE), emptyField(BLACK), field(PAWN, BLACK, WHITE), field(BISHOP, WHITE, BLACK), field(PAWN, BLACK, WHITE), field(KNIGHT, WHITE, BLACK), field(PAWN, BLACK, WHITE)},
                        new UiField[]{field(KNIGHT, BLACK, WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), field(KNIGHT, BLACK, BLACK), emptyField(WHITE), emptyField(BLACK)},
                        new UiField[]{emptyField(BLACK), field(PAWN, BLACK, WHITE), emptyField(BLACK), field(KNIGHT, WHITE, WHITE), field(PAWN, WHITE, BLACK), emptyField(WHITE), emptyField(BLACK), field(PAWN, WHITE, WHITE)},
                        new UiField[]{emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), field(PAWN, WHITE, WHITE), emptyField(BLACK)},
                        new UiField[]{emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), field(PAWN, WHITE, WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE)},
                        new UiField[]{field(PAWN, WHITE, WHITE), emptyField(BLACK), field(PAWN, WHITE, WHITE), emptyField(BLACK), field(KING, WHITE, WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK)},
                        new UiField[]{field(QUEEN, BLACK, BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), emptyField(BLACK), emptyField(WHITE), field(BISHOP, BLACK, BLACK), emptyField(WHITE)},
                },
                result
        );
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
    void displayBoardAscii_invalidFenString_throwsException(String position) {
        // Act & assert
        assertThrows(IllegalArgumentException.class, () -> testee.parse(position));
    }

    private UiField emptyField(PlayerColor color) {
        return new UiField(null, color);
    }

    private static UiField field(PieceType pieceType, PlayerColor pieceColor, PlayerColor fieldColor) {
        return new UiField(new Piece(pieceType, pieceColor), fieldColor);
    }
}