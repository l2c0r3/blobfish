package ch.hslu.cas.msed.blobfish.util;

import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TexTableUtil {

    private TexTableUtil() {
        // util class
    }

    public record GlobalMeasurementEntry(String positionFen, String positionDescription, String algorithm,
                                         String strategy, int depth, Duration duration) {
        public GlobalMeasurementEntry(final CSVRecord record) {
            this(
                    record.get(0),
                    record.get(1),
                    record.get(2),
                    record.get(3),
                    Integer.parseInt(record.get(4)),
                    Duration.parse(record.get(5))
            );
        }
    }

    public static void generateMeasurementPerformanceGainTable(final String referenceAlgorithm, final List<GlobalMeasurementEntry> measurements, final File rootFolderForMeasurements) {
        var referenceDepth = measurements
                .stream()
                .filter(e -> e.algorithm().equals(referenceAlgorithm))
                .mapToInt(GlobalMeasurementEntry::depth)
                .max()
                .orElse(0);

        var referenceEntries = measurements
                .stream()
                .filter(e -> e.algorithm().equals(referenceAlgorithm))
                .filter(e -> e.depth() == referenceDepth)
                .collect(Collectors.groupingBy(GlobalMeasurementEntry::strategy));

        var algorithms = measurements
                .stream()
                .map(GlobalMeasurementEntry::algorithm)
                .filter(algorithm -> !algorithm.equals(referenceAlgorithm))
                .collect(Collectors.toSet());

        // rows -> cells
        var performanceGains = measurements
                .stream()
                .filter(e -> !e.algorithm().equals(referenceAlgorithm))
                .filter(e -> e.depth() == referenceDepth)
                .collect(Collectors.groupingBy(GlobalMeasurementEntry::strategy))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        (strategyEntry) -> {
                            var referenceMedian = MeasurementUtil.calcMedianDuration(referenceEntries.get(strategyEntry.getKey()).stream()
                                    .map(GlobalMeasurementEntry::duration)
                                    .toList());


                            return strategyEntry.getValue().stream()
                                    .collect(Collectors.groupingBy(GlobalMeasurementEntry::algorithm))
                                    .values().stream()
                                    .map(globalMeasurementEntries -> {
                                        var median = MeasurementUtil.calcMedianDuration(globalMeasurementEntries.stream()
                                                .map(GlobalMeasurementEntry::duration)
                                                .toList());

                                        var gain = (double) referenceMedian.toNanos() / median.toNanos();
                                        return Math.round(gain * 1_000.0) / 1_000.0;
                                    })
                                    .toList();
                        }
                ));

        var rowCounter = new AtomicInteger(0);
        var rows = performanceGains.entrySet()
                .stream()
                .map(e -> {
                    var isEven = rowCounter.incrementAndGet() % 2 == 0;

                    return createTableRow(e.getKey(), e.getValue(), isEven);
                })
                .collect(Collectors.joining(System.lineSeparator()));

        var colCount = algorithms.size();

        var headers = algorithms.stream()
                .map("\\multicolumn{1}{r|}{\\textbf{%s}}"::formatted)
                .collect(Collectors.joining(" & "));

        var rowTitle = "Strategy";
        var headerTitle = "Median performance gain across positions";
        var tableCaption = "Median performance difference from %s at depth %d".formatted(referenceAlgorithm, referenceDepth);
        var tableLabel = tableCaption.toLowerCase().replaceAll(" ", "-");

        var content = createTexTable(colCount, rowTitle, headerTitle, headers, rows, tableCaption, tableLabel);

        var fileName = "performance-gains-%s.tex".formatted(referenceAlgorithm.toLowerCase().replaceAll(" ", "-"));
        var file = new File(rootFolderForMeasurements, fileName);

        try (var fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createTableRow(final String title, final List<Double> values, final boolean isEven) {
        var color = isEven ? "EFEFEF" : "FFFFFF";

        var rowContent = values.stream()
                .map(v -> v == null ? "" : String.format("%.2f", v))
                .collect(Collectors.joining(" & "));

        return """
                \\rowcolor[HTML]{%s}
                \\textbf{%s}               & %s \\\\
                """.formatted(color, title, rowContent);
    }

    public static String createTexTable(final int numberOfColumns, final String rowTitle, final String headerTitle, final String headers, final String rows, final String tableCaption, final String tableLabel) {
        return """
                \\begin{table}[H]
                    \\centering
                    \\resizebox{\\textwidth}{!}{%%
                        \\begin{tabular}{|p{8cm}|*{%d}{r|}}
                            \\hline
                            & \\multicolumn{%d}{c|}{\\textbf{%s}} \\\\
                            \\cline{2-%d}
                            \\multirow{-2}{*}{\\parbox{8cm}{\\centering\\textbf{%s}}} & %s \\\\
                            \\hline
                            %s
                            \\hline
                        \\end{tabular}%%
                    }
                    \\caption{%s}
                    \\label{tab:%s}
                \\end{table}
                """.formatted(numberOfColumns, numberOfColumns, headerTitle, numberOfColumns + 1, rowTitle, headers, rows, tableCaption, tableLabel);
    }
}
