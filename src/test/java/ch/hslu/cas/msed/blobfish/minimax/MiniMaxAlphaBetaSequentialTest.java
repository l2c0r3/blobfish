package ch.hslu.cas.msed.blobfish.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.CompositeEvaluationStrategy;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEvaluation;
import ch.hslu.cas.msed.blobfish.eval.MaterialEvaluation;
import ch.hslu.cas.msed.blobfish.minimax.base.MiniMaxAlgo;

class MiniMaxAlphaBetaSequentialTest extends AbstractMiniMaxTest {

    @Override
    MiniMaxAlgo getTestee(PlayerColor playerColor) {
        var evalStrategy = CompositeEvaluationStrategy.builder().add(new MateAwareEvaluation()).add(new MaterialEvaluation()).build();
        return new MiniMaxAlphaBetaSequential(3, evalStrategy, playerColor);
    }

}