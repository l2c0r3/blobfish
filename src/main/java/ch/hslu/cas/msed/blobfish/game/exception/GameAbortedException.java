package ch.hslu.cas.msed.blobfish.game.exception;

public class GameAbortedException extends RuntimeException {
    public GameAbortedException(String message) {
        super(message);
    }
}
