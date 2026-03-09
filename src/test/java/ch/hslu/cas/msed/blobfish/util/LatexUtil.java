package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.QualityTest;
import ch.hslu.cas.msed.blobfish.QualityTest.QualityTestResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LatexUtil {

    private LatexUtil() {
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
                        >{\\raggedright\\arraybackslash}p{0.33\\textwidth}|
                        >{\\centering\\arraybackslash}p{0.12\\textwidth}|
                        >{\\raggedright\\arraybackslash}p{0.27\\textwidth}|
                        >{\\centering\\arraybackslash}p{0.12\\textwidth}|
                        >{\\centering\\arraybackslash}p{0.08\\textwidth}|}
                \\caption{%s} \\\\
                \\hline
                \\multicolumn{5}{|c|}{\\textbf{%s}} \\\\ \\hline
                \\textbf{FEN} & \\textbf{Stockfish move} & \\textbf{Evaluation} & \\textbf{Move} & \\textbf{Pkt} \\\\ \\hline
                \\endfirsthead
                
                \\hline
                \\multicolumn{5}{|c|}{\\textbf{%s} -- Fortsetzung} \\\\ \\hline
                \\textbf{FEN} & \\textbf{Stockfish move} & \\textbf{Evaluation} & \\textbf{Move} & \\textbf{Pkt} \\\\ \\hline
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
                .forEach(result -> sb.append(generateTableRow(result)));

        sb.append("""
                \\end{longtable}
                \\end{document}
                """);

        var tmpFile = FileUtil.createTmpFile("qualityMoves", "tex");
        try (FileWriter fw = new FileWriter(tmpFile)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tmpFile;
    }

    private static String generateTableRow(QualityTestResult testResult) {
        StringBuilder sb = new StringBuilder();

        int rowCount = testResult.evalQualityResult().size();

        // TODO: Check if higher chunk size possible
        var fenCutted = String.join("\\\\", splitFenForLatex(testResult.fen(), 9));
        sb.append("""
                \\multirow{%d}{*}{
                    \\begin{tabular}{l}
                        %s
                    \\end{tabular}
                }
                """.formatted(rowCount, fenCutted));

        // TODO: nummerierte liste
        var stockFishMoves = testResult.stockfishMove().stream()
                .map(s -> "- " + escapeLatex(s))
                .collect(Collectors.joining("\\\\"));
        sb.append("""
                &
                \\multirow{%d}{*}{
                    \\begin{tabular}{c}
                        %s
                    \\end{tabular}
                }
                """.formatted(rowCount, stockFishMoves));

        var sortedEvalQualityResult = testResult.evalQualityResult().stream()
                .sorted(Comparator.comparing((QualityTest.EvalQualityResult r) -> r.strategy().description()))
                .toList();

        for (int i = 0; i < rowCount; i++) {
            var result = sortedEvalQualityResult.get(i);
            if (i != 0) {
                sb.append("&");
            }

            var strategyDescription = escapeLatex(result.strategy().description());
            var move = escapeLatex(result.move());

            sb.append("""
                    & %s & %s & %s \\\\
                    """.formatted(strategyDescription, move, result.pointWon()));

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

    private static String escapeLatex(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\textbackslash{}")
                .replace("&", "\\&")
                .replace("%", "\\%")
                .replace("$", "\\$")
                .replace("#", "\\#")
                .replace("_", "\\_")
                .replace("{", "\\{")
                .replace("}", "\\}");
    }
}