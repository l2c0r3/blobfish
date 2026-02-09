package ch.hslu.cas.msed.blobfish.stockfish;

import java.io.IOException;

public class StockFishService implements AutoCloseable {

    private final StockFishContainer stockFishContainer;
    private final UciClient uci;

    StockFishService(StockFishContainer stockFishContainer, UciClient uci) {
        this.stockFishContainer = stockFishContainer;
        this.uci = uci;
    }

    public void setPosition(String fen) {
        uci.setPosition(fen);
    }


    @Override
    public void close() throws IOException {
        uci.close();
        stockFishContainer.close();
    }


    public static class StockFishServiceBuilder {

        private int multiPV;

        public StockFishServiceBuilder withMultiPV(int value) {
            this.multiPV = value;
            return this;
        }

        public StockFishService build() throws IOException {
            var stockFishContainer = new StockFishContainer();
            stockFishContainer.init();

            UciClient uci = new UciClient(stockFishContainer.getHost(),  stockFishContainer.getPort());

            // set options
            if (multiPV != 0) {
                uci.setMultiPV(multiPV);
            }

            return new StockFishService(stockFishContainer, uci);
        }
    }
}
