package com.github.papahigh.phonetic.benchmarks.distribution;

import com.github.papahigh.phonetic.VowelsMode;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;

import static com.github.papahigh.phonetic.benchmarks.distribution.StatsCalculator.statsCalculator;
import static com.github.papahigh.phonetic.support.EncodersFactory.russianPhonetic;


@RunWith(BlockJUnit4ClassRunner.class)
abstract class AbstractDistributionTests extends TestCase {

    abstract List<String> getDictionary();

    StatsCalculator.DictionaryStats getDictionaryStats(VowelsMode vowelsMode, int maxCodeLength) {
        return statsCalculator(getDictionary(), russianPhonetic(vowelsMode, maxCodeLength))
                .withMaxBucketStats()
                .calculate();
    }
}
