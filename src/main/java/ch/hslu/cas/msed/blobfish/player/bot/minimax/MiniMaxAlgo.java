package ch.hslu.cas.msed.blobfish.player.bot;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.board.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import lombok.Getter;

@Getter
public abstract class MiniMaxAlgo {

    private final int calculationDepth;
    private final EvalStrategy evalStrategy;
    private final PlayerColor ownPlayerColor;

    public MiniMaxAlgo(int calculationDepth, EvalStrategy evalStrategy, PlayerColor ownPlayerColor){
        this.calculationDepth = calculationDepth;
        this.evalStrategy = evalStrategy;
        this.ownPlayerColor = ownPlayerColor;
    }

    public abstract String getBestNextMove(ChessBoard chessBoard);

}
