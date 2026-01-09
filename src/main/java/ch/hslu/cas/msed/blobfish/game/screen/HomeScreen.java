package ch.hslu.cas.msed.blobfish.game.screen;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;
import ch.hslu.cas.msed.blobfish.game.InputReader;
import ch.hslu.cas.msed.blobfish.game.MatchConfig;
import ch.hslu.cas.msed.blobfish.game.OutputWriter;
import ch.hslu.cas.msed.blobfish.player.HumanPlayer;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxBotPlayer;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxSequential;

public class HomeScreen {
    private final OutputWriter writer;
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
        var humanPlayer = new HumanPlayer(humanPlayerColor, reader);

        var botPlayerColor = PlayerColor.WHITE.equals(humanPlayerColor) ? PlayerColor.BLACK : PlayerColor.WHITE;
        var miniMaxAlgo = new MiniMaxSequential(4, new MateAwareEval(new MaterialEval()), botPlayerColor);
        var botPlayer = new MiniMaxBotPlayer(botPlayerColor, miniMaxAlgo);

        return switch (humanPlayerColor) {
            case WHITE -> new MatchConfig(humanPlayer, botPlayer);
            case BLACK -> new MatchConfig(botPlayer, humanPlayer);
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
