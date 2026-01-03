package ch.hslu.cas.msed.blobfish.eval;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaterialEvalTest {

    private final MaterialEval testee = new MaterialEval();

    private static Stream<Object[]> positionToEvalProvider() {
        return Stream.of(
                new Object[]{"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 0.0},   //start position
                new Object[]{"rnbqkbnr/ppp1pppp/8/3P4/8/8/PP1PPPPP/RNBQKBNR b KQkq - 0 2", 1.0},
                new Object[]{"r3kbnr/pp1bp1pp/2Q5/1p6/8/4K3/PP3PPP/R1B2BNR w kq - 1 12", 8.0},
                new Object[]{"rnbqkbnr/pp1p1ppp/8/3p4/3p4/4P3/PPP2PPP/RNB1KBNR w KQkq - 0 5", -10}
        );
    }

    @ParameterizedTest
    @MethodSource("positionToEvalProvider")
    void render_piece_rendersExpectedAnsiString(String position, double expectedEval) {
        // Act
        var actual = testee.getEvaluation(position);

        // Assert
        assertEquals(expectedEval, actual);
    }

}