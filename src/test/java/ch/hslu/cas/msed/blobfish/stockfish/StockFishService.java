package ch.hslu.cas.msed.blobfish.stockfish;

public class StockFishService implements AutoCloseable {

    private final StockFishContainer stockFishContainer;
    private final UciClient uci;

    public StockFishService() {
        stockFishContainer = new StockFishContainer();
        stockFishContainer.init();

        uci = new UciClient(stockFishContainer.getHost(),  stockFishContainer.getPort());

        uci.init();
    }


    @Override
    public void close() throws Exception {
        uci.close();
        stockFishContainer.close();
    }
}
