package com.github.papahigh.phonetic.benchmarks.distribution;

import com.github.papahigh.phonetic.VowelsMode;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;


public class AllVowelsEncoderOrthographicDistributionTests extends RussianOrthographicDistributionTests {

    @Test
    public void testOrthographicMaxLength6() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_ALL, 6);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(6));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(337));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.57));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(23));
    }

    @Test
    public void testOrthographicMaxLength8() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_ALL, 8);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(8));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(39));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.64));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(15));
    }

    @Test
    public void testOrthographicMaxLength10() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_ALL, 10);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(10));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(15));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.78));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(10));
    }

    @Test
    public void testOrthographicMaxLength12() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_ALL, 12);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(12));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(12));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.69));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(12));
    }

    @Test
    public void testOrthographicMaxLength14() {

        StatsCalculator.DictionaryStats stats = getDictionaryStats(VowelsMode.ENCODE_ALL, 14);

        assertThat(stats.maxCodeLength, lessThanOrEqualTo(14));

        assertThat(stats.maxBucketStats.getSize(), lessThanOrEqualTo(8));
        assertThat(stats.maxBucketStats.getMinJaroWinklerDistance(), greaterThanOrEqualTo(0.58));
        assertThat(stats.maxBucketStats.getMaxLevenshteinDistance(), lessThanOrEqualTo(4));
    }
}
