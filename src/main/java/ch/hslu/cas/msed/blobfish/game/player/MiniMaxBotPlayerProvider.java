package ch.hslu.cas.msed.blobfish.game.player;

import ch.hslu.cas.msed.blobfish.base.PlayerColor;
import ch.hslu.cas.msed.blobfish.minimax.base.MiniMaxAlgo;

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
