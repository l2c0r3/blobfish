package ch.hslu.cas.msed.blobfish.stockfish.junit;

import ch.hslu.cas.msed.blobfish.stockfish.StockFishService;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class StockfishExtension implements BeforeAllCallback, TestInstancePostProcessor, BeforeEachCallback {

    /**
     * Starts container if needed
     */
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        StockFishSingleton.getOrStart();
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
                    field.set(testInstance, StockFishSingleton.get());
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
        var stockfishService = StockFishSingleton.get();
        stockfishService.newGame();
    }

    // TODO: implement AfterAll
}