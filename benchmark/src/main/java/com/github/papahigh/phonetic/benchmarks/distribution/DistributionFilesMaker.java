package com.github.papahigh.phonetic.benchmarks.distribution;


import com.github.papahigh.phonetic.benchmarks.Config;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jooq.lambda.Unchecked;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.papahigh.phonetic.benchmarks.distribution.StatsCalculator.statsCalculator;
import static com.github.papahigh.phonetic.support.EncodersFactory.createEncoder;
import static java.util.concurrent.CompletableFuture.supplyAsync;


class DistributionFilesMaker {

    private final List<String> encoderWithoutMaxLength;
    private final List<String> encoderWithMaxLength;

    DistributionFilesMaker(List<String> encoderWithoutMaxLength, List<String> encoderWithMaxLength) {
        this.encoderWithoutMaxLength = encoderWithoutMaxLength;
        this.encoderWithMaxLength = encoderWithMaxLength;
    }

    void createPlotFiles(String location, List<String> dictionary) throws IOException {
        // ensure path is accessible before calculations
        Path directory = Config.resolvePath(location);

        Map<String, Map<Integer, StatsCalculator.DictionaryStats>> aggregatedResult = calculateStats(dictionary);
        MultiValuedMap<Integer, StatsCalculator.DictionaryStats> statsGroups = MultiMapUtils.newListValuedHashMap();
        encoderWithMaxLength.forEach(encoderName -> aggregatedResult.get(encoderName).forEach(statsGroups::put));
        encoderWithoutMaxLength.forEach(encoderName -> aggregatedResult.get(encoderName).forEach(statsGroups::put));

        // max.dat
        List<String> maxFileContent = new ArrayList<>();
        addFileHeader(maxFileContent, "# Max words with same code / maxCodeLength");

        // median.dat
        List<String> meanFileContent = new ArrayList<>();
        addFileHeader(meanFileContent, "# Mean words with same code / maxCodeLength");

        // codes.dat
        List<String> codesFileContent = new ArrayList<>();
        addFileHeader(codesFileContent, "# Unique codes in dictionary / maxCodeLength");

        IntStream.range(4, 15).forEach(maxCodeLength -> {

                    Collection<StatsCalculator.DictionaryStats> statsGroup = statsGroups.get(maxCodeLength);

                    maxFileContent.add(String.valueOf(maxCodeLength) + "\t" +
                            statsGroup.stream()
                                    .map(stats -> String.valueOf(stats.bucketSizes.getMax()))
                                    .collect(Collectors.joining("\t")));

                    meanFileContent.add(String.valueOf(maxCodeLength) + "\t" +
                            statsGroup.stream()
                                    .map(stats -> String.valueOf(stats.bucketSizes.getMean()))
                                    .collect(Collectors.joining("\t")));

                    codesFileContent.add(String.valueOf(maxCodeLength) + "\t" +
                            statsGroup.stream()
                                    .map(stats -> String.valueOf(stats.bucketSizes.getN()))
                                    .collect(Collectors.joining("\t")));
                }
        );

        Files.write(directory.resolve("max.dat"), maxFileContent);
        Files.write(directory.resolve("mean.dat"), meanFileContent);
        Files.write(directory.resolve("codes.dat"), codesFileContent);
    }

    private void addFileHeader(List<String> fileContent, String legend) {
        fileContent.add(legend);
        fileContent.add("Length\t" +
                String.join("\t", encoderWithMaxLength) + "\t" +
                String.join("\t", encoderWithoutMaxLength));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<Integer, StatsCalculator.DictionaryStats>> calculateStats(List<String> dictionary) {

        CompletableFuture<?>[] fixedLengthEncoderResults =
                encoderWithoutMaxLength
                        .stream()
                        .map(encoderName -> supplyAsync(() -> new ImmutableTriple<>(encoderName, 0,
                                statsCalculator(dictionary, createEncoder(encoderName)).calculate())))
                        .toArray((IntFunction<CompletableFuture<?>[]>) CompletableFuture[]::new);

        CompletableFuture<?>[] variableLengthEncoderResults =
                encoderWithMaxLength
                        .stream()
                        .flatMap(encoderName -> IntStream.range(4, 15).mapToObj(keyLength ->
                                supplyAsync(() -> new ImmutableTriple<>(encoderName, keyLength,
                                        statsCalculator(dictionary, createEncoder(encoderName, keyLength)).calculate()))))
                        .toArray((IntFunction<CompletableFuture<?>[]>) CompletableFuture[]::new);

        Map<String, Map<Integer, StatsCalculator.DictionaryStats>> aggregatedResult = new HashMap<>();

        // collect results from encoders with max length
        CompletableFuture.allOf(variableLengthEncoderResults).join();
        Arrays.stream(variableLengthEncoderResults)
                .map(Unchecked.function(CompletableFuture::get))
                .map(o -> (ImmutableTriple<String, Integer, StatsCalculator.DictionaryStats>) o)
                .forEach(triple -> aggregatedResult.computeIfAbsent(triple.getLeft(), v -> new TreeMap<>())
                        .put(triple.getMiddle(), triple.getRight())
                );

        // collect results from encoders without max length
        CompletableFuture.allOf(fixedLengthEncoderResults).join();
        Arrays.stream(fixedLengthEncoderResults)
                .map(Unchecked.function(CompletableFuture::get))
                .map(o -> (ImmutableTriple<String, Integer, StatsCalculator.DictionaryStats>) o)
                .forEach(triple -> IntStream.range(4, 15).forEach(maxCodeLength -> aggregatedResult.computeIfAbsent(triple.getLeft(), v -> new TreeMap<>())
                        .put(maxCodeLength, triple.getRight()))
                );

        return aggregatedResult;
    }
}
