package ch.hslu.cas.msed.blobfish.player.bot.minimax;

public record MoveHistoryNode(String move, MoveHistoryNode parent, int depth) {

    public MoveHistoryNode(String move, MoveHistoryNode parent) {
        this(move, parent, parent == null ? 1 : parent.depth + 1);
    }
}