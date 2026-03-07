package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.EvalWrapper;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;

import java.util.List;

class MiniMaxAlphaBetaSequentialTest extends AbstractMiniMaxTest {

    @Override
    MiniMaxAlgo getTestee(PlayerColor playerColor) {
        return new MiniMaxAlphaBetaSequential(3, new EvalWrapper(List.of(new MateAwareEval(), new MateAwareEval())), playerColor);
    }

}