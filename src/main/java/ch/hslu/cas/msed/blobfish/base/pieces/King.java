package ch.hslu.cas.msed.blobfish.base.pieces;

public class King implements Piece {

    @Override
    public int getPoint() {
        return 100;
    }

    @Override
    public Character getUIRepresent() {
        return '♚';
    }
}
