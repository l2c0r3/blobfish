package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.List;


// TODO: Review pic
public class EvaluationUtil {

    public record EvalConfig(EvalStrategy wrapper, String description){}

    public static List<EvalConfig> getAllEvalStrategiesCombinations() {
        try (ScanResult scan = new ClassGraph()
                .enableClassInfo()
                .acceptPackages("ch.hslu.cas.msed.blobfish")
                .scan()) {

            var instances = scan
                    .getClassesImplementing(EvalStrategy.class)
                    .stream()
                    .filter(e -> !e.getName().contains("Wrapper"))
                    .filter(classInfo -> !classInfo.isInterface())
                    .map(classInfo -> {
                        try {
                            Class<? extends EvalStrategy> clazz = classInfo.loadClass(EvalStrategy.class);
                            return clazz.getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray();

            return List.of();
        }
    }
}
