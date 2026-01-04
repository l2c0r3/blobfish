package ch.hslu.cas.msed.blobfish.board;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.List;

public class ChessBoard {

    private final static String STARTPOSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final Board board;

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
    }

    private ChessBoard(Board board) {
        this.board = board;
    }

    /**
     * Do move with SAN annotation. e.g Nc6
     */
    public ChessBoard doMove(String san) {
        var copyOfBoard = new Board();
        copyOfBoard.loadFromFen(this.board.getFen());
        copyOfBoard.doMove(san);
        return new ChessBoard(copyOfBoard);
    }

    /**
     * Return the list of all possible legal moves
     *
     * @return the list of legal Moves
     */
    public List<Move> legalMoves() {
        return board.legalMoves();
    }

    /***
     * Verifies if the move still to be executed will leave the resulting board in a valid (legal) position.
     * @param san the SAN representation of a move
     * @return whether the move is legal
     */
    public boolean isMoveLegal(String san) {
        MoveList moves = new MoveList(board.getFen());
        try {
            moves.addSanMove(san);
        } catch (RuntimeException e) {
            return false;
        }
        return board.isMoveLegal(moves.getLast(), true);
    }

    public boolean isGameOver() {
        return board.legalMoves().isEmpty();
    }

    public String getFen() {
        return board.getFen();
    }
}
