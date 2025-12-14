package ch.hslu.cas.msed.blobfish.board.ui;

class UiBoard {

    private final UiField[][] fields = new UiField[8][8];

    UiField get(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            throw new IllegalArgumentException("Row and column must be between 0 and 7");
        }
        return fields[row][col];
    }

    void set(int row, int col, UiField f) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            throw new IllegalArgumentException("Row and column must be between 0 and 7");
        }
        fields[row][col] = f;
    }

    public UiField[][] getAllFields() {
        UiField[][] copy = new UiField[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(fields[i], 0, copy[i], 0, 8);
        }
        return copy;
    }
}
