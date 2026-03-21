package ch.hslu.cas.msed.blobfish.base.exception;

import lombok.Getter;

@Getter
public class InvalidMoveException extends RuntimeException {
    private final String originalMove;

    public InvalidMoveException(String originalMove, String message) {
        super(message);
        this.originalMove = originalMove;
    }
}
