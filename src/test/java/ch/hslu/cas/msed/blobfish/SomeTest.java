package ch.hslu.cas.msed.blobfish;

import ch.hslu.cas.msed.blobfish.stockfish.StockFishService;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class SomeTest {

    @Test
    void blub() throws Exception {
        StockFishService stockFishService = new StockFishService.StockFishServiceBuilder()
                .withMultiPV(3)
                .build();


        stockFishService.setPosition("1r4r1/5p1k/p2p1q1p/2b1nPQ1/p7/6RP/B1R2PPK/2B5 b - - 0 1");


        stockFishService.close();
    }

}
