package com.github.papahigh.phonetic.benchmarks.distribution;


import org.junit.BeforeClass;

import java.util.List;

import static com.github.papahigh.phonetic.support.DictionaryLoader.loadDictionary;


public abstract class RussianSurnamesDistributionTests extends AbstractDistributionTests {

    private static List<String> surnamesDictionary;

    @BeforeClass
    public static void setup() {
        surnamesDictionary = loadDictionary("dictionary-russian-surnames.txt");
    }

    @Override
    List<String> getDictionary() {
        return surnamesDictionary;
    }
}
