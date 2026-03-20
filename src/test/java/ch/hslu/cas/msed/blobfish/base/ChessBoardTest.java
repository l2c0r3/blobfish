package ch.hslu.cas.msed.blobfish.base;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChessBoardTest {

    private static Stream<Object[]> boardArrayProvider() {
        return Stream.of(
                new Object[]{new ChessBoard("8/5ppk/4p1p1/3pq3/3Q4/1B2r2P/P5P1/3R3K b - - 8 42"), new char[]{
                        '.', '.', '.', 'R', '.', '.', '.', 'K', // A1-H1
                        'P', '.', '.', '.', '.', '.', 'P', '.', // A2-H2
                        '.', 'B', '.', '.', 'r', '.', '.', 'P', // A3-H3
                        '.', '.', '.', 'Q', '.', '.', '.', '.', // A4-H4
                        '.', '.', '.', 'p', 'q', '.', '.', '.', // A5-H5
                        '.', '.', '.', '.', 'p', '.', 'p', '.', // A6-H6
                        '.', '.', '.', '.', '.', 'p', 'p', 'k', // A7-H7
                        '.', '.', '.', '.', '.', '.', '.', '.'  // A8-H8
                }},
                new Object[]{new ChessBoard("Q7/p1pk3p/2p3p1/3p4/8/1PNbP3/P1PP2PP/R3KqNR w - - 7 17"), new char[]{
                        'R', '.', '.', '.', 'K', 'q', 'N', 'R', // A1-H1
                        'P', '.', 'P', 'P', '.', '.', 'P', 'P', // A2-H2
                        '.', 'P', 'N', 'b', 'P', '.', '.', '.', // A3-H3
                        '.', '.', '.', '.', '.', '.', '.', '.', // A4-H4
                        '.', '.', '.', 'p', '.', '.', '.', '.', // A5-H5
                        '.', '.', 'p', '.', '.', '.', 'p', '.', // A6-H6
                        'p', '.', 'p', 'k', '.', '.', '.', 'p', // A7-H7
                        'Q', '.', '.', '.', '.', '.', '.', '.'  // A8-H8
                }}
        );
    }

    private static Stream<Object[]> endgameBoardProvider() {
        return Stream.of(
                // position, endgame
                new Object[]{new ChessBoard("8/5ppk/4p1p1/3pq3/3Q4/1B2r2P/P5P1/3R3K b - - 8 42"), true},
                new Object[]{new ChessBoard("Q7/p1pk3p/2p3p1/3p4/8/1PNbP3/P1PP2PP/R3KqNR w - - 7 17"), false},
                new Object[]{new ChessBoard("Q7/p1pk3p/2p2qp1/3p1b2/8/1PN1P3/P1PP2PP/R4KNR b - - 4 15"), false},
                new Object[]{new ChessBoard("3r2k1/p4ppp/1p6/5p2/1P3P2/4P3/PQR1KP1P/3q4 w - - 7 28"), true}
        );
    }

    private static Stream<Object[]> matedBoardProvider() {
        return Stream.of(
                // position, mated
                new Object[]{new ChessBoard("8/5ppk/4p1p1/3pq3/3Q4/1B2r2P/P5P1/3R3K b - - 8 42"), false},
                new Object[]{new ChessBoard("Q7/p1pk3p/2p3p1/3p4/8/1PNbP3/P1PP2PP/R3KqNR w - - 7 17"), true},
                new Object[]{new ChessBoard("Q7/p1pk3p/2p2qp1/3p1b2/8/1PN1P3/P1PP2PP/R4KNR b - - 4 15"), false},
                new Object[]{new ChessBoard("3r2k1/p4ppp/1p6/5p2/1P3P2/4P3/PQR1KP1P/3q4 w - - 7 28"), true}
        );
    }

    @ParameterizedTest
    @MethodSource("boardArrayProvider")
    void boardToArray(ChessBoard board, char[] fenArray) {
        // Arrange
        var expected = IntStream.range(0, fenArray.length)
                .mapToObj(i -> fenArray[i])
                .map(c -> c == '.' ? null : new Piece(c))
                .toArray(Piece[]::new);

        // Act
        var actual = board.boardToArray();

        // Assert
        assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("endgameBoardProvider")
    void isEndGame(ChessBoard board, boolean isEndgame) {
        // Act
        var actual = board.isEndGame();

        // Assert
        assertEquals(isEndgame, actual);
    }

    @ParameterizedTest
    @MethodSource("matedBoardProvider")
    void isMated(ChessBoard board, boolean isMated) {
        // Act
        var actual = board.isMated();

        // Assert
        assertEquals(isMated, actual);
    }
}