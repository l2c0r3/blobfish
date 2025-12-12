package ch.hslu.cas.msed.blobfish.base.pieces;

public class Bishop implements Piece {

    @Override
    public int getPoint() {
        return 3;
    }

    @Override
    public Character getUIRepresent() {
        return '♝';
    }

    @Override
    public Character getFENRepresent() {
        return 'b';
    }
}
