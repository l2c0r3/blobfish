package ch.hslu.cas.msed.blobfish.board;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessBoardPrinter {


    private static final char WHITE_KING = '♔';
    private static final char WHITE_QUEEN = '♕';
    private static final char WHITE_ROOK = '♖';
    private static final char WHITE_BISHOP = '♗';
    private static final char WHITE_KNIGHT = '♘';
    private static final char WHITE_PAWN = '♙';
    private static final char WHITE_SQUARE = '□';

    private static final char BLACK_KING = '♚';
    private static final char BLACK_QUEEN = '♛';
    private static final char BLACK_ROOK = '♜';
    private static final char BLACK_BISHOP = '♝';
    private static final char BLACK_KNIGHT = '♞';
    private static final char BLACK_PAWN = '♟';
    private static final char BLACK_SQUARE = '■';

    private ChessBoardPrinter() {
        // utility class
    }

    /**
     * @param fen position of board
     * @param perspective which color is at the bottom
     * @return the position of the chessboard in a String with Ascii Characters
     */
    public static String displayBoardAscii(String fen, PlayerColor perspective) {
        var fields = initFields();


        var result = mapFieldsToString(fields);

        System.out.println(result);
        return result;
    }

    private static String mapFieldsToString(List<List<Character>> fields) {
        return fields.stream()
                .map(row -> row.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
    }

    private static List<List<Character>> initFields() {
        var boardList = new ArrayList<List<Character>>();
        for (int row = 0; row < 8; row++) {
            var rowList = new ArrayList<Character>();
            for (int col = 0; col < 8; col++) {
                if (row % 2 == 0 && col % 2 == 0) {
                    rowList.add(WHITE_SQUARE);
                } else if (row % 2 == 0) {
                    rowList.add(BLACK_SQUARE);
                } else if (col % 2 == 0) {
                    rowList.add(BLACK_SQUARE);
                } else {
                    rowList.add(WHITE_SQUARE);
                }
            }
            boardList.add(rowList);
        }
        return boardList;
    }
}
