package ch.hslu.cas.msed.blobfish.evaluation;

import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaterialEvalTest {

    private final MaterialEval testee = new MaterialEval();

    private static Stream<Object[]> positionToEvalProvider() {
        return Stream.of(
                new Object[]{new ChessBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"), 0},   //start position
                new Object[]{new ChessBoard("rnbqkbnr/ppp1pppp/8/3P4/8/8/PP1PPPPP/RNBQKBNR b KQkq - 0 2"), 100},
                new Object[]{new ChessBoard("r3kbnr/pp1bp1pp/2Q5/1p6/8/4K3/PP3PPP/R1B2BNR w kq - 1 12"), 800},
                new Object[]{new ChessBoard("rnbqkbnr/pp1p1ppp/8/3p4/3p4/4P3/PPP2PPP/RNB1KBNR w KQkq - 0 5"), -1000}
        );
    }

    @ParameterizedTest
    @MethodSource("positionToEvalProvider")
    void getEvaluation_returnsExpected(ChessBoard board, int expectedEval) {
        // Act
        var actual = testee.getEvaluation(board);

        // Assert
        assertEquals(expectedEval, actual);
    }

}