package ch.hslu.cas.msed.blobfish.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.evaluation.CompositeEvaluationStrategy;
import ch.hslu.cas.msed.blobfish.evaluation.MateAwareEvaluation;
import ch.hslu.cas.msed.blobfish.evaluation.MaterialEvaluation;
import ch.hslu.cas.msed.blobfish.minimax.base.MiniMaxAlgo;
import ch.hslu.cas.msed.blobfish.minimax.cached.base.MiniMaxSequentialWithCache;

class MiniMaxSequentialWithCacheTest extends AbstractMiniMaxTest {

    @Override
    MiniMaxAlgo getTestee(PlayerColor playerColor) {
        var evalStrategy = CompositeEvaluationStrategy.builder().add(new MateAwareEvaluation()).add(new MaterialEvaluation()).build();
        return new MiniMaxSequentialWithCache(3, evalStrategy, playerColor);
    }
}