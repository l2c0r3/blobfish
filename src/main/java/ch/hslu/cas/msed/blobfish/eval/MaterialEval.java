package ch.hslu.cas.msed.blobfish.eval;

import ch.hslu.cas.msed.blobfish.base.FenUtil;
import ch.hslu.cas.msed.blobfish.base.Piece;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class MaterialEval implements EvalStrategy {

    /**
     * A positive number (e.g., +1.0) means White has an advantage equal of a pawn;
     * a negative number (e.g., -2.0) means Black has the edge.
     */
    @Override
    public double getEvaluation(String positionFen) {
        return parseFen(positionFen).stream()
                .mapToInt(p -> PlayerColor.WHITE.equals(p.color()) ? p.materialPoints() : -p.materialPoints())
                .sum();
    }

    private List<Piece> parseFen(String fen) {
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

    private String removeEmptyFieldsFromPosition(String fenBlock) {
        return  fenBlock.replaceAll("\\d", "");
    }
}
