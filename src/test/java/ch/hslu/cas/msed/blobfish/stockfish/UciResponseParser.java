package ch.hslu.cas.msed.blobfish.stockfish;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UciResponseParser {

    private static final Pattern infoRegex = Pattern.compile("info depth (\\d+) seldepth (\\d+) multipv (\\d+) score cp (\\d+) nodes (\\d+) nps (\\d+) hashfull (\\d+) tbhits (\\d+) time (\\d+) pv (.+)");

    public List<String> parseBestMoves(String... response) {
        if (response == null || response.length == 0) {
            throw new IllegalArgumentException("Response cannot be null or empty");
        }

        var resultLines = Arrays.stream(response)
                .filter(i -> infoRegex.matcher(i).matches())
                .toList();

        var maxCalculatedDepth = resultLines.stream()
                        .map(infoRegex::matcher)
                        .filter(Matcher::find)
                        .mapToInt(m -> Integer.parseInt(m.group(1)))
                        .max()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid depth"));

        return resultLines.stream()
                        .filter(l -> l.startsWith("info depth " + maxCalculatedDepth))
                        .map(infoRegex::matcher)
                        .filter(Matcher::find)
                        .map(m -> m.group(10))
                        .map(String::trim)
                        .map(m -> m.split(" ")[0])
                        .toList();
    }
}
