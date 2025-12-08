package ch.hslu.cas.msed.blobfish.board;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
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

    private static final Map<Character, Character> CHARACTER_MAP = Map.ofEntries(
            // black pieces
    Map.entry('r', BLACK_ROOK),
            Map.entry('n', BLACK_KNIGHT),
            Map.entry('b', BLACK_BISHOP),
            Map.entry('q', BLACK_QUEEN),
            Map.entry('k', BLACK_KING),
            Map.entry('p', BLACK_PAWN),

            // white pieces
            Map.entry('R', WHITE_ROOK),
            Map.entry('N', WHITE_KNIGHT),
            Map.entry('B', WHITE_BISHOP),
            Map.entry('Q', WHITE_QUEEN),
            Map.entry('K', WHITE_KING),
            Map.entry('P', WHITE_PAWN)
    );

    private ChessBoardPrinter() {
        // utility class
    }

    /**
     * @param fenString position of board
     * @return the position of the chessboard in a String with Ascii Characters
     */
    public static String displayBoardAscii(String fenString) {
        validateFenString(fenString);

        var fields = initFields();
        var fenBlocks = getFenBlocks(fenString);

        // replace empty blocks by characters
        for (int rowPointer = 0; rowPointer < fenBlocks.length; rowPointer++) {
            var fieldsColumnPointer = 0;

            for (int columnPointer = 0; columnPointer < fenBlocks[rowPointer].length(); columnPointer++) {
                var fenCode = fenBlocks[rowPointer].charAt(columnPointer);

                // skip by numbers
                if (Character.isDigit(fenCode)) {
                    int parsedFenCode = Character.getNumericValue(fenCode);
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
        if (StringUtils.isBlank(fenString)) {
            return new String[0];
        }

        // only get the pos infos
        if (fenString.contains(" ")) {
            fenString = fenString.substring(0, fenString.indexOf(" "));
        }

        return fenString.split("/");
    }

    private static void validateFenString(String fenString) {
        if (StringUtils.isBlank(fenString)) {
            throw new IllegalArgumentException("FEN string must not be blank");
        }

        var blocks = getFenBlocks(fenString);
        String invalidFenStringPrefix = String.format("Invalid FEN string: [%s] -", fenString);

        // check amount of blocks
        if (blocks.length > 8) {
            throw new IllegalArgumentException(invalidFenStringPrefix + " too much blocks");
        } else if (blocks.length < 8) {
            throw new IllegalArgumentException(invalidFenStringPrefix + " too less blocks");
        }

        // check block content - amount and is piece valid
        for (var block : blocks) {
            var blockMessage = String.format("in block [%s]", block);

            var amountOfPiecesAndEmptyFields = 0;
            for (int i = 0; i < block.length(); i++) {
                var c = block.charAt(i);
                if (Character.isDigit(c)) {
                    int parsedInt = Character.getNumericValue(c);
                    amountOfPiecesAndEmptyFields += parsedInt;
                } else {
                    amountOfPiecesAndEmptyFields++;

                    // check if piece is valid
                    if(!CHARACTER_MAP.containsKey(c)) {
                        throw new IllegalArgumentException(invalidFenStringPrefix + " invalid piece " + blockMessage);
                    }
                }
            }

            if (amountOfPiecesAndEmptyFields < 8) {
                throw new IllegalArgumentException(invalidFenStringPrefix + " too less pieces " + blockMessage);
            } else if (amountOfPiecesAndEmptyFields > 8) {
                throw new IllegalArgumentException(invalidFenStringPrefix + " too many pieces " + blockMessage);
            }
        }
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
                rowList.add((row + col) % 2 == 0 ? WHITE_SQUARE : BLACK_SQUARE);
            }
            boardList.add(rowList);
        }
        return boardList;
    }
}
