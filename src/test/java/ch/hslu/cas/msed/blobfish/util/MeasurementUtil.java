package ch.hslu.cas.msed.blobfish.util;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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

    public static Duration calcAverageDuration(List<MeasurementResult<String>> measurements) {
        Duration total = measurements.stream()
                .map(MeasurementUtil.MeasurementResult::duration)
                .reduce(Duration.ZERO, Duration::plus);

        return total.dividedBy(measurements.size());
    }

    public static Duration calcMedianDuration(List<Duration> measurements) {
        if (measurements.isEmpty()) {
            return Duration.ZERO;
        }

        var sortedMeasurements = new ArrayList<>(measurements);
        sortedMeasurements.sort(Duration::compareTo);

        if (sortedMeasurements.size() % 2 == 0) {
            var d1 = sortedMeasurements.get(measurements.size() / 2 - 1);
            var d2 = sortedMeasurements.get(measurements.size() / 2);
            return d1.plus(d2).dividedBy(2);
        } else {
            return sortedMeasurements.get(sortedMeasurements.size() / 2);
        }
    }
}
