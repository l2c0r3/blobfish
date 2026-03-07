package ch.hslu.cas.msed.blobfish.board;

import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.util.Arrays;
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
     *
     * @return returns a new instance of the Chessboard with the new position
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

    /**
     * Retrieves the current position on the board as an array.
     * The first 8 entries are the squares A1-H1, the second 8 are the squares A2-H2 and so on.
     * On squares where there are no pieces, the values are null.
     *
     * @return an array containing the pieces
     */
    public Piece[] boardToArray() {
        var boardArray = board.boardToArray();
        // for some reason the original array has a length of 65 and the last piece is always empty, so we remove the superficial entry.
        var newBoardArray = Arrays.copyOf(boardArray, 64);
        return Arrays.stream(newBoardArray)
                .map(com.github.bhlangonijr.chesslib.Piece::getFenSymbol)
                .flatMapToInt(String::chars)
                .mapToObj(c -> (char) c)
                .map(c -> {
                    if (c.toString().equals(com.github.bhlangonijr.chesslib.Piece.NONE.getFenSymbol())) {
                        return null;
                    } else {
                        return new Piece(c);
                    }
                })
                .toArray(Piece[]::new);
    }

    /**
     * Determines if the position on the board is in the endgame stage.
     *
     * @return whether the position on the board is in the endgame stage.
     * @implNote It is considered to be endgame, when both players combined have no more than 6 pieces excluding pawns and kings.
     * This is the same as the <a href="https://github.com/lichess-org/scalachess/blob/master/core/src/main/scala/Divider.scala">lichess</a> implementation.
     */
    public boolean isEndGame() {
        var numberOfPieces = Arrays.stream(board.boardToArray())
                .filter(p -> p.getPieceType() != null)
                .filter(p -> switch (p.getPieceType()) {
                    case KNIGHT, BISHOP, ROOK, QUEEN -> true;
                    default -> false;
                }).count();

        return numberOfPieces <= 6;
    }

    public boolean isCapture(final Move move) {
        return board.getPiece(move.getTo()) != com.github.bhlangonijr.chesslib.Piece.NONE || (
                board.getEnPassant() != Square.NONE
                        && board.getPiece(move.getFrom()).getPieceType() == PieceType.PAWN
                        && move.getTo() == board.getEnPassant()
        );
    }

    public boolean isGameOver() {
        return board.legalMoves().isEmpty();
    }

    public String getFen() {
        return board.getFen();
    }

    public boolean isMated() {
        return this.board.isMated();
    }

    public PlayerColor getSideToMove() {
        return switch (board.getSideToMove()) {
            case WHITE -> PlayerColor.WHITE;
            case BLACK -> PlayerColor.BLACK;
        };
    }
}
