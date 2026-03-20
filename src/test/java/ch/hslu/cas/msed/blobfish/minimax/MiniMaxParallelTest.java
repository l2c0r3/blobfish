package ch.hslu.cas.msed.blobfish.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.evaluation.CompositeEvalStrategy;
import ch.hslu.cas.msed.blobfish.evaluation.MateAwareEval;
import ch.hslu.cas.msed.blobfish.evaluation.MaterialEval;
import ch.hslu.cas.msed.blobfish.minimax.base.MiniMaxAlgo;

class MiniMaxParallelTest extends AbstractMiniMaxTest {

    @Override
    MiniMaxAlgo getTestee(PlayerColor playerColor) {
        var evalStrategy = CompositeEvalStrategy.builder().add(new MateAwareEval()).add(new MaterialEval()).build();
        return new MiniMaxParallel(3, evalStrategy, playerColor);
    }

}