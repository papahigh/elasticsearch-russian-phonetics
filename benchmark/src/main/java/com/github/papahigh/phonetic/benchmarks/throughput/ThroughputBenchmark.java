package com.github.papahigh.phonetic.benchmarks.throughput;

import com.github.papahigh.phonetic.generated.RussianMorphologyDictionary;
import com.github.papahigh.phonetic.generated.RussianOrthographicDictionary;
import com.github.papahigh.phonetic.generated.RussianSurnamesDictionary;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.List;
import java.util.Random;

import static com.github.papahigh.phonetic.support.EncodersFactory.createEncoder;


public class ThroughputBenchmark {

    @Benchmark
    public void encode(final BenchmarkState state) throws EncoderException {
        state.encoder.encode(state.word);
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {

        String word;
        List<String> dictionary;

        @Param({"RussianAV[8]"})
        String encoderName;

        @Param({"russian_orthography"})
        String dictionaryName;

        StringEncoder encoder;

        @Setup(Level.Trial)
        public void setUp() {
            encoder = createEncoder(encoderName);
            switch (dictionaryName) {
                case "russian_orthography":
                    dictionary = RussianOrthographicDictionary.DICT;
                    break;
                case "russian_surnames":
                    dictionary = RussianSurnamesDictionary.DICT;
                    break;
                case "russian_morphology":
                    dictionary = RussianMorphologyDictionary.DICT;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Setup(Level.Invocation)
        public void setUpWord() {
            word = dictionary.get(new Random().nextInt(dictionary.size()));
        }
    }
}
