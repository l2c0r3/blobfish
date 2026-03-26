package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.eval.CompositeEvaluationStrategy;
import ch.hslu.cas.msed.blobfish.base.EvaluationStrategy;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.List;
import java.util.stream.Collectors;


public class EvaluationUtil {

    private static final List<Class> EVAL_CLASSES_TO_IGNORE = List.of(
            CompositeEvaluationStrategy.class
    );

    public record EvalConfig(EvaluationStrategy strategy, String description) {
    }

    @SuppressWarnings("unchecked")
    public static List<EvalConfig> getAllEvalStrategiesCombinations() {
        try (ScanResult scan = new ClassGraph()
                .enableClassInfo()
                .acceptPackages("ch.hslu.cas.msed.blobfish")
                .scan()) {

            List<EvaluationStrategy> evalClasses = (List<EvaluationStrategy>) scan
                    .getClassesImplementing(EvaluationStrategy.class)
                    .stream()
                    .filter(classInfo -> !classInfo.isInterface())
                    .filter(classInfo -> !EVAL_CLASSES_TO_IGNORE.contains(classInfo.loadClass()))
                    .map(classInfo -> {
                        try {
                            Class<? extends EvaluationStrategy> clazz = classInfo.loadClass(EvaluationStrategy.class);
                            return clazz.getDeclaredConstructor().newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            return PowerSetUtil.getPowerSet(evalClasses).stream()
                    .filter(s -> !s.isEmpty())
                    .map(EvaluationUtil::mapToEvalConfig)
                    .toList();
        }
    }

    private static EvalConfig mapToEvalConfig(List<EvaluationStrategy> evalStrategies) {
        if (evalStrategies.isEmpty()) {
            return null;
        }

        if (evalStrategies.size() == 1) {
            var strategy = evalStrategies.getFirst();
            var name = strategy.getClass().getSimpleName();
            return new EvalConfig(strategy, name);
        }

        var strategyCombined = new CompositeEvaluationStrategy.Builder();
        evalStrategies.forEach(strategyCombined::add);
        var strategyDescription = evalStrategies.stream()
                .map(EvaluationStrategy::getClass)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(" & "));
        return new EvalConfig(strategyCombined.build(),  strategyDescription);
    }
}
