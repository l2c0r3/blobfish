package ch.hslu.cas.msed.blobfish.base.pieces;

public class Queen implements Piece {

    @Override
    public int getPoint() {
        return 9;
    }

    @Override
    public Character getUIRepresent() {
        return '♛';
    }
}
