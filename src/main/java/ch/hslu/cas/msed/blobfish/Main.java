package ch.hslu.cas.msed.blobfish;

import ch.hslu.cas.msed.blobfish.game.ChessGame;

import java.io.IOException;

public class Main {

    static void main() throws IOException {
        ChessGame game = new ChessGame();
        game.start();
    }
}