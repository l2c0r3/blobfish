package ch.hslu.cas.msed.blobfish.game.screen;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.game.InputReader;
import ch.hslu.cas.msed.blobfish.game.MatchConfig;
import ch.hslu.cas.msed.blobfish.game.OutputWriter;
import ch.hslu.cas.msed.blobfish.player.BotPlayer;
import ch.hslu.cas.msed.blobfish.player.HumanPlayer;

public class HomeScreen {
    OutputWriter writer;
    private final InputReader reader;

    public HomeScreen(InputReader reader, OutputWriter writer) {
        this.writer = writer;
        this.reader = reader;
    }

    public MatchConfig getMatchConfig() {
        writer.println("=== Start Screen ===");
        writer.println("The game can always be closed with \"exit\"");
        writer.println("A match can always be aborted with \"quit\"");
        writer.flush();

        var humanPlayerColor = getPlayerColor();

        return switch (humanPlayerColor) {
            case WHITE -> new MatchConfig(new HumanPlayer(PlayerColor.WHITE, reader), new BotPlayer(PlayerColor.BLACK));
            case BLACK -> new MatchConfig(new BotPlayer(PlayerColor.WHITE), new HumanPlayer(PlayerColor.BLACK, reader));
        };
    }

    private PlayerColor getPlayerColor() {
        PlayerColor playerColor;
        do {
            var input = reader.readLine("Choose a color to start a match [white|black]:");

            try {
                playerColor = PlayerColor.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                writer.printlnAndFlush("Invalid input.");
                playerColor = null;
            }
        } while (playerColor == null);

        return playerColor;
    }
}
