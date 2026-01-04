package ch.hslu.cas.msed.blobfish.board.ui;

import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.FenUtil;

class FenUiBoardParser {

    UiBoard parse(String fen) {
        FenUtil.validateFenString(fen);
        var fenBlocks = FenUtil.getFenPositionBlocks(fen);
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

    private static SquareColor getSquareColor(int rowIndex, int colIndex) {
        return (rowIndex + colIndex) % 2 == 0 ? SquareColor.LIGHT : SquareColor.DARK;
    }

}
