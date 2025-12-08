package ch.hslu.cas.msed.blobfish.game.exceptions;

public class GameAbortedException extends RuntimeException {
    public GameAbortedException(String message) {
        super(message);
    }
}
