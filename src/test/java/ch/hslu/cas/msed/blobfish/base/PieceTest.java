package ch.hslu.cas.msed.blobfish.base;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PieceTest {

    static Stream<Object[]> fenToPiece() {
        return Stream.of(
                new Object[]{'k', PieceType.KING, Color.BLACK},
                new Object[]{'K', PieceType.KING, Color.WHITE},
                new Object[]{'q', PieceType.QUEEN, Color.BLACK},
                new Object[]{'Q', PieceType.QUEEN, Color.WHITE},
                new Object[]{'r', PieceType.ROOK, Color.BLACK},
                new Object[]{'R', PieceType.ROOK, Color.WHITE},
                new Object[]{'b', PieceType.BISHOP, Color.BLACK},
                new Object[]{'B', PieceType.BISHOP, Color.WHITE},
                new Object[]{'n', PieceType.KNIGHT, Color.BLACK},
                new Object[]{'N', PieceType.KNIGHT, Color.WHITE},
                new Object[]{'p', PieceType.PAWN, Color.BLACK},
                new Object[]{'P', PieceType.PAWN, Color.WHITE}
        );
    }

    @ParameterizedTest
    @MethodSource("fenToPiece")
    void fromFen_parsesUpperAndLowerCase(char fenCode, PieceType expectedType, Color expectedColor) {
        // Act
        var result = new Piece(fenCode);

        // Assert
        assertEquals(expectedType, result.type());
        assertEquals(expectedColor, result.color());
    }
}
