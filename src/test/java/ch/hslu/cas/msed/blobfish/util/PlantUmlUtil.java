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

        var content = """
        @startchart
        
        title
        %s
        end title
        
        h-axis %s grid
        v-axis "%s" 0 --> 800 spacing 100
        
        %s
        
        legend right
        @endchart
        """.formatted(barTitle, hAxisTitle, verticalAxisTitle, barStrings);

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

    private Double getMaxValueForChart(List<ChartBar> bars) {
        var maxResult = bars.stream()
                .flatMap(b -> b.values().stream())
                .max(Double::compare).orElse(0.0);
        return roundForBarChart(maxResult);

    }

    private

    public Double roundForBarChart(Double d) {

    }
}
