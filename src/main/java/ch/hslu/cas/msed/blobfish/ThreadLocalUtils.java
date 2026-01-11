package ch.hslu.cas.msed.blobfish;

import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ThreadLocalUtils {

    private ThreadLocalUtils() {
    }

    /**
     * Resets a static ThreadLocal field in the given class.
     *
     * @param clazz     The class containing the ThreadLocal.
     * @param fieldName The name of the ThreadLocal field.
     * @throws ReflectiveOperationException If reflection fails.
     */
    public static void resetStaticThreadLocal(@NonNull Class<?> clazz, @NonNull String fieldName)
            throws ReflectiveOperationException {

        // Get the declared field (private/protected/public)
        Field field = clazz.getDeclaredField(fieldName);

        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException("Field " + fieldName + " is not static");
        }

        field.setAccessible(true);

        // Ensure it's a ThreadLocal
        Object obj = field.get(null); // null because static
        if (!(obj instanceof ThreadLocal<?> tl)) {
            throw new IllegalArgumentException("Field " + fieldName + " is not a ThreadLocal");
        }

        // Remove value for current thread
        tl.remove();
    }
}