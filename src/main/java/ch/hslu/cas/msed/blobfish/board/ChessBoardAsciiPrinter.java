package ch.hslu.cas.msed.blobfish.board;

import ch.hslu.cas.msed.blobfish.base.Piece;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChessBoardAsciiPrinter {

    @Getter
    private enum UiPiece {

        KING(Piece.KING, '♚'),
        QUEEN(Piece.QUEEN, '♛'),
        ROOK(Piece.ROOK, '♜'),
        BISHOP(Piece.BISHOP, '♝'),
        KNIGHT(Piece.KNIGHT, '♞'),
        PAWN(Piece.PAWN, '♟');

        private final Piece piece;
        private final Character uiRepresent;

        UiPiece(Piece piece, Character uiRepresent) {
            this.piece = piece;
            this.uiRepresent = uiRepresent;
        }
    }

    public static final char WHITE_KING = '♚';
    public static final char WHITE_QUEEN = '♛';
    public static final char WHITE_ROOK = '♜';
    public static final char WHITE_BISHOP = '♝';
    public static final char WHITE_KNIGHT = '♞';
    public static final char WHITE_PAWN = '♟';
    public static final char WHITE_SQUARE = '■';

    public static final char BLACK_KING = '♔';
    public static final char BLACK_QUEEN = '♕';
    public static final char BLACK_ROOK = '♖';
    public static final char BLACK_BISHOP = '♗';
    public static final char BLACK_KNIGHT = '♘';
    public static final char BLACK_PAWN = '♙';
    public static final char BLACK_SQUARE = '□';

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

    private ChessBoardAsciiPrinter() {
        // utility class
    }

    /**
     * @param fenString position of board
     * @return the position of the chessboard in a String with Ascii Characters
     */
    public static String displayBoardAscii(String fenString) {
        validateFenString(fenString);

        var fenBlocks = getFenBlocks(fenString);
        var board = getFenBlocksAsBoard(fenBlocks);
        var boardWithPadding = addPaddingToBoard(board);
        var boardStr = mapBoardToString(boardWithPadding);


        System.out.println(boardStr);
        return boardStr;
    }

    private static ArrayList<List<String>> addPaddingToBoard(ArrayList<List<Character>> board) {
        final Character whiteSpace = ' ';
        Predicate<Character> isPieceASquare = p -> p.equals(WHITE_SQUARE) || p.equals(BLACK_SQUARE);

        var boardWithPadding = new ArrayList<List<String>>();

        board.forEach(row -> {
            var rowWithPadding = new ArrayList<String>();

            for (int i = 0; i < row.size(); i++) {
                StringBuilder fieldAsString = new StringBuilder();
                var piece = row.get(i);

                if (i == 0) {
                    fieldAsString.append(whiteSpace);
                }

                fieldAsString.append(piece);

                // double squares
                if (isPieceASquare.test(piece)) {
                    fieldAsString.append(piece);
                }

                if (i != row.size() - 1) {
                    fieldAsString.append(whiteSpace);
                }
                rowWithPadding.add(fieldAsString.toString());
            }
            boardWithPadding.add(rowWithPadding);
        });

        return boardWithPadding;
    }

    private static ArrayList<List<Character>> getFenBlocksAsBoard(String[] fenBlocks) {
        var board = new ArrayList<List<Character>>();

        // replace empty blocks by characters
        for (int rowIndex = 0; rowIndex < fenBlocks.length; rowIndex++) {
            var rowList = new ArrayList<Character>();
            var fenBlock = fenBlocks[rowIndex];
            var colIndex = 0;

            for (int fenBlockIndex = 0; fenBlockIndex < fenBlock.length(); fenBlockIndex++) {
                var fenCode = fenBlock.charAt(fenBlockIndex);

                // empty field by numbers
                if (Character.isDigit(fenCode)) {
                    int amountOfEmptyFields = Character.getNumericValue(fenCode);
                    for (int i = 0; i < amountOfEmptyFields; i++) {
                        var square = (rowIndex + colIndex) % 2 == 0 ? WHITE_SQUARE : BLACK_SQUARE;
                        rowList.add(square);
                        colIndex++;
                    }
                } else {
                    // replace pieces in list
                    var pieceToSet = CHARACTER_MAP.get(fenCode);
                    rowList.add(pieceToSet);
                    colIndex++;
                }
            }
            board.add(rowList);
        }
        return board;
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
                    if (!CHARACTER_MAP.containsKey(c)) {
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

    private static String mapBoardToString(List<List<String>> fields) {
        return fields.stream()
                .map(row -> row.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
    }
}
