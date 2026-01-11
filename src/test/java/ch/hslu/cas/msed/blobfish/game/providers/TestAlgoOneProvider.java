package ch.hslu.cas.msed.blobfish.game.providers;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.player.bot.BotAlgorithm;

public class TestAlgoOneProvider implements BotAlgorithmProvider {
    private static final String ALGORITHM_NAME = "test-algo-one";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public TestAlgoOne create(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
        return new TestAlgoOne(calculationDepth, evalStrategy, ownPlayerColor);
    }

    public static class TestAlgoOne extends BotAlgorithm {
        public TestAlgoOne(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor) {
            super(calculationDepth, evalStrategy, ownPlayerColor);
        }

        @Override
        public String getNextBestMove(ChessBoard chessBoard) {
            return "";
        }
    }
}