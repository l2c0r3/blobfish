package ch.hslu.cas.msed.blobfish.util;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PlantUmlUtil {

    public record ChartBar(String barDescription, List<Double> values){}

    private static final List<String> diagramColors = List.of(
            "#1F77B4", // Blau
            "#FF7F0E", //Orange
            "#2CA02C", // Grün
            "#D62728", // Rot
            "#9467BD", // Lila
            "#8C564B", // Braun
            "#E377C2", // Pink
            "#7F7F7F", // Grau
            "#BCBD22", // Oliv
            "#17BECF", // Türkis
            "#AEC7E8", // Hellblau
            "#FFBB78", // Hellorange
            "#98DF8A", // Hellgrün
            "#FF9896", // Hellrot
            "#C5B0D5", // Helllila
            "#9EDAE5" // Helltürkis
    );

    private PlantUmlUtil() {
        // util class
    }

    public static File createBarChart(String barTitle, List<String> horizontalAxisTitles, String verticalAxisTitle,
                                      List<ChartBar> bars) {

        if (bars.size() > diagramColors.size()) {
            throw new IllegalStateException("don't have enough diagram colors to create bar diagram");
        }

        var hAxisTitle = "[ " + String.join(",", horizontalAxisTitles) + " ]";

        AtomicInteger colorIndex = new AtomicInteger(0);
        var barStrings = bars.stream()
                .map(chartBar -> {
                    var barDescription = chartBar.barDescription();
                    var measurements = chartBar.values().stream()
                            .map(m -> {
                                if (m == 0) {
                                    return 0.00001; // heigh = 0 in diagram is not possible in plantuml
                                } else {
                                    return m;
                                }
                            })
                            .map(Object::toString)
                            .collect(Collectors.joining(","));
                    var colorI = colorIndex.getAndIncrement();
                    if (colorI > diagramColors.size()) {
                        throw new RuntimeException("color out of bounds");
                    }
                    return "bar \"" + barDescription + "\"" + "[ " +  measurements + " ] " + diagramColors.get(colorI);
                })
                .collect(Collectors.joining("\n"));

        var allValues = bars.stream().flatMap(b -> b.values().stream()).toList();
        var maxValue = getMaxValueForChart(allValues);
        var spacing = getBarChartSpacing(allValues);

        var content = """
        @startchart
        
        title
        %s
        end title
        
        h-axis %s
        v-axis "%s" 0 --> %s spacing %s grid
        
        %s
        
        legend right
        @endchart
        """.formatted(barTitle, hAxisTitle, verticalAxisTitle, maxValue, spacing, barStrings);

        var tmpFile = FileUtil.createTmpFile("plantuml", "csv");
        try (FileWriter fw = new FileWriter(tmpFile)) {
            fw.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tmpFile;
    }

    public static File convertPlantUmlToSvg(File plantuml) {
        var tmpFile = FileUtil.createTmpFile("plant2Svg", "svg");
        try (var reader = new FileReader(plantuml);
             var writer = new FileWriter(tmpFile)
        ) {
            var content = reader.readAllAsString();

            var sourceFileReader = new SourceStringReader(content);

            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            sourceFileReader.outputImage(os, new FileFormatOption(FileFormat.SVG));
            os.close();

            final String svg = os.toString(StandardCharsets.UTF_8);
            writer.write(svg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tmpFile;
    }

    private static Double getMaxValueForChart(List<Double> measurements) {
        double max = measurements == null ? 0.0 :
                measurements.stream()
                        .mapToDouble(Double::doubleValue)
                        .max()
                        .orElse(0.0);

        if (max <= 0.0) {
            return 1.0;
        }

        return niceCeil(max);
    }

    private static Double getBarChartSpacing(List<Double> measurements) {
        double maxAxis = getMaxValueForChart(measurements);
        if (maxAxis <= 0.0) {
            return 1.0;
        }

        int targetTicks = 8;
        double rawStep = maxAxis / targetTicks;
        double step = niceCeil(rawStep);

        return Math.max(step, 0.00001);
    }

    /**
     * Round x up to 1, 2, 5 * 10^n
     */
    private static double niceCeil(double x) {
        if (x <= 0) {
            return 1.0;
        }

        double exp = Math.floor(Math.log10(x));
        double base = Math.pow(10, exp);
        double f = x / base; // 1..10

        double nice;
        if (f <= 1.0) nice = 1.0;
        else if (f <= 2.0) nice = 2.0;
        else if (f <= 5.0) nice = 5.0;
        else nice = 10.0;

        return nice * base;
    }

}
