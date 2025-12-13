package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.Color;
import ch.hslu.cas.msed.blobfish.base.Piece;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessBoardAsciiPrinter {


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
        var boardStr = mapBoardToString(board);


        System.out.println(boardStr);
        return boardStr;
    }

    private static ArrayList<List<UiFieldPrinter>> getFenBlocksAsBoard(String[] fenBlocks) {
        var board = new ArrayList<List<UiFieldPrinter>>();

        // replace empty blocks by characters
        for (int rowIndex = 0; rowIndex < fenBlocks.length; rowIndex++) {
            var rowList = new ArrayList<UiFieldPrinter>();
            var fenBlock = fenBlocks[rowIndex];
            var colIndex = 0;

            for (int fenBlockIndex = 0; fenBlockIndex < fenBlock.length(); fenBlockIndex++) {
                var fenCode = fenBlock.charAt(fenBlockIndex);
                var squareColor = (rowIndex + colIndex) % 2 == 0 ? Color.WHITE : Color.BLACK;

                // empty field by numbers
                if (Character.isDigit(fenCode)) {
                    int amountOfEmptyFields = Character.getNumericValue(fenCode);
                    for (int i = 0; i < amountOfEmptyFields; i++) {
                        var square = new UiFieldPrinter(null, null, squareColor);
                        rowList.add(square);
                        colIndex++;
                    }
                } else {
                    // replace pieces in list
                    var pieceToSet = new UiFieldPrinter(fenCode, squareColor);
                    rowList.add(pieceToSet);
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
                    try {
                        Piece.fromFen(c);
                    } catch (Exception e) {
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

    private static String mapBoardToString(List<List<UiFieldPrinter>> fields) {
        return fields.stream()
                .map(row -> row.stream()
                        .map(UiFieldPrinter::print)
                        .collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
    }
}
