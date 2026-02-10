package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.PerformanceTest;
import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import ch.hslu.cas.msed.blobfish.eval.MaterialEval;
import ch.hslu.cas.msed.blobfish.player.bot.BotAlgorithm;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxAlgo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class BotAlgorithmProvider {

    @FunctionalInterface
    public interface MiniMaxAlgoConstructor {
        MiniMaxAlgo create(int depth, EvalStrategy strategy, PlayerColor playerToMove);
    }

    public record PossibleStrategy(EvalStrategy strategy, String description) {
    }

    public static final List<PossibleStrategy> possibleStrategies = List.of(
            new PossibleStrategy(new MaterialEval(), "Simple material evaluation"),
            new PossibleStrategy(new MateAwareEval(new MaterialEval()), "Mate aware material evaluation")
    );

    public static List<MiniMaxAlgoConstructor> getAllMiniMaxConstructors() {
        Class<?> base = MiniMaxAlgo.class;

        try (ScanResult scan = new ClassGraph()
                .enableClassInfo()
                .acceptPackages("ch.hslu.cas.msed.blobfish")
                .scan()) {

            return scan.getSubclasses(base.getName()).loadClasses()
                    .stream().map(clazz -> {
                        try {
                            var constructor = clazz.getDeclaredConstructor(int.class, EvalStrategy.class, PlayerColor.class);
                            return (MiniMaxAlgoConstructor) (
                                    int depth,
                                    EvalStrategy strategy,
                                    PlayerColor playerToMove
                            ) -> instantiateAlgorithm(constructor, depth, strategy, playerToMove);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();
        }
    }

    public static MiniMaxAlgo instantiateAlgorithm(
            Constructor<?> constructor,
            int depth,
            EvalStrategy strategy,
            PlayerColor playerToMove
    ) {
        try {
            return (MiniMaxAlgo) constructor.newInstance(depth, strategy, playerToMove);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


}
