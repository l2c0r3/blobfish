package ch.hslu.cas.msed.blobfish.board;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import com.github.bhlangonijr.chesslib.Board;

public class ChessBoard {

    private final static String STARTPOSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final Board board;
    private final EvalBar evalBar;

    /**
     * Starts a chessboard with standard start position
     */
    public ChessBoard() {
        this(STARTPOSITION_FEN);
    }

    /**
     * Load a FEN position into the chessboard.
     */
    public ChessBoard(String fen) {
        board = new Board();
        board.loadFromFen(fen);

        evalBar = new EvalBar(fen);
    }

    /**
     * Do move with SAN annotation. e.g Nc6
     */
    public ChessBoard doMove(String san) {
        board.doMove(san);
        var newBoardPosition = board.getFen();
        return new ChessBoard(newBoardPosition);
    }

    public int getEvaluation() {
        return this.evalBar.getEvaluation();
    }

    /**
     * @param perspective which color is at the bottom
     * @return the position of the chessboard in a String with Ascii Characters
     */
    public String displayBoardAscii(PlayerColor perspective) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
