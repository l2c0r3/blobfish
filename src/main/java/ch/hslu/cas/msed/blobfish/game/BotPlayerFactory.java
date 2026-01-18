package ch.hslu.cas.msed.blobfish.game;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.eval.EvalStrategy;
import ch.hslu.cas.msed.blobfish.game.providers.BotAlgorithmProvider;
import ch.hslu.cas.msed.blobfish.game.providers.BotPlayerProvider;
import ch.hslu.cas.msed.blobfish.player.bot.BotAlgorithm;
import ch.hslu.cas.msed.blobfish.player.bot.BotPlayer;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public final class BotPlayerFactory {

    private static final Map<String, BotPlayerProvider<? extends BotAlgorithm>> BOT_PLAYERS;
    private static final Map<String, BotAlgorithmProvider> ALGORITHMS;

    private BotPlayerFactory() {
    }

    static {
        BOT_PLAYERS = ServiceLoader.load(BotPlayerProvider.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .map(BotPlayerFactory::castSafely)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        BotPlayerProvider::getBotName,
                        p -> p,
                        (existing, _) -> {
                            throw new IllegalStateException(
                                    "Duplicate BotPlayerProvider for name: " + existing.getBotName());
                        }
                ));

        ALGORITHMS = ServiceLoader.load(BotAlgorithmProvider.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toMap(
                        BotAlgorithmProvider::getAlgorithmName,
                        p -> p,
                        (existing, _) -> {
                            throw new IllegalStateException(
                                    "Duplicate BotAlgorithmProvider for name: " + existing.getAlgorithmName());
                        }
                ));
    }

    /**
     * Creates a list of strings in the format BOT_PLAYER + "." + ALGORITHM
     * using only compatible bot-algorithm combinations.
     */
    public static List<String> getViableBotNames() {
        return BOT_PLAYERS.values().stream()
                .flatMap(botProvider ->
                        ALGORITHMS.values().stream()
                                // only keep algorithms compatible with this bot
                                .filter(algoProvider -> {
                                    var algo = getAlgorithmProviderReturnType(algoProvider);
                                    return botSupportsAlgorithm(botProvider, algo);
                                })
                                // map to BOT_PLAYER.ALGORITHM name
                                .map(algoProvider ->
                                        botProvider.getBotName() + "." + algoProvider.getAlgorithmName())
                )
                .collect(Collectors.toList());
    }

    public static BotPlayer create(
            @NonNull String botName,
            @NonNull String algorithmName,
            int calculationDepth,
            EvalStrategy evalStrategy,
            PlayerColor playerColor
    ) {
        var botProvider = BOT_PLAYERS.get(botName);
        if (botProvider == null) {
            throw new IllegalArgumentException("Unknown bot player: " + botName);
        }

        var algProvider = ALGORITHMS.get(algorithmName);
        if (algProvider == null) {
            throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
        }

        var algorithm = algProvider.create(calculationDepth, evalStrategy, playerColor);
        if (!botSupportsAlgorithm(botProvider, algorithm.getClass())) {
            throw new IllegalArgumentException("Unsupported bot player + algorithm combination");
        }

        return create(botProvider, playerColor, algorithm);
    }

    private static boolean botSupportsAlgorithm(
            BotPlayerProvider<? extends BotAlgorithm> botProvider,
            Class<?> algorithm
    ) {
        return botProvider.algorithmType().isAssignableFrom(algorithm);
    }

    private static Class<?> getAlgorithmProviderReturnType(BotAlgorithmProvider provider) {
        try {
            Method createMethod = provider.getClass().getMethod("create", int.class, EvalStrategy.class, PlayerColor.class);
            return createMethod.getReturnType();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Algorithm provider must have a create() method", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <A extends BotAlgorithm> BotPlayer create(
            BotPlayerProvider<A> botProvider,
            PlayerColor color,
            BotAlgorithm algorithm
    ) {
        return botProvider.create(color, (A) algorithm);
    }

    // type safety for generics
    private static BotPlayerProvider<? extends BotAlgorithm> castSafely(
            BotPlayerProvider<?> provider
    ) {
        if (!BotAlgorithm.class.isAssignableFrom(provider.algorithmType())) {
            return null;
        }

        return provider;
    }
}