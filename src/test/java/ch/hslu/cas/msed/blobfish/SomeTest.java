package ch.hslu.cas.msed.blobfish;

import ch.hslu.cas.msed.blobfish.testcontainers.StockFishContainer;
import org.junit.jupiter.api.Test;

class SomeTest {

    @Test
    void blub() {
        StockFishContainer stockFishContainer = new StockFishContainer();
        stockFishContainer.start();
        stockFishContainer.stop();
    }

}
