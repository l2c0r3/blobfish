package ch.hslu.cas.msed.blobfish.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {

    private FileUtil() {
        // util class
    }

    public static File createTmpFile(String prefix, String suffix) {
        try {
            return Files.createTempFile(prefix, suffix).toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
