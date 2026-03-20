package ch.hslu.cas.msed.blobfish.player.bot;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.eval.EvaluationStrategy;
import ch.hslu.cas.msed.blobfish.minimax.base.PathEvaluation;
import lombok.Getter;

@Getter
public abstract class BotAlgorithm {

    private final int calculationDepth;
    private final EvaluationStrategy evaluationStrategy;
    private final PlayerColor ownPlayerColor;

    public BotAlgorithm(final int calculationDepth, final EvaluationStrategy evaluationStrategy, final PlayerColor ownPlayerColor) {
        if (calculationDepth < 0) throw new IllegalArgumentException("calculationDepth cannot be negative");
        this.calculationDepth = calculationDepth;
        this.evaluationStrategy = evaluationStrategy;
        this.ownPlayerColor = ownPlayerColor;
    }

    public abstract PathEvaluation getBestPath(final ChessBoard chessBoard);

}
