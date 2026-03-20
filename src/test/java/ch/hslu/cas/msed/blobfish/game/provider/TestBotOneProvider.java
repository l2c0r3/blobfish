package ch.hslu.cas.msed.blobfish.game.provider;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.game.exception.GameAbortedException;
import ch.hslu.cas.msed.blobfish.game.exception.MatchAbortedException;
import ch.hslu.cas.msed.blobfish.player.bot.BotPlayer;

public class TestBotOneProvider implements BotPlayerProvider<TestAlgoOneProvider.TestAlgoOne> {
    private static final String BOT_NAME = "test-bot-one";

    @Override
    public String getBotName() {
        return BOT_NAME;
    }

    @Override
    public Class<TestAlgoOneProvider.TestAlgoOne> algorithmType() {
        return TestAlgoOneProvider.TestAlgoOne.class;
    }

    @Override
    public BotPlayer create(PlayerColor playerColor, TestAlgoOneProvider.TestAlgoOne botAlgorithm) {
        return new TestBotOne(playerColor, botAlgorithm);
    }

    public static class TestBotOne extends BotPlayer {
        public TestBotOne(PlayerColor playerColor, TestAlgoOneProvider.TestAlgoOne botAlgorithm) {
            super(playerColor, botAlgorithm);
        }

        @Override
        public String getNextMove(ChessBoard board) throws MatchAbortedException, GameAbortedException {
            return "";
        }
    }

}