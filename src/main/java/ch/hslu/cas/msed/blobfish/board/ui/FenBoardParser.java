package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PieceType;
import org.apache.commons.lang3.StringUtils;

class FenBoardParser {

    UiBoard parse(String fen) {
        validateFenString(fen);
        var fenBlocks = getFenBlocks(fen);
        return parseFenBlocksAsBoard(fenBlocks);
    }

    private static UiBoard parseFenBlocksAsBoard(String[] fenBlocks) {
        var board = new UiBoard();

        // replace empty blocks by characters
        for (int rowIndex = 0; rowIndex < fenBlocks.length; rowIndex++) {
            var fenBlock = fenBlocks[rowIndex];
            var colIndex = 0;

            for (int fenBlockIndex = 0; fenBlockIndex < fenBlock.length(); fenBlockIndex++) {
                var fenCode = fenBlock.charAt(fenBlockIndex);

                // empty field by numbers
                if (Character.isDigit(fenCode)) {
                    int amountOfEmptyFields = Character.getNumericValue(fenCode);
                    for (int i = 0; i < amountOfEmptyFields; i++) {
                        var squareColor = getSquareColor(rowIndex, colIndex);
                        var uiField = new UiField(null, squareColor);
                        board.set(rowIndex, colIndex, uiField);
                        colIndex++;
                    }
                } else {
                    // replace pieces in list
                    var squareColor = getSquareColor(rowIndex, colIndex);
                    var piece = new Piece(fenCode);
                    var uiField = new UiField(piece, squareColor);
                    board.set(rowIndex, colIndex, uiField);
                    colIndex++;
                }
            }
        }
        return board;
    }

    private static PlayerColor getSquareColor(int rowIndex, int colIndex) {
        return (rowIndex + colIndex) % 2 == 0 ? PlayerColor.WHITE : PlayerColor.BLACK;
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
                        PieceType.fromFen(c);
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
}
