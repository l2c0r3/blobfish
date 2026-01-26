package ch.hslu.cas.msed.blobfish.player.bot.minimax;

public record MoveNode(double eval, MoveHistoryNode history) {
    public String firstMove() {
        if (history == null) return null;

        MoveHistoryNode node = history;
        while (node.parent() != null) {
            node = node.parent();
        }

        return node.move();
    }
}
