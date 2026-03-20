package ch.hslu.cas.msed.blobfish.player.bot.minimax.chached;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.CompositeEvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.AbstractMiniMaxTest;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.base.MiniMaxAlgo;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.cached.MiniMaxAlphaBetaSequentialWithCache;

class MiniMaxAlphaBetaSequentialWithCacheTest extends AbstractMiniMaxTest {

    @Override
    MiniMaxAlgo getTestee(PlayerColor playerColor) {
        var evalStrategy = CompositeEvalStrategy.builder().add(new MateAwareEval()).add(new MaterialEval()).build();
        return new MiniMaxAlphaBetaSequentialWithCache(3, evalStrategy, playerColor);
    }

}