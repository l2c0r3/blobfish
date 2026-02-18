package ch.hslu.cas.msed.blobfish;

import ch.hslu.cas.msed.blobfish.stockfish.StockFishService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.bouncycastle.crypto.agreement.jpake.JPAKEUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Tag(value = "performance")
class QualityTest {

    private static final List<String> TEST_FENS = List.of(
        "1r4r1/5p1k/p2p1q1p/2b1nPQ1/p7/6RP/B1R2PPK/2B5 b - - 0 1"
    );
    private static final HashMap<String, List<String>> BEST_MOVES = new HashMap<>();
    private static final int DEPTH_TO_CALCULATE = 5;
    private static final int MULTI_PV = 3;


    @BeforeAll
    static void setup() {
        try (StockFishService stockFishService = new StockFishService.StockFishServiceBuilder()
                .withMultiPV(MULTI_PV)
                .withDefaultCalculationDepth(DEPTH_TO_CALCULATE)
                .build()) {

            TEST_FENS.forEach(fen -> {
                stockFishService.setPosition(fen);
                var bestStockfishMoves = stockFishService.go();
                BEST_MOVES.put(fen, bestStockfishMoves);
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void blub() throws Exception {

    }

}
