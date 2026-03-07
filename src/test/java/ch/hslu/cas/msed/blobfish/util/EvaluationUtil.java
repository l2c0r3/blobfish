package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class EvaluationUtil {

    public record EvalConfig(EvalStrategy strategy, String description){}

    public static List<EvalConfig> getAllEvalStrategies() {
        Class<?> base = EvalStrategy.class;

        try (ScanResult scan = new ClassGraph()
                .enableClassInfo()
                .acceptPackages("ch.hslu.cas.msed.blobfish")
                .scan()) {

            var evalStrategies =  scan.getSubclasses(base.getName()).loadClasses().stream()
                    .filter(c -> c.getName().contains("wrapper"))
                    .map(EvaluationUtil::mapToConstructor)
                    .map(EvaluationUtil::initClass);

            var a = "aa";
        }

        return List.of();
    }

    private static Object initClass(Constructor<?> c) {
        try {
            return c.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Constructor<?> mapToConstructor(Class<?> c) {
        try {
            return c.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
