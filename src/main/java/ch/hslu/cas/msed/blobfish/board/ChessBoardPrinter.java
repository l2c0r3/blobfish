package ch.hslu.cas.msed.blobfish.board;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessBoardPrinter {

    public static final char WHITE_KING = '♔';
    public static final char WHITE_QUEEN = '♕';
    public static final char WHITE_ROOK = '♖';
    public static final char WHITE_BISHOP = '♗';
    public static final char WHITE_KNIGHT = '♘';
    public static final char WHITE_PAWN = '♙';
    public static final char WHITE_SQUARE = '□';

    public static final char BLACK_KING = '♚';
    public static final char BLACK_QUEEN = '♛';
    public static final char BLACK_ROOK = '♜';
    public static final char BLACK_BISHOP = '♝';
    public static final char BLACK_KNIGHT = '♞';
    public static final char BLACK_PAWN = '♟';
    public static final char BLACK_SQUARE = '■';

    private static final Map<Character, Character> CHARACTER_MAP = new HashMap<Character, Character>() {{
        // black pieces
        put('r', BLACK_ROOK);
        put('n', BLACK_KNIGHT);
        put('b', BLACK_BISHOP);
        put('q', BLACK_QUEEN);
        put('k', BLACK_KING);
        put('p', BLACK_PAWN);

        // white pieces
        put('R', WHITE_ROOK);
        put('N', WHITE_KNIGHT);
        put('B', WHITE_BISHOP);
        put('Q', WHITE_QUEEN);
        put('K', WHITE_KING);
        put('P', WHITE_PAWN);
    }};

    private ChessBoardPrinter() {
        // utility class
    }

    /**
     * @param fenString position of board
     * @return the position of the chessboard in a String with Ascii Characters
     */
    public static String displayBoardAscii(String fenString) {
        var fields = initFields();
        var fenBlocks = getFenBlocks(fenString);

        // replace empty blocks by characters
        for (int rowPointer = 0; rowPointer < fenBlocks.length; rowPointer++) {
            var fieldsColumnPointer = 0;

            for (int columnPointer = 0; columnPointer < fenBlocks[rowPointer].length(); columnPointer++) {
                var fenCode = fenBlocks[rowPointer].charAt(columnPointer);

                // skip by numbers
                if (StringUtils.isNumeric(String.valueOf(fenCode))) {
                    int parsedFenCode = Integer.parseInt(String.valueOf(fenCode));
                    fieldsColumnPointer = fieldsColumnPointer + parsedFenCode;
                    continue;
                }

                // replace pieces in list
                var pieceToSet = CHARACTER_MAP.get(fenCode);
                var row = fields.get(rowPointer);
                row.set(fieldsColumnPointer, pieceToSet);
                fieldsColumnPointer++;
            }
        }

        return mapFieldsToString(fields);
    }

    private static String[] getFenBlocks(String fenString) {
        if (StringUtils.isEmpty(fenString)) {
            return new String[0];
        }

        // only get the pos infos
        if (fenString.contains(" ")) {
            fenString = fenString.substring(0, fenString.indexOf(" "));
        }

        return fenString.split("/");
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
