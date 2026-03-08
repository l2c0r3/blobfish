package ch.hslu.cas.msed.blobfish.base;

public class BoardTransformationUtil {

    private BoardTransformationUtil() {
        // utility class
    }

    public static int flipRankIndex(int index) {
        // bitwise XOR operation -> same as `(7 - row) * 8 + col;`
        return index ^ 56;
    }

    public static int flipFileIndex(int index) {
        // bitwise XOR operation -> same as `row * 8 + 7 - col;`
        return index ^ 7;
    }

    public static int rotateBoardIndex(int index) {
        return 63 - index;
    }
}