package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.PerformanceTest;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ch.hslu.cas.msed.blobfish.PerformanceTest.getAlgorithmName;

public class PlantUmlUtil {

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

    public static File createPlantUml(PerformanceTest.PositionToTest positionToTest, Map<PerformanceTest.AlgorithmStrategy, List<PerformanceTest.MeasurementOfDepth>> results) {
        AtomicInteger colorIndex = new AtomicInteger(0);
        var barStrings = results.keySet().stream()
                .sorted((a1, a2) -> {
                    var nameA1 = getAlgorithmName(a1);
                    var nameA2 = getAlgorithmName(a2);
                    return String.CASE_INSENSITIVE_ORDER.reversed().compare(nameA1, nameA2);
                })
                .map(k -> {
                    var algorithmName = getAlgorithmName(k);
                    var messurements = results.get(k).stream()
                            .map(m -> m.measurementResult().duration().toMillis())
                            .map(m -> {
                                if (m == 0) {
                                    return 0.00001; // heigh = 0 in diagram is not possible in plantuml
                                } else {
                                    return m;
                                }
                            })
                            .map(m -> m + "")
                            .collect(Collectors.joining(","));
                    var colorI = colorIndex.getAndIncrement();
                    if (colorI > diagramColors.size()) {
                        throw new RuntimeException("color out of bounds");
                    }
                    return "bar \"" + algorithmName + "\"" + "[ " +  messurements + " ] " + diagramColors.get(colorI) + "\n";
                })
                .collect(Collectors.joining());

        var hAxis = "[";
        for (int i = 1; i <= results.entrySet().iterator().next().getValue().size() ; i++) {
            hAxis += "Depth" + i;
            if (i != results.entrySet().iterator().next().getValue().size()) {
                hAxis += ",";
            }
        }
        hAxis += "]";

        var content = """
        @startchart
        
        title
        FEN: %s
        end title
        
        h-axis %s
        v-axis "Calculation time [ms]" 0 --> 800 spacing 100
        
        %s
        
        legend right
        @endchart
        """.formatted(positionToTest.fen(), hAxis, barStrings);

        var tmpFile = FileUtil.createTmpFile("resultFile", "csv");
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
}
