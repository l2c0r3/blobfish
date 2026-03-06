package ch.hslu.cas.msed.blobfish.stockfish.junit;

import ch.hslu.cas.msed.blobfish.stockfish.StockFishService;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class StockfishExtension implements TestInstancePostProcessor {

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
}