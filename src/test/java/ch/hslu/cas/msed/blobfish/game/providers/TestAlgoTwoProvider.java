package ch.hslu.cas.msed.blobfish.game.providers;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.BotAlgorithm;

public class TestAlgoTwoProvider implements BotAlgorithmProvider {
    private static final String ALGORITHM_NAME = "test-algo-two";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public TestAlgoTwo create(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        return new TestAlgoTwo(calculationDepth, evalStrategy, ownPlayerColor);
    }

    public static class TestAlgoTwo extends BotAlgorithm {
        public TestAlgoTwo(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
            super(calculationDepth, evalStrategy, ownPlayerColor);
        }

        @Override
        public String getNextBestMove(ChessBoard chessBoard) {
            return "";
        }
    }
}