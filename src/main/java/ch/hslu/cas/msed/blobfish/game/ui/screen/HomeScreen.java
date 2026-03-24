package ch.hslu.cas.msed.blobfish.game.ui.screen;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.*;
import ch.hslu.cas.msed.blobfish.game.player.BotPlayerFactory;
import ch.hslu.cas.msed.blobfish.game.InputReader;
import ch.hslu.cas.msed.blobfish.game.MatchConfig;
import ch.hslu.cas.msed.blobfish.game.OutputWriter;
import ch.hslu.cas.msed.blobfish.game.player.HumanCliPlayer;
import ch.hslu.cas.msed.blobfish.player.BotPlayer;

public class HomeScreen {
    private final OutputWriter writer;
    private final InputReader reader;

    private static final int CALC_DEPTH = 4;

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
        var humanPlayer = new HumanCliPlayer(humanPlayerColor, reader);

        var botPlayerColor = PlayerColor.WHITE.equals(humanPlayerColor) ? PlayerColor.BLACK : PlayerColor.WHITE;
        var botPlayerEvalStrategy = CompositeEvaluationStrategy.builder().add(new MateAwareEvaluation()).add(new MaterialEvaluation()).add(new PieceSquareEvaluation()).build();
        var botPlayer = getBotPlayer(CALC_DEPTH, botPlayerEvalStrategy, botPlayerColor);

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

    private BotPlayer getBotPlayer(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor playerColor) {
        BotPlayer botPlayer;
        do {
            var viableBotNames = String.join("|", BotPlayerFactory.getViableBotNames());
            var input = reader.readLine("Choose an opponent [" + viableBotNames + "]:");

            try {
                var parts = input.split("\\.");
                if (parts.length != 2) {
                    writer.printlnAndFlush("Invalid bot name: " + input);
                    botPlayer = null;
                    continue;
                }

                var botName = parts[0];
                var algorithmName = parts[1];

                botPlayer = BotPlayerFactory.create(botName, algorithmName, calculationDepth, evaluationStrategy, playerColor);
            } catch (IllegalArgumentException e) {
                writer.printlnAndFlush("Invalid input.");
                botPlayer = null;
            }
        } while (botPlayer == null);

        return botPlayer;
    }
}
