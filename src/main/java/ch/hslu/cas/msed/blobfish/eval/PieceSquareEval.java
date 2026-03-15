package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.base.BoardTransformationUtil;
import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PieceType;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;

import java.util.stream.IntStream;

public class PieceSquareEval implements EvalStrategy {

    @Override
    public int getEvaluation(final ChessBoard board) {
        var boardAsArray = board.boardToArray();
        var isEndgame = board.isEndGame();

        return IntStream.range(0, boardAsArray.length).map(i -> {
            var piece = boardAsArray[i];
            if (piece == null) return 0;

            return getPieceValue(i, piece, isEndgame);
        }).sum();
    }

    private int getPieceValue(final int posIndex, final Piece piece, final boolean isEndgame) {
        var pst = getPstForPiece(piece, isEndgame);
        var index = piece.color() == PlayerColor.BLACK ? BoardTransformationUtil.flipFileIndex(posIndex) : BoardTransformationUtil.flipRankIndex(posIndex);

        return piece.color() == PlayerColor.BLACK ? -pst[index] : pst[index];
    }

    private int[] getPstForPiece(final Piece piece, final boolean isEndgame) {
        return switch (piece.type()) {
            case PieceType.PAWN -> PAWN_PST;
            case PieceType.KNIGHT -> KNIGHT_PST;
            case PieceType.BISHOP -> BISHOP_PST;
            case PieceType.ROOK -> ROOK_PST;
            case PieceType.QUEEN -> QUEEN_PST;
            case PieceType.KING -> isEndgame ? KING_END_PST : KING_OPENING_PST;
        };
    }

    /* =====================================
        Piece Square Tables - White POV
     ===================================== */

    private final int[] PAWN_PST = {
            0, 0, 0, 0, 0, 0, 0, 0,             // A8-H8
            50, 50, 50, 50, 50, 50, 50, 50,     // A7-H7
            10, 10, 20, 30, 30, 20, 10, 10,     // A6-H6
            5, 5, 10, 25, 25, 10, 5, 5,         // A5-H5
            0, 0, 0, 20, 20, 0, 0, 0,           // A4-H4
            5, -5, -10, 0, 0, -10, -5, 5,       // A3-H3
            5, 10, 10, -20, -20, 10, 10, 5,     // A2-H2
            0, 0, 0, 0, 0, 0, 0, 0              // A1-H1
    };

    private final int[] KNIGHT_PST = {
            -50, -40, -30, -30, -30, -30, -40, -50,     // A8-H8
            -40, -20, 0, 0, 0, 0, -20, -40,             // A7-H7
            -30, 0, 10, 15, 15, 10, 0, -30,             // A6-H6
            -30, 5, 15, 20, 20, 15, 5, -30,             // A5-H5
            -30, 0, 15, 20, 20, 15, 0, -30,             // A4-H4
            -30, 5, 10, 15, 15, 10, 5, -30,             // A3-H3
            -40, -20, 0, 5, 5, 0, -20, -40,             // A2-H2
            -50, -40, -30, -30, -30, -30, -40, -50      // A1-H1
    };

    private final int[] BISHOP_PST = {
            -20, -10, -10, -10, -10, -10, -10, -20,     // A8-H8
            -10, 0, 0, 0, 0, 0, 0, -10,                 // A7-H7
            -10, 0, 5, 10, 10, 5, 0, -10,               // A6-H6
            -10, 5, 5, 10, 10, 5, 5, -10,               // A5-H5
            -10, 0, 10, 10, 10, 10, 0, -10,             // A4-H4
            -10, 10, 10, 10, 10, 10, 10, -10,           // A3-H3
            -10, 5, 0, 0, 0, 0, 5, -10,                 // A2-H2
            -20, -10, -10, -10, -10, -10, -10, -20      // A1-H1
    };

    private final int[] ROOK_PST = {
            0, 0, 0, 0, 0, 0, 0, 0,         // A8-H8
            5, 10, 10, 10, 10, 10, 10, 5,   // A7-H7
            -5, 0, 0, 0, 0, 0, 0, -5,       // A6-H6
            -5, 0, 0, 0, 0, 0, 0, -5,       // A5-H5
            -5, 0, 0, 0, 0, 0, 0, -5,       // A4-H4
            -5, 0, 0, 0, 0, 0, 0, -5,       // A3-H3
            -5, 0, 0, 0, 0, 0, 0, -5,       // A2-H2
            0, 0, 0, 5, 5, 0, 0, 0          // A1-H1
    };

    private final int[] QUEEN_PST = {
            -20, -10, -10, -5, -5, -10, -10, -20,   // A8-H8
            -10, 0, 0, 0, 0, 0, 0, -10,             // A7-H7
            -10, 0, 5, 5, 5, 5, 0, -10,             // A6-H6
            -5, 0, 5, 5, 5, 5, 0, -5,               // A5-H5
            0, 0, 5, 5, 5, 5, 0, -5,                // A4-H4
            -10, 5, 5, 5, 5, 5, 0, -10,             // A3-H3
            -10, 0, 5, 0, 0, 0, 0, -10,             // A2-H2
            -20, -10, -10, -5, -5, -10, -10, -20    // A1-H1
    };

    private final int[] KING_OPENING_PST = {
            -30, -40, -40, -50, -50, -40, -40, -30,     // A8-H8
            -30, -40, -40, -50, -50, -40, -40, -30,     // A7-H7
            -30, -40, -40, -50, -50, -40, -40, -30,     // A6-H6
            -30, -40, -40, -50, -50, -40, -40, -30,     // A5-H5
            -20, -30, -30, -40, -40, -30, -30, -20,     // A4-H4
            -10, -20, -20, -20, -20, -20, -20, -10,     // A3-H3
            20, 20, 0, 0, 0, 0, 20, 20,                 // A2-H2
            20, 30, 10, 0, 0, 10, 30, 20                // A1-H1
    };

    private final int[] KING_END_PST = {
            -50, -40, -30, -20, -20, -30, -40, -50,     // A8-H8
            -30, -20, -10, 0, 0, -10, -20, -30,         // A7-H7
            -30, -10, 20, 30, 30, 20, -10, -30,         // A6-H6
            -30, -10, 30, 40, 40, 30, -10, -30,         // A5-H5
            -30, -10, 30, 40, 40, 30, -10, -30,         // A4-H4
            -30, -10, 20, 30, 30, 20, -10, -30,         // A3-H3
            -30, -30, 0, 0, 0, 0, -30, -30,             // A2-H2
            -50, -30, -30, -30, -30, -30, -30, -50      // A1-H1
    };
}
