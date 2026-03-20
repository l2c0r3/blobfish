package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.player.bot.PathEvaluation;

import java.util.ArrayList;

public class MoveNodeMapper {

    public PathEvaluation mapToPathEvaluation(final MoveNode moveNode) {
        if (moveNode == null || moveNode.history() == null) {
            return null;
        } else {
            var path = new ArrayList<String>();

            var historyNode = moveNode.history();
            do {
                path.add(historyNode.move());
                historyNode = historyNode.parent();
            } while (historyNode != null);

            return new PathEvaluation(path.reversed(), moveNode.eval());
        }
    }
}
