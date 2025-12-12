package ch.hslu.cas.msed.blobfish.base.pieces;

public class Rook implements Piece {

    @Override
    public int getPoint() {
        return 5;
    }

    @Override
    public Character getUIRepresent() {
        return '♜';
    }
}
