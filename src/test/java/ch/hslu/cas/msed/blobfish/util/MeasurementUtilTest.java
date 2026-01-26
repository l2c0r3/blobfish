package ch.hslu.cas.msed.blobfish.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MeasurementUtilTest {

    private static Stream<Object[]> medianProvider() {
        return Stream.of(
                // all same
                new Object[]{List.of(
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(1)),
                        Duration.ofSeconds(1)
                },

                // even ->  (12 + 14) / 2 = 13s
                new Object[]{List.of(
                        Duration.ofSeconds(5),
                        Duration.ofSeconds(12),
                        Duration.ofSeconds(14),
                        Duration.ofSeconds(15)),
                        Duration.ofSeconds(13)
                },

                // odd -> middle
                new Object[]{List.of(
                        Duration.ofSeconds(5),
                        Duration.ofSeconds(14),
                        Duration.ofSeconds(15)),
                        Duration.ofSeconds(14)
                },

                // unsorted, odd
                new Object[]{List.of(
                        Duration.ofSeconds(10),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(7)),
                        Duration.ofSeconds(7)
                },

                // unsorted, odd
                new Object[]{List.of(
                        Duration.ofSeconds(10),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(2),
                        Duration.ofSeconds(3),
                        Duration.ofSeconds(7)),
                        Duration.ofSeconds(3)
                },

                // unsorted, even -> (7 + 10) / 2 = 8.5s
                new Object[]{List.of(
                        Duration.ofSeconds(12),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(10),
                        Duration.ofSeconds(7)),
                        Duration.ofSeconds(8).plusMillis(500)
                },

                // duplicates 1,5,5,9 => (5+5)/2 = 5
                new Object[]{List.of(
                        Duration.ofSeconds(5),
                        Duration.ofSeconds(9),
                        Duration.ofSeconds(1),
                        Duration.ofSeconds(5)),
                        Duration.ofSeconds(5)
                }
        );
    }


    @ParameterizedTest
    @MethodSource("medianProvider")
    void test_calcMedianDuration_returnsExpected(List<Duration> measurements, Duration expected) {
        // Act
        var result = MeasurementUtil.calcMedianDuration(measurements);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void test_calcMedianDuration_evenCount_returnsExactAverageWithMillis() {
        // Arrange
        var measures = List.of(
                Duration.ofSeconds(4),
                Duration.ofSeconds(1),
                Duration.ofSeconds(3),
                Duration.ofSeconds(2)
        );

        // Act
        var result = MeasurementUtil.calcMedianDuration(measures);

        // Assert
        // sorted: 1s, 2s, 3s, 4s -> (2s+3s)/2 = 2.5s
        assertEquals(Duration.ofSeconds(2).plusMillis(500), result);
    }
}