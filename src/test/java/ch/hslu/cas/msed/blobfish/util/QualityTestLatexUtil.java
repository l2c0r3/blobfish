package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.QualityTest;
import ch.hslu.cas.msed.blobfish.QualityTest.QualityTestResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QualityTestLatexUtil {

    private QualityTestLatexUtil() {
        // util class
    }

    public static File generateTableQualityMoves(List<QualityTestResult> results,
                                                 QualityTest.QualityTestCategory tacticTableToGenerate) {
        StringBuilder sb = new StringBuilder();

        var tacticDescription = escapeLatex(tacticTableToGenerate.getDescription());
        var caption = escapeLatex("Quality Test von " + tacticTableToGenerate.getDescription());

        sb.append("""
                \\documentclass{article}
                
                \\usepackage[a4paper,margin=2cm]{geometry}
                \\usepackage{array}
                \\usepackage{multirow}
                \\usepackage{longtable}
                
                \\begin{document}
                \\renewcommand{\\arraystretch}{1.2}
                \\setlength{\\LTleft}{0pt}
                \\setlength{\\LTright}{0pt}
                
                \\begin{longtable}{|
                        >{\\raggedright\\arraybackslash}p{0.18\\textwidth}|
                        >{\\centering\\arraybackslash}p{0.14\\textwidth}|
                        >{\\raggedright\\arraybackslash}p{0.22\\textwidth}|
                        >{\\centering\\arraybackslash}p{0.30\\textwidth}|
                        >{\\centering\\arraybackslash}p{0.05\\textwidth}|}
                \\caption{%s} \\\\
                \\hline
                \\multicolumn{5}{|c|}{\\textbf{%s}} \\\\ \\hline
                \\textbf{FEN} & \\textbf{Stockfish best next move} & \\textbf{Evaluation} & \\textbf{Move} & \\textbf{Pkt.} \\\\ \\hline
                \\endfirsthead
                
                \\hline
                \\multicolumn{5}{|c|}{\\textbf{%s} -- Fortsetzung} \\\\ \\hline
                \\textbf{FEN} & \\textbf{Stockfish best next move} & \\textbf{Evaluation} & \\textbf{Move} & \\textbf{Pkt.} \\\\ \\hline
                \\endhead
                
                \\hline
                \\multicolumn{5}{|r|}{Fortsetzung auf der nächsten Seite} \\\\
                \\hline
                \\endfoot
                
                \\hline
                \\endlastfoot
                """.formatted(caption, tacticDescription, tacticDescription));

        results.stream()
                .filter(testResult -> tacticTableToGenerate.equals(testResult.qualityTestCategory()))
                .forEach(result -> sb.append(generateTableRowQualityMoves(result)));

        sb.append("""
                \\end{longtable}
                \\end{document}
                """);

        return generateFile("qualityMoves", sb);
    }

    private static String generateTableRowQualityMoves(QualityTestResult testResult) {
        StringBuilder sb = new StringBuilder();

        int rowCount = testResult.evalQualityResult().size();

        // Add fen
        var fenCutted = String.join("\\\\", splitFenForLatex(testResult.fen(), 9));
        sb.append("""
                \\multirow{%d}{*}{
                    \\begin{tabular}{l}
                        %s
                    \\end{tabular}
                }
                """.formatted(rowCount, fenCutted));

        // Add stockfish moves
        StringBuilder stockFishRanglist = new StringBuilder();
        for (int i = 0; i < testResult.stockfishMove().size(); i++) {
            stockFishRanglist.append(i + 1).append(". ")
                    .append(testResult.stockfishMove().get(i));
            if (i != testResult.stockfishMove().size() - 1) {
                stockFishRanglist.append("\\\\");
            }
        }
        sb.append("""
                &
                \\multirow{%d}{*}{
                    \\begin{tabular}{c}
                        %s
                    \\end{tabular}
                }
                """.formatted(rowCount, escapeLatex(stockFishRanglist.toString())));

        // ensure always same order
        var sortedEvalQualityResult = testResult.evalQualityResult().stream()
                .sorted(Comparator.comparing((QualityTest.EvalQualityResult r) -> r.strategy().description()))
                .toList();

        // add evalname + moves
        for (int i = 0; i < rowCount; i++) {
            var result = sortedEvalQualityResult.get(i);
            if (i != 0) {
                sb.append("&");
            }

            var strategyDescription = escapeLatex(result.strategy().description());
            var moves = String.join(", ", result.pathEvaluation().move());

            sb.append("""
                    & %s & %s & %s \\\\
                    """.formatted(strategyDescription, escapeLatex(moves), result.pointWon()));

            if (i != rowCount - 1) {
                sb.append("\\cline{3-5}\n");
            } else {
                sb.append("\\hline\n");
            }
        }

        return sb.toString();
    }

    private static List<String> splitFenForLatex(String fen, int chunkSize) {
        String clean = escapeLatex(fen);
        return clean.lines()
                .flatMap(line -> chunkString(line, chunkSize).stream())
                .toList();
    }

    private static List<String> chunkString(String input, int chunkSize) {
        var result = new java.util.ArrayList<String>();
        for (int i = 0; i < input.length(); i += chunkSize) {
            result.add(input.substring(i, Math.min(input.length(), i + chunkSize)));
        }
        return result;
    }

    public static File generateOverallSum(List<QualityTestResult> results, int maxPoints) {
        StringBuilder sb = new StringBuilder();

        var header = String.format("\\textbf{Rang} & \\textbf{Evaluation} & \\textbf{Gewonnene Punkte von %d} \\\\ \\hline", maxPoints);
        sb.append("""
                \\documentclass{article}
                
                \\usepackage[a4paper,margin=2cm]{geometry}
                \\usepackage{array}
                \\usepackage{multirow}
                \\usepackage{longtable}
                
                \\begin{document}
                    \\begin{longtable}{|
                            >{\\centering\\arraybackslash}p{0.10\\textwidth}|
                            >{\\raggedright\\arraybackslash}p{0.70\\textwidth}|
                            >{\\centering\\arraybackslash}p{0.20\\textwidth}|}
                        \\caption{Quality Test Rangliste} \\\\
                """);
        sb.append("""
                \\hline
                    %s
                \\endfirsthead
                \\hline
                    %s
                \\endhead
                
                \\hline
                \\multicolumn{3}{|r|}{Fortsetzung auf der nächsten Seite} \\\\
                \\hline
                \\endfoot
                
                \\hline
                \\endlastfoot
                """.formatted(header, header));

        var sortedEntries = results.stream()
                .flatMap(r -> r.evalQualityResult().stream())
                .collect(Collectors.groupingBy(
                        QualityTest.EvalQualityResult::strategy,
                        Collectors.summingInt(QualityTest.EvalQualityResult::pointWon)
                ))
                .entrySet().stream()
                .sorted(
                        Map.Entry.<EvaluationUtil.EvalConfig, Integer>comparingByValue()
                                .reversed()
                )
                .toList();

        String rangliste = IntStream.range(0, sortedEntries.size())
                .mapToObj(i -> {
                    var entry = sortedEntries.get(i);
                    return (i + 1) + " & " + escapeLatex(entry.getKey().description()) + " & " + entry.getValue() + " \\\\ \\hline";
                })
                .collect(Collectors.joining("\n"));
        sb.append(rangliste);

        sb.append("""
                    \\end{longtable}
                \\end{document}
                
                """);

        return generateFile("overall", sb);
    }

    private static String escapeLatex(String s) {
        if (s == null) return "";
        return s
                .replace("&", "\\&")
                .replace("%", "\\%");
    }


    private static File generateFile(String filename, StringBuilder sb) {
        var tmpFile = FileUtil.createTmpFile(filename, "tex");
        try (FileWriter fw = new FileWriter(tmpFile)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tmpFile;
    }


}