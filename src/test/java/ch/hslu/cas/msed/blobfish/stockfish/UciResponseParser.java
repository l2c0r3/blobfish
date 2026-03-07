package ch.hslu.cas.msed.blobfish.stockfish;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UciResponseParser {

    private static final Pattern infoRegex = Pattern.compile("^info depth (\\d+)\\b.*\\bmultipv (\\d+)\\b.*\\bscore (?:cp|mate) -?\\d+\\b.*\\bpv\\s+(.+)$");

    public List<String> parseBestMoves(String... response) {
        if (response == null || response.length == 0) {
            throw new IllegalArgumentException("Response cannot be null or empty");
        }

        var resultLines = Arrays.stream(response)
                .filter(i -> infoRegex.matcher(i).matches())
                .toList();

        var maxCalculatedDepth = resultLines.stream()
                .map(infoRegex::matcher)
                .filter(Matcher::matches)
                .mapToInt(m -> Integer.parseInt(m.group(1)))
                .max()
                .orElseThrow(() -> new IllegalArgumentException("Invalid depth"));

        return resultLines.stream()
                .map(infoRegex::matcher)
                .filter(Matcher::matches)
                .filter(m -> Integer.parseInt(m.group(1)) == maxCalculatedDepth)
                .sorted(java.util.Comparator.comparingInt(m -> Integer.parseInt(m.group(2))))
                .map(m -> m.group(3))
                .map(String::trim)
                .map(m -> m.split("\\s+")[0])
                .toList();
    }
}
