package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniMaxTestSequentialTest extends AbstractMiniMaxTest {

    @Override
    MiniMaxAlgo getTestee(PlayerColor playerColor) {
        return new MiniMaxSequential(3, new MateAwareEval(new MaterialEval()), playerColor);
    }

}