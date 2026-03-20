package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.evaluation.CompositeEvalStrategy;
import ch.hslu.cas.msed.blobfish.evaluation.MateAwareEval;
import ch.hslu.cas.msed.blobfish.evaluation.MaterialEval;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MiniMaxAlgo;

class MiniMaxAlphaBetaSequentialTest extends AbstractMiniMaxTest {

    @Override
    MiniMaxAlgo getTestee(PlayerColor playerColor) {
        var evalStrategy = CompositeEvalStrategy.builder().add(new MateAwareEval()).add(new MaterialEval()).build();
        return new MiniMaxAlphaBetaSequential(3, evalStrategy, playerColor);
    }

}