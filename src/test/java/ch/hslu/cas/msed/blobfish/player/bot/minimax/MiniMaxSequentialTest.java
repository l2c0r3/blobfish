package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.CompositeEvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MiniMaxAlgo;

class MiniMaxSequentialTest extends AbstractMiniMaxTest {

    @Override
    MiniMaxAlgo getTestee(PlayerColor playerColor) {
        var evalStrategy = CompositeEvalStrategy.builder().add(new MateAwareEval()).add(new MaterialEval()).build();
        return new MiniMaxSequential(3, evalStrategy, playerColor);
    }

}