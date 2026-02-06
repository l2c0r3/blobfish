package ch.hslu.cas.msed.blobfish.stockfish;

import java.io.IOException;

public class StockFishService implements AutoCloseable {

    private final StockFishContainer stockFishContainer;
    private final UciClient uci;

    public StockFishService() {
        stockFishContainer = new StockFishContainer();
        stockFishContainer.init();

        try {
            uci = new UciClient(stockFishContainer.getHost(),  stockFishContainer.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() throws Exception {
        uci.close();
        stockFishContainer.close();
    }
}
