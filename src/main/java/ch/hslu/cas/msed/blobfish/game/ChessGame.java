package ch.hslu.cas.msed.blobfish.game;

import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.game.exceptions.GameAbortedException;
import ch.hslu.cas.msed.blobfish.game.screen.HomeScreen;
import ch.hslu.cas.msed.blobfish.game.screen.MatchScreen;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

public class ChessGame {
    private final Terminal terminal;

    public ChessGame() throws IOException {
        terminal = TerminalBuilder.builder().system(true).build();
    }

    public void start() {
        final OutputWriter writer = new OutputWriter(terminal);
        final InputReader reader = new InputReader(terminal, new DefaultParser());

        while (true) {
            try {
                var homeScreen = new HomeScreen(reader, writer);
                var matchScreenConfig = homeScreen.getMatchConfig();

                var matchScreen = new MatchScreen(matchScreenConfig.white(), matchScreenConfig.black(), new ChessBoard(), writer);
                matchScreen.start();
            } catch (GameAbortedException e) {
                writer.printlnAndFlush("Thanks for playing!");
                break;
            }
        }
    }
}
