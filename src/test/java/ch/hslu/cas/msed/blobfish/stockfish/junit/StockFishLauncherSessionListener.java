package ch.hslu.cas.msed.blobfish.stockfish.junit;

import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

import java.io.IOException;

/**
 * <a href="https://docs.junit.org/6.0.3/advanced-topics/launcher-api.html">Junit Launcher</a>
 * to have a global stockfish container in test run
 */
public class StockFishLauncherSessionListener implements LauncherSessionListener {

    @Override
    public void launcherSessionOpened(LauncherSession session) {
        try {
            StockFishSingleton.getOrStart();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void launcherSessionClosed(LauncherSession session) {
        try {
            StockFishSingleton.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
