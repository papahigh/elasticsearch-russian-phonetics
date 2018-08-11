package com.github.papahigh.phonetic.benchmarks.fuzziness;

import com.github.papahigh.phonetic.benchmarks.Config;
import com.github.papahigh.phonetic.support.EncodersFactory;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jooq.lambda.Unchecked;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class HistogramFileMaker {

    private final List<String> encoders;

    HistogramFileMaker(List<String> encoders) {
        this.encoders = encoders;
    }

    void createPlotFiles(String location, List<String> dictionary) throws IOException {
        // ensure path is accessible before calculations
        Path directory = Config.resolvePath(location);

        Map<String, SummaryStatistics> stats = calculateStats(dictionary);

        // results.dat
        List<String> fileContent = new ArrayList<>();
        addFileHeader(fileContent, "# Encoder / Percent of matches (" + (dictionary.size() - 1) + " total)");

        stats.entrySet()
                .stream()
                .map(entry -> new ImmutableTriple<>(
                                entry.getKey(),
                                BigDecimal.valueOf(entry.getValue().getN() * 100)
                                        .divide(BigDecimal.valueOf(dictionary.size()), RoundingMode.HALF_UP)
                                        .doubleValue(),
                                entry.getValue().getN()
                        )
                )
                .sorted(Comparator.comparing(o -> o.middle + o.right))
                .forEach(triple -> fileContent.add(triple.left + "\t" + triple.middle + "\t" + triple.right + "\t" + Config.getEncoderStyle(triple.left)));


        Files.write(directory.resolve("results.dat"), fileContent);
    }

    private Map<String, SummaryStatistics> calculateStats(List<String> dictionary) {
        Map<String, SummaryStatistics> stats = new HashMap<>();

        encoders.forEach(name ->
                dictionary.stream()
                        .skip(1)
                        .forEach(Unchecked.consumer(test -> doTest(
                                // stats
                                stats.computeIfAbsent(name, (n) -> new AggregateSummaryStatistics().createContributingStatistics()),
                                // encoder
                                EncodersFactory.createEncoder(name),
                                // test string
                                test
                                )
                        ))
        );

        dictionary.stream()
                .skip(1)
                .forEach(test -> doTestLevenshtein(
                        // stats
                        stats.computeIfAbsent("Levenshtein<3", (n) -> new AggregateSummaryStatistics().createContributingStatistics()),
                        // test string
                        test,
                        3
                ));

        return stats;
    }

    private static void addFileHeader(List<String> fileContent, String legend) {
        fileContent.add(legend);
        fileContent.add("Encoder\tMatchPercent\tSum");
    }

    private static Double getScoreForLine(StringEncoder encoder, String line) throws EncoderException {
        String[] array = line.split(";");
        if (encoder.encode(array[0]).equals(encoder.encode(array[1]))) {
            return Double.parseDouble(array[2]);
        } else {
            return 0.0;
        }
    }

    private static Double getLevenstainScoreForLine(String line, int maxLevenstain) {
        String[] array = line.split(";");
        if (new LevenshteinDistance().apply(array[0], array[1]) < maxLevenstain) {
            return Double.parseDouble(array[2]);
        } else {
            return 0.0;
        }
    }

    private static void doTestLevenshtein(SummaryStatistics stats, String line, int levenstain) {
        double score = getLevenstainScoreForLine(line, levenstain);
        if (score > 0) {
            stats.addValue(score);
        }
    }

    private static void doTest(SummaryStatistics stats, StringEncoder encoder, String line) throws EncoderException {
        double score = getScoreForLine(encoder, line);
        if (score > 0) {
            stats.addValue(score);
        }
    }
}
