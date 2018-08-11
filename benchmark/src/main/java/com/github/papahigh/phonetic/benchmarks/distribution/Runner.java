package com.github.papahigh.phonetic.benchmarks.distribution;

import com.github.papahigh.phonetic.benchmarks.Config;
import org.jooq.lambda.Unchecked;

import static com.github.papahigh.phonetic.benchmarks.Config.getFileName;
import static com.github.papahigh.phonetic.benchmarks.Config.getFixedLengthEncoders;
import static com.github.papahigh.phonetic.benchmarks.Config.getParameterizedLengthEncoders;
import static com.github.papahigh.phonetic.support.DictionaryLoader.loadDictionary;


public class Runner {

    public static void main(String[] args) {

        DistributionFilesMaker filesMaker = new DistributionFilesMaker(getFixedLengthEncoders(), getParameterizedLengthEncoders());
        Config.getDictionariesStream().forEach(Unchecked.consumer(dictionary ->
                filesMaker.createPlotFiles(dictionary, loadDictionary(getFileName(dictionary))))
        );
    }
}
