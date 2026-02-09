package ch.hslu.cas.msed.blobfish.stockfish;

import java.util.List;

public class UciResponseParser {

    public List<String> parseBestMoves(String... response) {
        if (response == null || response.length == 0) {
            throw new IllegalArgumentException("Response cannot be null or empty");
        }


        return List.of("blub");
    }
}
