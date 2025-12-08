package ch.hslu.cas.msed.blobfish.game;

import ch.hslu.cas.msed.blobfish.game.exceptions.GameAbortedException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Parser;
import org.jline.terminal.Terminal;

public class InputReader {
    private final LineReader reader;

    public InputReader(Terminal terminal, Parser parser) {
        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(parser)
                .build();
    }

    public String readLine(String prompt) throws GameAbortedException {
        var input = reader.readLine(prompt).trim().toLowerCase();

        if (input.equals("exit")) {
            throw new GameAbortedException("Game aborted by user.");
        }

        return input;
    }
}
