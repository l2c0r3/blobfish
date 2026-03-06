package ch.hslu.cas.msed.blobfish;

import ch.hslu.cas.msed.blobfish.stockfish.StockFishService;
import ch.hslu.cas.msed.blobfish.stockfish.junit.InjectStockfish;
import ch.hslu.cas.msed.blobfish.stockfish.junit.StockfishExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(StockfishExtension.class)
class SomeTest {

    @InjectStockfish
    StockFishService stockFishService;

    @Test
    void blub() throws Exception {
        stockFishService.setPosition("1r4r1/5p1k/p2p1q1p/2b1nPQ1/p7/6RP/B1R2PPK/2B5 b - - 0 1");
        stockFishService.go();
    }

    @Test
    void blub2() throws Exception {
        stockFishService.setPosition("1r4r1/5p1k/p2p1q1p/2b1nPQ1/p7/6RP/B1R2PPK/2B5 b - - 0 1");
        stockFishService.go();
    }

}
