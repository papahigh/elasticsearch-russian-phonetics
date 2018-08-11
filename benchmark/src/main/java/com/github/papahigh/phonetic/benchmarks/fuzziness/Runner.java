package com.github.papahigh.phonetic.benchmarks.fuzziness;

import com.github.papahigh.phonetic.benchmarks.Config;

import java.io.IOException;
import java.util.stream.Collectors;

import static com.github.papahigh.phonetic.support.DictionaryLoader.loadDictionaryRaw;


public class Runner {

    public static void main(String[] args) throws IOException {
        new HistogramFileMaker(Config.getEncodersStream().collect(Collectors.toList()))
                .createPlotFiles("misspellings_and_typos", loadDictionaryRaw("orfo_and_typos.L1_5+PHON.csv"));
    }
}
