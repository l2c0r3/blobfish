package ch.hslu.cas.msed.blobfish.stockfish.junit;

import ch.hslu.cas.msed.blobfish.stockfish.StockFishService;

import java.io.IOException;

public final class StockFishSingleton {

    private static volatile StockFishService service;

    private StockFishSingleton() {
    }

    public static StockFishService getOrStart() throws IOException {
        if (service == null) {
            synchronized (StockFishSingleton.class) {
                if (service == null) {
                    service = new StockFishService.StockFishServiceBuilder()
                            .withMultiPV(3)
                            .withDefaultCalculationDepth(5)
                            .build();
                }
            }
        }
        return service;
    }

    public static StockFishService get() {
        if (service == null) {
            throw new IllegalStateException("Stockfish wurde noch nicht gestartet.");
        }
        return service;
    }

    public static void stop() throws IOException {
        synchronized (StockFishSingleton.class) {
            if (service != null) {
                service.close();
                service = null;
            }
        }
    }
}
