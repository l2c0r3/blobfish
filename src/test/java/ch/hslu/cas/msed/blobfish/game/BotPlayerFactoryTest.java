package ch.hslu.cas.msed.blobfish.game;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.base.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.game.player.BotPlayerFactory;
import ch.hslu.cas.msed.blobfish.game.provider.TestBotOneProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class BotPlayerFactoryTest {
    @Test
    void getViableBotNames() {
        var expectedBotNames = List.of("test-bot-one.test-algo-one", "test-bot-two.test-algo-two");

        var viableBotNames = BotPlayerFactory.getViableBotNames();

        // contains instead of equals, because it also loads the classes from 'main'
        // and not just 'test'. I haven't found a way to circumvent this :(
        Assertions.assertTrue(viableBotNames.containsAll(expectedBotNames));
    }

    @Test
    void create_success() {
        var expectedBotPlayerClass = TestBotOneProvider.TestBotOne.class;

        var botPlayer = BotPlayerFactory.create("test-bot-one", "test-algo-one", 1, new TestEvaluationStrategy(), PlayerColor.WHITE);

        Assertions.assertEquals(expectedBotPlayerClass, botPlayer.getClass());
    }

    @Test
    void create_wrong_bot_name() {
        var thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> BotPlayerFactory.create("test-bot", "test-algo-one", 1, new TestEvaluationStrategy(), PlayerColor.WHITE));

        Assertions.assertEquals("Unknown bot player: test-bot", thrown.getMessage());
    }

    @Test
    void create_wrong_algo_name() {
        var thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> BotPlayerFactory.create("test-bot-one", "test-algo", 1, new TestEvaluationStrategy(), PlayerColor.WHITE));

        Assertions.assertEquals("Unknown algorithm: test-algo", thrown.getMessage());
    }

    @Test
    void create_wrong_combination() {
        var thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> BotPlayerFactory.create("test-bot-one", "test-algo-two", 1, new TestEvaluationStrategy(), PlayerColor.WHITE));

        Assertions.assertEquals("Unsupported bot player + algorithm combination", thrown.getMessage());
    }

    private static class TestEvaluationStrategy implements EvaluationStrategy {

        @Override
        public int getEvaluation(ChessBoard board) {
            return 0;
        }
    }
}