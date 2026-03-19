package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.CompositeEvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;

class MiniMaxSequentialWithCacheTest extends AbstractMiniMaxTest {

    @Override
    MiniMaxAlgo getTestee(PlayerColor playerColor) {
        var evalStrategy = CompositeEvalStrategy.builder().add(new MateAwareEval()).add(new MaterialEval()).build();
        return new MiniMaxSequentialWithCache(3, evalStrategy, playerColor);
    }
}