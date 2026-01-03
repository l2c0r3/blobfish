package ch.hslu.cas.msed.blobfish.base;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PieceTest {

    private static Stream<Object[]> fenToPiece() {
        return Stream.of(
                new Object[]{'k', PieceType.KING, PlayerColor.BLACK},
                new Object[]{'K', PieceType.KING, PlayerColor.WHITE},
                new Object[]{'q', PieceType.QUEEN, PlayerColor.BLACK},
                new Object[]{'Q', PieceType.QUEEN, PlayerColor.WHITE},
                new Object[]{'r', PieceType.ROOK, PlayerColor.BLACK},
                new Object[]{'R', PieceType.ROOK, PlayerColor.WHITE},
                new Object[]{'b', PieceType.BISHOP, PlayerColor.BLACK},
                new Object[]{'B', PieceType.BISHOP, PlayerColor.WHITE},
                new Object[]{'n', PieceType.KNIGHT, PlayerColor.BLACK},
                new Object[]{'N', PieceType.KNIGHT, PlayerColor.WHITE},
                new Object[]{'p', PieceType.PAWN, PlayerColor.BLACK},
                new Object[]{'P', PieceType.PAWN, PlayerColor.WHITE}
        );
    }

    @ParameterizedTest
    @MethodSource("fenToPiece")
    void fromFen_parsesUpperAndLowerCase(char fenCode, PieceType expectedType, PlayerColor expectedColor) {
        // Act
        var result = new Piece(fenCode);

        // Assert
        assertEquals(expectedType, result.type());
        assertEquals(expectedColor, result.color());
    }
}
