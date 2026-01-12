package ch.hslu.cas.msed.blobfish.util;

import java.time.Duration;
import java.util.concurrent.Callable;

public class MeasurementUtil {

    private MeasurementUtil() {
        // util class
    }

    public record MeasurementResult<ResultType>(Duration duration, ResultType result) {}

    public static <ResultType> MeasurementResult<ResultType> measureOperation(Callable<ResultType> operation) {
        var startTime = System.nanoTime();
        ResultType result;
        try {
            result = operation.call();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        var endTime = System.nanoTime();
        var duration = Duration.ofNanos(endTime - startTime);
        return new MeasurementResult<>(duration, result);
    }
}
