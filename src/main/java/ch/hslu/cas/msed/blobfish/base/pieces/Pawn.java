package ch.hslu.cas.msed.blobfish.base.pieces;

public class Pawn implements Piece {

    @Override
    public int getPoint() {
        return 1;
    }

    @Override
    public Character getUIRepresent() {
        return '♟';
    }
}
