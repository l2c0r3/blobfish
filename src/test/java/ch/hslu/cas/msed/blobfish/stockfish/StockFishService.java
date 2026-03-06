package ch.hslu.cas.msed.blobfish.stockfish;

import java.io.IOException;
import java.util.List;

public class StockFishService implements AutoCloseable {

    private final StockFishContainer stockFishContainer;
    private final UciClient uci;

    private int calculationDepth = 245;

    StockFishService(StockFishContainer stockFishContainer, UciClient uci) {
        this.stockFishContainer = stockFishContainer;
        this.uci = uci;
    }

    public void setPosition(String fen) {
        this.uci.setPosition(fen);
    }

    public void setDefaultCalculationDepth(int calulationDepth) {
        this.calculationDepth = calulationDepth;
    }

    public List<String> go() {
        return this.go(calculationDepth);
    }

    public List<String> go(int depth) {
        return uci.go(depth);
    }

    // Always close both
    @Override
    public void close() throws IOException {
        IOException failure = null;
        try {
            uci.close();
        } catch (IOException e) {
            failure = e;
        }
        try {
            stockFishContainer.close();
        } catch (RuntimeException closeEx) {
            if (failure != null) {
                failure.addSuppressed(closeEx);
            } else {
                throw closeEx;
            }
        }
        if (failure != null) {
            throw failure;
        }
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
            UciClient uci = null;
            try {
                stockFishContainer.init();
                uci = new UciClient(stockFishContainer.getHost(), stockFishContainer.getPort());

                if (multiPV != 0) {
                    uci.setMultiPV(multiPV);
                }

                var service = new StockFishService(stockFishContainer, uci);
                if (defaultCalucationDepth != 0) {
                    service.setDefaultCalculationDepth(defaultCalucationDepth);
                }
                return service;
            } catch (IOException | RuntimeException e) {
                // close when container fails to start or something
                if (uci != null) {
                    try {
                        uci.close();
                    } catch (IOException closeEx) {
                        e.addSuppressed(closeEx);
                    }
                }
                try {
                    stockFishContainer.close();
                } catch (Exception closeEx) {
                    e.addSuppressed(closeEx);
                }
                throw e;
            }
        }
    }
}
