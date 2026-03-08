package ch.hslu.cas.msed.blobfish.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class BoardTransformationUtilTest {

    private static final String[] board = {
            "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", // A1-H1
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", // A2-H2
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", // A3-H3
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", // A4-H4
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", // A5-H5
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", // A6-H6
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", // A7-H7
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"  // A8-H8
    };

    private static final String[] flippedRankBoard = {
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8", // A8-H8
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7", // A7-H7
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6", // A6-H6
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", // A5-H5
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", // A4-H4
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", // A3-H3
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", // A2-H2
            "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"  // A1-H1
    };

    private static final String[] flippedFileBoard = {
            "h1", "g1", "f1", "e1", "d1", "c1", "b1", "a1", // H1-A1
            "h2", "g2", "f2", "e2", "d2", "c2", "b2", "a2", // H2-A2
            "h3", "g3", "f3", "e3", "d3", "c3", "b3", "a3", // H3-A3
            "h4", "g4", "f4", "e4", "d4", "c4", "b4", "a4", // H4-A4
            "h5", "g5", "f5", "e5", "d5", "c5", "b5", "a5", // H5-A5
            "h6", "g6", "f6", "e6", "d6", "c6", "b6", "a6", // H6-A6
            "h7", "g7", "f7", "e7", "d7", "c7", "b7", "a7", // H7-A7
            "h8", "g8", "f8", "e8", "d8", "c8", "b8", "a8"  // H8-A8
    };

    private static final String[] rotatedBoard = {
            "h8", "g8", "f8", "e8", "d8", "c8", "b8", "a8", // H8-A8
            "h7", "g7", "f7", "e7", "d7", "c7", "b7", "a7", // H7-A7
            "h6", "g6", "f6", "e6", "d6", "c6", "b6", "a6", // H6-A6
            "h5", "g5", "f5", "e5", "d5", "c5", "b5", "a5", // H5-A5
            "h4", "g4", "f4", "e4", "d4", "c4", "b4", "a4", // H4-A4
            "h3", "g3", "f3", "e3", "d3", "c3", "b3", "a3", // H3-A3
            "h2", "g2", "f2", "e2", "d2", "c2", "b2", "a2", // H2-A2
            "h1", "g1", "f1", "e1", "d1", "c1", "b1", "a1"  // H1-A1
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
        var flippedField = flippedRankBoard[flippedIndex];

        Assertions.assertEquals(expectedField, board[posIndex]);
        Assertions.assertEquals(expectedField, flippedField);
    }

    @ParameterizedTest
    @MethodSource("boardArrayProvider")
    void flipFileIndex(int posIndex, String expectedField) {
        var flippedIndex = BoardTransformationUtil.flipFileIndex(posIndex);
        var flippedField = flippedFileBoard[flippedIndex];

        Assertions.assertEquals(expectedField, board[posIndex]);
        Assertions.assertEquals(expectedField, flippedField);
    }

    @ParameterizedTest
    @MethodSource("boardArrayProvider")
    void rotateBoardIndex(int posIndex, String expectedField) {
        var flippedIndex = BoardTransformationUtil.rotateBoardIndex(posIndex);
        var flippedField = rotatedBoard[flippedIndex];

        Assertions.assertEquals(expectedField, board[posIndex]);
        Assertions.assertEquals(expectedField, flippedField);
    }
}