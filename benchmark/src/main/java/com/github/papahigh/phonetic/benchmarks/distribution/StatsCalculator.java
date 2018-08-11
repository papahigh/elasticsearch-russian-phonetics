package com.github.papahigh.phonetic.benchmarks.distribution;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


class StatsCalculator {

    private final StringEncoder encoder;
    private final List<String> dictionary;

    private boolean calcMaxBucketStats;

    private StatsCalculator(List<String> dictionary, StringEncoder encoder) {
        this.dictionary = dictionary;
        this.encoder = encoder;
    }

    static StatsCalculator statsCalculator(List<String> dictionary, StringEncoder encoder) {
        return new StatsCalculator(dictionary, encoder);
    }

    StatsCalculator withMaxBucketStats() {
        this.calcMaxBucketStats = true;
        return this;
    }

    DictionaryStats calculate() {

        MultiValuedMap<String, String> wordsByKey = MultiMapUtils.newSetValuedHashMap();
        for (String word : dictionary) {
            try {
                wordsByKey.put(encoder.encode(word), word);
            } catch (EncoderException e) {
                throw new RuntimeException(e);
            }
        }

        DictionaryStats results = new DictionaryStats(dictionary.size(), wordsByKey.keySet().size());
        wordsByKey.keySet().forEach(s -> results.addBucketStats(s, wordsByKey.get(s).size()));

        if (calcMaxBucketStats) {

            Map.Entry<String, Collection<String>> maxBucket = wordsByKey.asMap()
                    .entrySet()
                    .stream()
                    .max(Comparator.comparingInt(o -> o.getValue().size()))
                    .orElseThrow(IllegalArgumentException::new);

            BucketStats maxBucketStats = new BucketStats(maxBucket.getKey(), maxBucket.getValue().size());
            maxBucket.getValue().forEach(word1 ->
                    maxBucket.getValue().forEach(word2 -> {
                        if (!word1.equals(word2)) {
                            maxBucketStats.addJaroJaroWinklerDistance(new JaroWinklerDistance().apply(word1, word2));
                            maxBucketStats.addLevenshteinDistance(new LevenshteinDistance().apply(word1, word2));
                        }
                    })
            );

            results.maxBucketStats = maxBucketStats;
        }

        return results;
    }


    public static class DictionaryStats {

        final int words;
        final int codes;
        final SummaryStatistics bucketSizes = new AggregateSummaryStatistics().createContributingStatistics();
        final Frequency frequency = new Frequency();

        // bucketSizes for largest group of words with same code
        BucketStats maxBucketStats;

        // max length probe
        String maxCode;
        int maxCodeLength;

        DictionaryStats(int words, int codes) {
            this.words = words;
            this.codes = codes;
        }

        void addBucketStats(String code, Integer size) {
            frequency.addValue(size);
            bucketSizes.addValue(size);
            int codeLength = code.length();
            if (maxCodeLength < codeLength) {
                maxCode = code;
                maxCodeLength = codeLength;
            }
        }

        @Override
        public String toString() {
            return "DictionaryStats{" +
                    "words=" + words +
                    ", codes=" + codes +
                    ", bucketSizes=" + bucketSizes +
                    ", maxBucketStats=" + maxBucketStats +
                    ", maxCode='" + maxCode + '\'' +
                    ", maxCodeLength=" + maxCodeLength +
                    '}';
        }
    }

    static class BucketStats {
        final String code;
        final int size;
        private double minJaroWinklerDistance = 1.0;
        private int maxLevenshteinDistance = 0;

        BucketStats(String code, int size) {
            this.code = code;
            this.size = size;
        }


        void addJaroJaroWinklerDistance(double JaroWinklerDistance) {
            if (this.minJaroWinklerDistance > JaroWinklerDistance) {
                this.minJaroWinklerDistance = JaroWinklerDistance;
            }
        }

        void addLevenshteinDistance(int levenshteinDistance) {
            if (this.maxLevenshteinDistance < levenshteinDistance) {
                this.maxLevenshteinDistance = levenshteinDistance;
            }
        }

        String getCode() {
            return code;
        }

        int getSize() {
            return size;
        }

        double getMinJaroWinklerDistance() {
            return minJaroWinklerDistance;
        }

        int getMaxLevenshteinDistance() {
            return maxLevenshteinDistance;
        }

        @Override
        public String toString() {
            return "BucketStats{" +
                    "\n\tcode='" + code + '\'' +
                    "\n\tsize=" + size +
                    "\n\tminJaroWinklerDistance=" + minJaroWinklerDistance +
                    "\n\tmaxLevenshteinDistance=" + maxLevenshteinDistance +
                    '}';
        }
    }
}
