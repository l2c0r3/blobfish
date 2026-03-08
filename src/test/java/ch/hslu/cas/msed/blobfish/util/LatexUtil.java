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

    public static File generateTableQualityMoves(List<QualityTestResult> results, QualityTest.QualityTestCategory tacticTableToGenerate) {
        StringBuilder sb = new StringBuilder();

        var tacticDescription = escapeLatex(tacticTableToGenerate.getDescription());

        sb.append("""
                \\documentclass{article}
                
                \\usepackage{array}
                \\usepackage{multirow}
                \\usepackage{rotating}
                \\usepackage{tabularx}
                
                \\newcolumntype{Y}{>{\\raggedright\\arraybackslash}X}
                
                \\begin{document}
                    \\begin{table}[h]
                            \\renewcommand{\\arraystretch}{1.2}
                            
                            \\begin{tabular}{|
                                    >{\\raggedright\\arraybackslash}p{0.33\\textwidth}|
                                    >{\\centering\\arraybackslash}p{0.12\\textwidth}|
                                    >{\\raggedright\\arraybackslash}p{0.27\\textwidth}|
                                    >{\\centering\\arraybackslash}p{0.12\\textwidth}|
                                    >{\\centering\\arraybackslash}p{0.08\\textwidth}|}
                                \\hline
                                \\multicolumn{5}{|c|}{\\textbf{%s}} \\\\ \\hline
                                \\textbf{FEN} & \\textbf{Stockfish move} & \\textbf{Evaluation} & \\textbf{Move} & \\textbf{Pkt} \\\\ \\hline
                """.formatted(tacticDescription));

        results.stream()
                .filter(testResult -> tacticTableToGenerate.equals(testResult.qualityTestCategory()))
                .forEach(result -> sb.append(generateTableRow(result)));

        var caption = "Quality Test von " + tacticDescription;
        sb.append(
                """
                        \\hline
                        \\end{tabular}
                        \\centering
                        \\caption{%s}
                    \\end{table}
                \\end{document}
                """.formatted(caption));


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

        var fenCutted = String.join("\\\\", testResult.fen().split("(?<=\\G.........)"));
        sb.append("""
                \\multirow{%d}{*}{
                    \\begin{tabular}{l}
                        %s
                    \\end{tabular}
                }
                """.formatted(rowCount, fenCutted));

        var stockFishMoves = testResult.stockfishMove().stream()
                .map(s -> "- " + s)
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
            sb.append("""
                    & %s & %s & %s \\\\
                    """.formatted(strategyDescription, result.move(), result.pointWon()));

            if (i != rowCount - 1) {
                sb.append("\\cline{3-5}\n");
            }
        }

        return sb.toString();
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
