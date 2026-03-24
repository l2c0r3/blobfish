package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.base.ChessBoard;
import ch.hslu.cas.msed.blobfish.base.FenUtil;
import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class MaterialEvaluation implements EvaluationStrategy {

    /**
     * A positive number (e.g., +100) means White has an advantage equal of a pawn;
     * a negative number (e.g., -200) means Black has the edge.
     */
    @Override
    public int getEvaluation(final ChessBoard board) {
        return parseFen(board.getFen()).stream()
                .mapToInt(p -> PlayerColor.WHITE.equals(p.color()) ? p.materialPoints() : -p.materialPoints())
                .sum();
    }

    private List<Piece> parseFen(final String fen) {
        FenUtil.validateFenString(fen);
        var posBlocks = FenUtil.getFenPositionBlocks(fen);

        return Arrays.stream(posBlocks)
                .map(this::removeEmptyFieldsFromPosition)
                .filter(StringUtils::isNotBlank)
                .flatMapToInt(String::chars)
                .mapToObj(c -> (char) c)
                .map(Piece::new)
                .toList();
    }

    private String removeEmptyFieldsFromPosition(final String fenBlock) {
        return fenBlock.replaceAll("\\d", "");
    }
}
