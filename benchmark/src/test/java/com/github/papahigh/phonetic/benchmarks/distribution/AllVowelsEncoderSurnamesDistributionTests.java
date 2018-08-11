package com.github.papahigh.phonetic.benchmarks.distribution;

import com.github.papahigh.phonetic.VowelsMode;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;


public class AllVowelsEncoderSurnamesDistributionTests extends RussianSurnamesDistributionTests {

    @Test
    public void testSurnamesMaxLength6() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_ALL, 6);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(6));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(183));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.6));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(11));
    }

    @Test
    public void testSurnamesMaxLength8() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_ALL, 8);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(8));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(40));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.76));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(8));
    }

    @Test
    public void testSurnamesMaxLength10() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_ALL, 10);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(10));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(17));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.69));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(6));

    }

    @Test
    public void testSurnamesMaxLength12() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_ALL, 12);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(12));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(16));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.8));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(3));
    }

    @Test
    public void testSurnamesMaxLength14() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_ALL, 14);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(14));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(16));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.8));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(3));
    }
}
