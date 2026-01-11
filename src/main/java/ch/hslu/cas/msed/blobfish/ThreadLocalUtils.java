package ch.hslu.cas.msed.blobfish;

import java.lang.reflect.Field;

public class ThreadLocalUtils {

    /**
     * Resets a static ThreadLocal field in the given class.
     *
     * @param clazz     The class containing the ThreadLocal.
     * @param fieldName The name of the ThreadLocal field.
     * @throws ReflectiveOperationException If reflection fails.
     */
    public static void resetStaticThreadLocal(Class<?> clazz, String fieldName)
            throws ReflectiveOperationException {

        // Get the declared field (private/protected/public)
        Field field = clazz.getDeclaredField(fieldName);
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