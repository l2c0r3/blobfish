package ch.hslu.cas.msed.blobfish.player.bot.minimax;

import com.github.bhlangonijr.chesslib.move.Move;

import java.util.LinkedList;

public record MoveNode(double eval, LinkedList<Move> history) {
}
