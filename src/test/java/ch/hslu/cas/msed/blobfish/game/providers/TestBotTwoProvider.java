package ch.hslu.cas.msed.blobfish.game.providers;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.game.exceptions.GameAbortedException;
import ch.hslu.cas.msed.blobfish.game.exceptions.MatchAbortedException;
import ch.hslu.cas.msed.blobfish.player.bot.BotPlayer;

public class TestBotTwoProvider implements BotPlayerProvider<TestAlgoTwoProvider.TestAlgoTwo> {
    private static final String BOT_NAME = "test-bot-two";

    @Override
    public String getBotName() {
        return BOT_NAME;
    }

    @Override
    public Class<TestAlgoTwoProvider.TestAlgoTwo> algorithmType() {
        return TestAlgoTwoProvider.TestAlgoTwo.class;
    }

    @Override
    public BotPlayer create(PlayerColor playerColor, TestAlgoTwoProvider.TestAlgoTwo botAlgorithm) {
        return new TestBotTwo(playerColor, botAlgorithm);
    }

    public static class TestBotTwo extends BotPlayer {
        public TestBotTwo(PlayerColor playerColor, TestAlgoTwoProvider.TestAlgoTwo botAlgorithm) {
            super(playerColor, botAlgorithm);
        }

        @Override
        public String getNextMove(ChessBoard board) throws MatchAbortedException, GameAbortedException {
            return "";
        }
    }

}