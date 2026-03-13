package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import ch.hslu.cas.msed.blobfish.player.bot.FirstMoveEvaluation;

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
}
