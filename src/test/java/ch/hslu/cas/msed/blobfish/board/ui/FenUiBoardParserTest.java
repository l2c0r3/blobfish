package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PieceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static ch.hslu.cas.msed.blobfish.base.PieceType.*;
import static ch.hslu.cas.msed.blobfish.base.PlayerColor.*;
import static ch.hslu.cas.msed.blobfish.board.ui.SquareColor.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FenUiBoardParserTest {

    private final FenUiBoardParser testee = new FenUiBoardParser();

    @Test
    void parse_emptyBoard_returnsAllEmptyFields() {
        // Arrange
        var position = "8/8/8/8/8/8/8/8";

        // Act
        var result = testee.parse(position).getAllFields();

        // Assert
        assertArrayEquals(
                new UiField[][]{
                        new UiField[]{emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK)},
                        new UiField[]{emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT)},
                        new UiField[]{emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK)},
                        new UiField[]{emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT)},
                        new UiField[]{emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK)},
                        new UiField[]{emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT)},
                        new UiField[]{emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK)},
                        new UiField[]{emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT)},
                }, result);
    }

    @Test
    void parse_startPosition_returnsCorrectPieces() {
        // Arrange
        var position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        // Act
        var result = testee.parse(position).getAllFields();

        // Assert
        assertArrayEquals(
                new UiField[][]{
                        new UiField[]{field(ROOK, BLACK, LIGHT), field(KNIGHT, BLACK, DARK), field(BISHOP, BLACK, LIGHT), field(QUEEN, BLACK, DARK), field(KING, BLACK, LIGHT), field(BISHOP, BLACK, DARK), field(KNIGHT, BLACK, LIGHT), field(ROOK, BLACK, DARK)},
                        new UiField[]{field(PAWN, BLACK, DARK), field(PAWN, BLACK, LIGHT), field(PAWN, BLACK, DARK), field(PAWN, BLACK, LIGHT), field(PAWN, BLACK, DARK), field(PAWN, BLACK, LIGHT), field(PAWN, BLACK, DARK), field(PAWN, BLACK, LIGHT)},
                        new UiField[]{emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK)},
                        new UiField[]{emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT)},
                        new UiField[]{emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK)},
                        new UiField[]{emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT)},
                        new UiField[]{field(PAWN, WHITE, LIGHT), field(PAWN, WHITE, DARK), field(PAWN, WHITE, LIGHT), field(PAWN, WHITE, DARK), field(PAWN, WHITE, LIGHT), field(PAWN, WHITE, DARK), field(PAWN, WHITE, LIGHT), field(PAWN, WHITE, DARK)},
                        new UiField[]{field(ROOK, WHITE, DARK), field(KNIGHT, WHITE, LIGHT), field(BISHOP, WHITE, DARK), field(QUEEN, WHITE, LIGHT), field(KING, WHITE, DARK), field(BISHOP, WHITE, LIGHT), field(KNIGHT, WHITE, DARK), field(ROOK, WHITE, LIGHT)}
                },
                result
        );
    }

    @Test
    void parse_midGamePosition_returnsCorrectPieces() {
        // Arrange
        var position = "r1bk3r/p2pBpNp/n4n2/1p1NP2P/6P1/3P4/P1P1K3/q5b1";

        // Act
        var result = testee.parse(position).getAllFields();

        // Assert
        assertArrayEquals(
                new UiField[][]{
                        new UiField[]{field(ROOK, BLACK, LIGHT), emptyField(DARK), field(BISHOP, BLACK, LIGHT), field(KING, BLACK, DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), field(ROOK, BLACK, DARK)},
                        new UiField[]{field(PAWN, BLACK, DARK), emptyField(LIGHT), emptyField(DARK), field(PAWN, BLACK, LIGHT), field(BISHOP, WHITE, DARK), field(PAWN, BLACK, LIGHT), field(KNIGHT, WHITE, DARK), field(PAWN, BLACK, LIGHT)},
                        new UiField[]{field(KNIGHT, BLACK, LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), field(KNIGHT, BLACK, DARK), emptyField(LIGHT), emptyField(DARK)},
                        new UiField[]{emptyField(DARK), field(PAWN, BLACK, LIGHT), emptyField(DARK), field(KNIGHT, WHITE, LIGHT), field(PAWN, WHITE, DARK), emptyField(LIGHT), emptyField(DARK), field(PAWN, WHITE, LIGHT)},
                        new UiField[]{emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), field(PAWN, WHITE, LIGHT), emptyField(DARK)},
                        new UiField[]{emptyField(DARK), emptyField(LIGHT), emptyField(DARK), field(PAWN, WHITE, LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT)},
                        new UiField[]{field(PAWN, WHITE, LIGHT), emptyField(DARK), field(PAWN, WHITE, LIGHT), emptyField(DARK), field(KING, WHITE, LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK)},
                        new UiField[]{field(QUEEN, BLACK, DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), emptyField(DARK), emptyField(LIGHT), field(BISHOP, BLACK, DARK), emptyField(LIGHT)},
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
    void parse_invalidFenString_throwsException(String position) {
        // Act & assert
        assertThrows(IllegalArgumentException.class, () -> testee.parse(position));
    }

    private UiField emptyField(SquareColor color) {
        return new UiField(null, color);
    }

    private static UiField field(PieceType pieceType, PlayerColor pieceColor, SquareColor fieldColor) {
        return new UiField(new Piece(pieceType, pieceColor), fieldColor);
    }
}