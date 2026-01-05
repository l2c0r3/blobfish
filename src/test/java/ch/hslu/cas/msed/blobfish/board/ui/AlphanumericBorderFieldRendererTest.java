package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ch.hslu.cas.msed.blobfish.board.ui.AlphanumericBorderFieldRenderer.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AlphanumericBorderFieldRendererTest {

    private final AlphanumericBorderFieldRenderer testee = new AlphanumericBorderFieldRenderer();

    @ParameterizedTest
    @MethodSource("getRenderPerspectives")
    void renderRow_rendersCorrectPerspective(PlayerColor perspective, String expected) {
        // Act
        String actual = testee.renderRow(perspective);

        // Assert
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("getRenderRowNumbers")
    void renderColumnField_rendersCorrectNumbers(int colNumber) {
        // Act
        String actual = testee.renderColumnField(colNumber);

        // Assert
        var expected = BG_COLOR + FG_COLOR + " " + colNumber + " " + RESET;
        assertEquals(expected, actual);
    }


    private static Stream<Arguments> getRenderPerspectives() {
        return Stream.of(
                Arguments.of(PlayerColor.WHITE, BG_COLOR + PLACEHOLDER_COLOR + " " + PLACEHOLDER_CHARACTER + " " + RESET + BG_COLOR + FG_COLOR + ROW_STRING_WHITE + RESET + BG_COLOR + PLACEHOLDER_COLOR + " " + PLACEHOLDER_CHARACTER + " " + RESET),
                Arguments.of(PlayerColor.BLACK, BG_COLOR + PLACEHOLDER_COLOR + " " + PLACEHOLDER_CHARACTER + " " + RESET + BG_COLOR + FG_COLOR + StringUtils.reverse(ROW_STRING_WHITE) + RESET + BG_COLOR + PLACEHOLDER_COLOR + " " + PLACEHOLDER_CHARACTER + " " + RESET)
        );
    }


    private static Stream<Arguments> getRenderRowNumbers() {
        return IntStream.rangeClosed(1, 8).mapToObj(Arguments::of);
    }
}