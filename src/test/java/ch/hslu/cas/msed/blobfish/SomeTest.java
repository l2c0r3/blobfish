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
        stockFishService.close();
    }

}
