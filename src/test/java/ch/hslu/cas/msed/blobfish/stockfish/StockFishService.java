package ch.hslu.cas.msed.blobfish.stockfish;

import java.io.IOException;
import java.util.List;

public class StockFishService implements AutoCloseable {

    private final StockFishContainer stockFishContainer;
    private final UciClient uci;

    private static int calculationDepth = 245;

    StockFishService(StockFishContainer stockFishContainer, UciClient uci) {
        this.stockFishContainer = stockFishContainer;
        this.uci = uci;
    }

    public void setPosition(String fen) {
        uci.setPosition(fen);
    }

    public void setDefaultCalculationDepth(int calulationDepth) {
        StockFishService.calculationDepth = calulationDepth;
    }

    public List<String> go() {
        return this.go(calculationDepth);
    }

    public List<String> go(int depth) {
        return uci.go(depth);
    }

    @Override
    public void close() throws IOException {
        uci.close();
        stockFishContainer.close();
    }


    public static class StockFishServiceBuilder {

        private int multiPV;
        private int defaultCalucationDepth;

        public StockFishServiceBuilder withMultiPV(int value) {
            this.multiPV = value;
            return this;
        }

        public StockFishServiceBuilder withDefaultCalculationDepth(int depth) {
            this.defaultCalucationDepth = depth;
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

            var service = new StockFishService(stockFishContainer, uci);

            if (defaultCalucationDepth != 0) {
                service.setDefaultCalculationDepth(defaultCalucationDepth);
            }

            return service;
        }
    }
}
