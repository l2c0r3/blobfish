package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.EvalWrapper;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.List;

public class EvaluationUtil {

    public record EvalConfig(EvalWrapper wrapper, String description){}

    public static List<EvalConfig> getAllEvalStrategiesCombinations() {
        try (ScanResult scan = new ClassGraph()
                .enableClassInfo()
                .acceptPackages("ch.hslu.cas.msed.blobfish")
                .scan()) {

            var instances = scan
                    .getClassesImplementing(EvalStrategy.class)
                    .stream()
                    .filter(e -> !e.getName().contains("Wrapper"))
                    .filter(e -> !e.getName().contains("Random"))
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

            var configs = new ArrayList<EvalConfig>();

            var instanceList = new ArrayList<EvalStrategy>();
            StringBuilder configDescription;

            for (int i = 0; i < instances.length; i++) {
                var instance = instances[i];

                instanceList = new ArrayList<>();
                instanceList.add((EvalStrategy) instance);
                configDescription = new StringBuilder(instance.getClass().getSimpleName());
                configs.add(new EvalConfig(new EvalWrapper(instanceList), configDescription.toString()));

                for (int j = i + 1; j < instances.length; j++) {

                    var nextInstances = instances[j];
                    instanceList.add((EvalStrategy) instance);
                    configDescription.append(" & ").append(nextInstances.getClass().getSimpleName());
                    configs.add(new EvalConfig(new EvalWrapper(instanceList), configDescription.toString()));
                }
            }

            return configs;
        }
    }
}
