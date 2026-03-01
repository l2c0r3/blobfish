package ch.hslu.cas.msed.blobfish.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class BoardTransformationUtilTest {

    private static final String[] board = {
            "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", // A-H1
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", // A-H2
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", // A-H3
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", // A-H4
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", // A-H5
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", // A-H6
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", // A-H7
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"  // A-H8
    };

    private static final String[] flippedBoard = {
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", // A-H8
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", // A-H7
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", // A-H6
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", // A-H5
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", // A-H4
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", // A-H3
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", // A-H2
            "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"  // A-H1
    };

    private static Stream<Object[]> boardArrayProvider() {
        return IntStream.range(0, board.length).mapToObj(
                idx -> new Object[]{idx, board[idx]}
        );
    }

    @ParameterizedTest
    @MethodSource("boardArrayProvider")
    void flipRankIndex(int posIndex, String expectedField) {
        var flippedIndex = BoardTransformationUtil.flipRankIndex(posIndex);
        var flippedField = flippedBoard[flippedIndex];

        Assertions.assertEquals(expectedField, board[posIndex]);
        Assertions.assertEquals(flippedField, expectedField);
    }
}