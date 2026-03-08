package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.eval.CompositeEvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.RandomEval;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.List;
import java.util.stream.Collectors;


// TODO: Review pic
public class EvaluationUtil {

    private static final List<Class> EVAL_CLASSES_TO_IGNORE = List.of(
            CompositeEvalStrategy.class,
            RandomEval.class
    );

    public record EvalConfig(EvalStrategy strategy, String description) {
    }

    @SuppressWarnings("unchecked")
    public static List<EvalConfig> getAllEvalStrategiesCombinations() {
        try (ScanResult scan = new ClassGraph()
                .enableClassInfo()
                .acceptPackages("ch.hslu.cas.msed.blobfish")
                .scan()) {

            List<EvalStrategy> evalClasses = (List<EvalStrategy>) scan
                    .getClassesImplementing(EvalStrategy.class)
                    .stream()
                    .filter(classInfo -> !classInfo.isInterface())
                    .filter(classInfo -> !EVAL_CLASSES_TO_IGNORE.contains(classInfo.loadClass()))
                    .map(classInfo -> {
                        try {
                            Class<? extends EvalStrategy> clazz = classInfo.loadClass(EvalStrategy.class);
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

    private static EvalConfig mapToEvalConfig(List<EvalStrategy> evalStrategies) {
        if (evalStrategies.isEmpty()) {
            return null;
        }

        if (evalStrategies.size() == 1) {
            var strategy = evalStrategies.getFirst();
            var name = strategy.getClass().getSimpleName();
            return new EvalConfig(strategy, name);
        }

        var strategyCombined = new CompositeEvalStrategy.Builder();
        evalStrategies.forEach(strategyCombined::add);
        var strategyDescription = evalStrategies.stream()
                .map(EvalStrategy::getClass)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(" & "));
        return new EvalConfig(strategyCombined.build(),  strategyDescription);
    }
}
