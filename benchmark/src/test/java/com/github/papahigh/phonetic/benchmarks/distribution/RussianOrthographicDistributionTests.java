package com.github.papahigh.phonetic.benchmarks.distribution;


import org.junit.BeforeClass;

import java.util.List;

import static com.github.papahigh.phonetic.support.DictionaryLoader.loadDictionary;


public abstract class RussianOrthographicDistributionTests extends AbstractDistributionTests {

    private static List<String> spellingDictionary;

    @BeforeClass
    public static void setup() {
        spellingDictionary = loadDictionary("dictionary-russian-orthography.txt");
    }

    @Override
    List<String> getDictionary() {
        return spellingDictionary;
    }
}
