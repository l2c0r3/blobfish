package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.player.bot.FirstMoveEvaluation;
import ch.hslu.cas.msed.blobfish.player.bot.PathEvaluation;

import java.util.ArrayList;

public class MoveNodeMapper {

    public FirstMoveEvaluation mapToFirstMoveEvaluation(MoveNode moveNode) {
        if (moveNode == null || moveNode.history() == null) {
            return null;
        } else {
            MoveHistoryNode node = moveNode.history();
            while (node.parent() != null) {
                node = node.parent();
            }
            return new FirstMoveEvaluation(node.move(), moveNode.eval());
        }
    }

    public PathEvaluation mapToPathEvaluation(MoveNode moveNode) {
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
