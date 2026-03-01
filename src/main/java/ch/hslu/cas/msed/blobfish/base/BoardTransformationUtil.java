package ch.hslu.cas.msed.blobfish.base;

public class BoardTransformationUtil {

    private BoardTransformationUtil() {
        // utility class
    }

    public static int flipRankIndex(int index) {
        // bitwise XOR operation -> same as `(7 - row) * 8 + col;`
        return index ^ 56;
    }
}