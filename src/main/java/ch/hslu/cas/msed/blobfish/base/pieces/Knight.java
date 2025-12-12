package ch.hslu.cas.msed.blobfish.base.pieces;

public class Knight implements Piece {

    @Override
    public int getPoint() {
        return 3;
    }

    @Override
    public Character getUIRepresent() {
        return '♞';
    }
}
