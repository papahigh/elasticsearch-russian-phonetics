package com.github.papahigh.phonetic.benchmarks.gc;

import com.github.papahigh.phonetic.benchmarks.Config;
import com.github.papahigh.phonetic.benchmarks.throughput.ThroughputBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;


public class Runner {

    public static void main(String[] args) throws IOException, RunnerException {

        Path outputDirectory = Config.resolvePath("gc");
        Options opt = new OptionsBuilder()
                .include(String.format(".*%s.*", ThroughputBenchmark.class.getSimpleName()))
                .param("dictionaryName", Config.getDictionariesStream().toArray(String[]::new))
                .param("encoderName", Config.getEncodersStream().toArray(String[]::new))
                .threads(1)
                .warmupIterations(5)
                .measurementIterations(10)
                .mode(Mode.Throughput)
                .timeUnit(TimeUnit.SECONDS)

                // gc profiler
                .addProfiler(GCProfiler.class)

                .forks(1)
                .output(outputDirectory.resolve("gc.log").toFile().getCanonicalPath())
                .result(outputDirectory.resolve("gc.csv").toFile().getCanonicalPath())
                .resultFormat(ResultFormatType.CSV)
                .build();

        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
