package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.Color;
import ch.hslu.cas.msed.blobfish.base.Piece;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UiFieldPrinterTest {

    @ParameterizedTest
    @CsvSource({
            "test, ♜ ",
            "tEst,TEST",
            "Java,JAVA"})
    void toUpperCase_ShouldGenerateTheExpectedUppercaseValue(String expected, UiFieldPrinter testee) {
        // Act
        var result = testee.print();

        // Assert
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideStringsForIsBlank() {
        return Stream.of(
                Arguments.of("", new UiFieldPrinter(Piece.ROOK, Color.BLACK, Color.WHITE)),
                Arguments.of("", new UiFieldPrinter(Piece.KNIGHT, Color.BLACK, Color.BLACK)),
                Arguments.of("", new UiFieldPrinter(Piece.BISHOP, Color.BLACK, Color.WHITE)),
                Arguments.of("", new UiFieldPrinter(Piece.QUEEN, Color.BLACK, Color.BLACK)),
                Arguments.of("", new UiFieldPrinter(Piece.KNIGHT, Color.BLACK, Color.BLACK)),
                Arguments.of("", new UiFieldPrinter(Piece.PAWN, Color.BLACK, Color.BLACK)),

                Arguments.of("", new UiFieldPrinter(Piece.ROOK, Color.WHITE, Color.WHITE)),
                Arguments.of("", new UiFieldPrinter(Piece.KNIGHT, Color.WHITE, Color.BLACK)),
                Arguments.of("", new UiFieldPrinter(Piece.BISHOP, Color.WHITE, Color.WHITE)),
                Arguments.of("", new UiFieldPrinter(Piece.QUEEN, Color.WHITE, Color.BLACK)),
                Arguments.of("", new UiFieldPrinter(Piece.KNIGHT, Color.WHITE, Color.BLACK)),
                Arguments.of("", new UiFieldPrinter(Piece.PAWN, Color.WHITE, Color.BLACK))
        );
    }

}