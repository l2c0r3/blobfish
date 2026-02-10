package ch.hslu.cas.msed.blobfish;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class AbstractPositionTest {

    public record PositionToTest(String fen, PlayerColor playerToMove, String description) {}

    static Stream<Arguments> positionProvider() {
        return Stream.of(
                Arguments.of(new PositionToTest("1r4r1/5p1k/p2p1q1p/2b1nPQ1/p7/6RP/B1R2PPK/2B5 b - - 0 1", PlayerColor.BLACK, "Complex position with many options")),
                Arguments.of(new PositionToTest("5rk1/5q1p/p2PR1p1/4p1b1/1pP5/1P1Q4/P5PP/1K2R3 b - - 0 31", PlayerColor.BLACK, "Mid game - deflection - short")),
                Arguments.of(new PositionToTest("r3k2r/pNp1ppbp/2nq2pn/7P/2B3b1/3P1N2/PPP2PP1/R1BQK2R b KQkq - 0 11", PlayerColor.BLACK, "Mid game - fork - short")),
                Arguments.of(new PositionToTest("rn1q1r1k/pbpp1p1p/1p2p3/4P3/3P2Qp/P2B4/1PP2PPP/R4RK1 w - - 0 17", PlayerColor.WHITE, "Mid game - en passant - long")),
                Arguments.of(new PositionToTest("r5k1/7p/1p1Qp1p1/p1np1r2/1q3P1P/1P6/2P3P1/RB3RK1 w - - 3 26", PlayerColor.WHITE, "Mid game - discovery - short")),
                Arguments.of(new PositionToTest("8/5ppk/4p1p1/3pq3/3Q4/1B2r2P/P5P1/3R3K b - - 8 42", PlayerColor.BLACK, "End game - deflection - short")),
                Arguments.of(new PositionToTest("5r1k/1pqnbr1P/p2p1pQp/2p5/3PP2P/1PN5/1PP3R1/R5K1 w - - 0 24", PlayerColor.WHITE, "Mid game - promotion - mate in 2 - short")),
                Arguments.of(new PositionToTest("Q7/p1pk3p/2p2qp1/3p1b2/8/1PN1P3/P1PP2PP/R4KNR b - - 4 15", PlayerColor.BLACK, "Mid game - discovery - mate in 2 - short"))
        );
    }
}
