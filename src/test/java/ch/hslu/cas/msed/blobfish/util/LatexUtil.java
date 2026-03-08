package ch.hslu.cas.msed.blobfish.util;

import ch.hslu.cas.msed.blobfish.QualityTest;
import ch.hslu.cas.msed.blobfish.QualityTest.QualityTestResult;
import ch.hslu.cas.msed.blobfish.eval.MateAwareEval;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LatexUtil {


    public static File generateTableQualityMoves(List<QualityTestResult> results, QualityTest.QualityTestCategory tacticTableToGenerate) {
        StringBuilder sb = new StringBuilder();

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
                """.formatted(tacticTableToGenerate.getDescription()));

        results.forEach(result -> sb.append(generateTableRow(result)));

        var caption = "Quality Test von " + tacticTableToGenerate.getDescription();
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
        var fenCutted = String.join("\\\\", testResult.fen().split("(?<=\\G.........)"));
        sb.append("""
                \\multirow{7}{*}{
                    \\begin{tabular}{l}
                        %s
                    \\end{tabular}
                }
                """.formatted(fenCutted));

        var stockFishMoves = testResult.stockfishMove().stream()
                .map(s -> "- " + s)
                .collect(Collectors.joining("\\\\"));
        sb.append("""
                &
                \\multirow{7}{*}{
                    \\begin{tabular}{c}
                        %s
                    \\end{tabular}
                }
                """.formatted(stockFishMoves));

        for (int i = 0; i < testResult.evalQualityResult().size(); i++) {
            var result = testResult.evalQualityResult().get(i);
            if (i != 0) {
                sb.append("&");
            }
            sb.append("""
                    & %s & %s & %s \\\\
                    """.formatted(result.strategy().description(), result.move(), result.pointWon()));

            if (i != testResult.evalQualityResult().size() - 1) {
                sb.append("\\cline{3-5}");
            }
        }
        return sb.toString();
    }

    @Test
    void blub() {
        List<QualityTestResult> testResults = List.of(
                new QualityTestResult(
                        QualityTest.QualityTestCategory.TACTICS,
                        "6k1/5ppp/5r2/8/8/5Q2/5PPP/6K1 w - - 0 1",
                        List.of("Qxf6", "Qh3", "Qa8+"),
                        List.of(
                                new QualityTest.EvalQualityResult(new EvaluationUtil.EvalConfig(new MateAwareEval(), "mate"), "Qxf6", 5),
                                new QualityTest.EvalQualityResult(new EvaluationUtil.EvalConfig(new MateAwareEval(), "mate"), "Qxf6", 5),
                                new QualityTest.EvalQualityResult(new EvaluationUtil.EvalConfig(new MateAwareEval(), "mate"), "Qxf6", 5)
                        )
                )
        );

        var file = LatexUtil.generateTableQualityMoves(testResults, QualityTest.QualityTestCategory.TACTICS);
        System.out.println("blub");
    }
}
