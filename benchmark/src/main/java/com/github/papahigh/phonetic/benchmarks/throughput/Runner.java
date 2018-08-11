package com.github.papahigh.phonetic.benchmarks.throughput;

import com.github.papahigh.phonetic.benchmarks.Config;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;


public class Runner {

    public static void main(String[] args) throws RunnerException, IOException {

        Path outputDirectory = Config.resolvePath("throughput");
        Options opt = new OptionsBuilder()
                .include(String.format(".*%s.*", ThroughputBenchmark.class.getSimpleName()))
                .param("dictionaryName", Config.getDictionariesStream().toArray(String[]::new))
                .param("encoderName", Config.getEncodersStream().toArray(String[]::new))
                .threads(1)
                .warmupIterations(5)
                .measurementIterations(10)
                .mode(Mode.Throughput)
                .timeUnit(TimeUnit.SECONDS)

                .forks(5)
                .output(outputDirectory.resolve("throughput.log").toFile().getCanonicalPath())
                .result(outputDirectory.resolve("throughput.csv").toFile().getCanonicalPath())
                .resultFormat(ResultFormatType.CSV)
                .build();

        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
