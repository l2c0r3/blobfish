package ch.hslu.cas.msed.blobfish.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PieceTypeTest {
    static Stream<Object[]> fenToPiece() {
        return Stream.of(
                new Object[]{'k', PieceType.KING},
                new Object[]{'K', PieceType.KING},
                new Object[]{'q', PieceType.QUEEN},
                new Object[]{'Q', PieceType.QUEEN},
                new Object[]{'r', PieceType.ROOK},
                new Object[]{'R', PieceType.ROOK},
                new Object[]{'b', PieceType.BISHOP},
                new Object[]{'B', PieceType.BISHOP},
                new Object[]{'n', PieceType.KNIGHT},
                new Object[]{'N', PieceType.KNIGHT},
                new Object[]{'p', PieceType.PAWN},
                new Object[]{'P', PieceType.PAWN}
        );
    }

    @ParameterizedTest
    @MethodSource("fenToPiece")
    void fromFen_parsesUpperAndLowerCase(char fenCode, PieceType expected) {
        assertEquals(expected, PieceType.fromFen(fenCode));
    }

    @Test
    void fromFen_throwsOnUnknownFen() {
        assertThrows(IllegalArgumentException.class, () -> PieceType.fromFen('x'));
        assertThrows(IllegalArgumentException.class, () -> PieceType.fromFen('1'));
        assertThrows(IllegalArgumentException.class, () -> PieceType.fromFen('?'));
    }

    @Test
    void points_areCorrect() {
        assertEquals(9000, PieceType.KING.getPoints());
        assertEquals(900, PieceType.QUEEN.getPoints());
        assertEquals(500, PieceType.ROOK.getPoints());
        assertEquals(300, PieceType.BISHOP.getPoints());
        assertEquals(300, PieceType.KNIGHT.getPoints());
        assertEquals(100, PieceType.PAWN.getPoints());
    }
}
