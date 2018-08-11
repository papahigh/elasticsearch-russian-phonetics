package com.github.papahigh.phonetic.benchmarks.distribution;

import com.github.papahigh.phonetic.VowelsMode;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class FirstVowelEncoderOrthographicDistributionTests extends RussianOrthographicDistributionTests {

    @Test
    public void testOrthographicMaxLength6() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_FIRST, 6);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(6));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(42));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.39));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(6));
    }

    @Test
    public void testOrthographicMaxLength8() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_FIRST, 8);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(8));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(42));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.39));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(6));
    }

    @Test
    public void testOrthographicMaxLength10() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_FIRST, 10);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(10));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(42));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.39));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(6));
    }

    @Test
    public void testOrthographicMaxLength12() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_FIRST, 12);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(12));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(42));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.39));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(6));
    }

    @Test
    public void testOrthographicMaxLength14() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_FIRST, 14);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(14));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(42));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.39));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(6));
    }
}
