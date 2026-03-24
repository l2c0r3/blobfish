package ch.hslu.cas.msed.blobfish.game.provider;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.player.BotAlgorithm;
import ch.hslu.cas.msed.blobfish.minimax.base.PathEvaluation;

public class TestAlgoOneProvider implements BotAlgorithmProvider {
    private static final String ALGORITHM_NAME = "test-algo-one";

    @Override
    public String getAlgorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public TestAlgoOne create(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor) {
        return new TestAlgoOne(calculationDepth, evaluationStrategy, ownPlayerColor);
    }

    public static class TestAlgoOne extends BotAlgorithm {
        public TestAlgoOne(int calculationDepth, EvaluationStrategy evaluationStrategy, PlayerColor ownPlayerColor) {
            super(calculationDepth, evaluationStrategy, ownPlayerColor);
        }

        @Override
        public PathEvaluation getBestPath(ChessBoard chessBoard) {
            return null;
        }
    }
}