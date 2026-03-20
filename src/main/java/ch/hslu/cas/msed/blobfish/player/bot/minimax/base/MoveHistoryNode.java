package ch.hslu.cas.msed.blobfish.player.bot.minimax.base;

public record MoveHistoryNode(String move, MoveHistoryNode parent, int depth) {

    public MoveHistoryNode(final String move, final MoveHistoryNode parent) {
        this(move, parent, parent == null ? 1 : parent.depth + 1);
    }
}