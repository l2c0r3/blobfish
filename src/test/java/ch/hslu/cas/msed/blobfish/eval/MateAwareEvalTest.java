package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MateAwareEvalTest {

    MateAwareEval testee = new MateAwareEval(new TestEval());

    private static Stream<Object[]> positionToEvalProvider() {
        return Stream.of(
                new Object[]{new ChessBoard("1R1k4/R7/8/8/8/8/8/1K6 b - - 0 1"), 1_000_000},    // white wins, b has to play
                new Object[]{new ChessBoard("3k4/R7/1R6/8/8/8/8/1K6 b - - 0 1"), 5},        // white not wins, b has to play
                new Object[]{new ChessBoard("1r1K4/r7/8/8/8/8/8/1k6 w - - 0 1"), -1_000_000},   // black wins, w has to play
                new Object[]{new ChessBoard("3K4/r7/1r6/8/8/8/8/1k6 w - - 0 1"), 5}        // black not wins, w has to play
        );
    }

    @MethodSource("positionToEvalProvider")
    @ParameterizedTest
    void getEvaluation_returnsExpected(ChessBoard board, int expectedResult) {
        // Act
        var result = testee.getEvaluation(board);

        // Assert
        assertEquals(expectedResult, result);
    }

    static class TestEval implements EvalStrategy {
        @Override
        public int getEvaluation(ChessBoard board) {
            return 5;
        }
    }
}