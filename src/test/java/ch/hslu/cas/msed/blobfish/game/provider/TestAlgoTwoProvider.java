package ch.hslu.cas.msed.blobfish.game.provider;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.game.player.BotAlgorithmProvider;
import ch.hslu.cas.msed.blobfish.player.BotAlgorithm;
import ch.hslu.cas.msed.blobfish.minimax.base.PathEvaluation;

public class TestAlgoTwoProvider implements BotAlgorithmProvider {
    private static final String ALGORITHM_NAME = "test-algo-two";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public TestAlgoTwo create(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor) {
        return new TestAlgoTwo(calculationDepth, evaluationStrategy, ownPlayerColor);
    }

    public static class TestAlgoTwo extends BotAlgorithm {
        public TestAlgoTwo(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor) {
            super(calculationDepth, evaluationStrategy, ownPlayerColor);
        }

        @Override
        public PathEvaluation getBestPath(ChessBoard chessBoard) {
            return null;
        }
    }
}