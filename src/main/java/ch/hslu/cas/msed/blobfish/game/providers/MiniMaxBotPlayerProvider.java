package ch.hslu.cas.msed.blobfish.game.providers;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.player.bot.BotPlayer;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxAlgo;
import ch.hslu.cas.msed.blobfish.player.bot.minimax.MiniMaxBotPlayer;

public class MiniMaxBotPlayerProvider implements BotPlayerProvider<MiniMaxAlgo> {
    private static final String BOT_NAME = "minimax";

    @Override
    public String getBotName() {
        return BOT_NAME;
    }

    @Override
    public Class<MiniMaxAlgo> algorithmType() {
        return MiniMaxAlgo.class;
    }

    @Override
    public BotPlayer create(PlayerColor playerColor, MiniMaxAlgo botAlgorithm) {
        return new MiniMaxBotPlayer(playerColor, botAlgorithm);
    }
}
