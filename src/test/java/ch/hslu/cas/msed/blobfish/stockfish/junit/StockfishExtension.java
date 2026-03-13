package ch.hslu.cas.msed.blobfish.stockfish.junit;

import ch.hslu.cas.msed.blobfish.stockfish.StockFishService;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.lang.reflect.Field;

public class StockfishExtension implements BeforeAllCallback, TestInstancePostProcessor, BeforeEachCallback, AfterAllCallback {

    private static volatile StockFishService stockfishService;

    /**
     * Starts container if needed
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        stockfishService = new StockFishService.StockFishServiceBuilder()
                .withMultiPV(3)
                .withDefaultCalculationDepth(5)
                .build();
    }

    /**
     * Implements field injection for @InjectStockfish
     */
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Class<?> type = testInstance.getClass();

        while (type != null) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectStockfish.class)
                        && field.getType().equals(StockFishService.class)) {
                    field.setAccessible(true);
                    field.set(testInstance, stockfishService);
                }
            }
            type = type.getSuperclass();
        }
    }

    /**
     * Resets stockfish after each method
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        stockfishService.newGame();
    }

    /**
     * Stops stockfish at the end of tests
     */
    @Override
    public void afterAll(ExtensionContext context) throws IOException {
        stockfishService.close();
    }
}