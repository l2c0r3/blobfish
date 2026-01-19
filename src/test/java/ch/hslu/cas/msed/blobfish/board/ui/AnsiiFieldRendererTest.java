package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PieceType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static ch.hslu.cas.msed.blobfish.board.ui.AnsiiFieldRenderer.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnsiiFieldRendererTest {

    private final AnsiiFieldRenderer testee = new AnsiiFieldRenderer();

    @ParameterizedTest
    @MethodSource("renderPieceCases")
    void render_piece_rendersExpectedAnsiString(UiField uiField, char expectedSymbol, String expectedBg, String expectedFg) {
        // Act
        String actual = testee.render(uiField);

        // Assert
        String expected = expectedBg + expectedFg + " " + expectedSymbol + " " + RESET;
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> renderPieceCases() {


        return Stream.of(
                Arguments.of(new UiField(new Piece(PieceType.KING, PlayerColor.WHITE), SquareColor.LIGHT), '♚', LIGHT_BG, WHITE_PIECE),
                Arguments.of(new UiField(new Piece(PieceType.QUEEN, PlayerColor.BLACK), SquareColor.DARK), '♛', DARK_BG, BLACK_PIECE),
                Arguments.of(new UiField(new Piece(PieceType.ROOK, PlayerColor.WHITE), SquareColor.DARK), '♜', DARK_BG, WHITE_PIECE),
                Arguments.of(new UiField(new Piece(PieceType.BISHOP, PlayerColor.BLACK), SquareColor.LIGHT), '♝', LIGHT_BG, BLACK_PIECE),
                Arguments.of(new UiField(new Piece(PieceType.KNIGHT, PlayerColor.WHITE), SquareColor.LIGHT), '♞', LIGHT_BG, WHITE_PIECE),
                Arguments.of(new UiField(new Piece(PieceType.PAWN, PlayerColor.BLACK), SquareColor.DARK), '♙', DARK_BG, BLACK_PIECE)
        );
    }
}
