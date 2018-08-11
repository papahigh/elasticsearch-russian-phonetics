package com.github.papahigh.phonetic.benchmarks;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


public class Config {

    private static final List<String> PREDEFINED_ENCODERS = Arrays.asList(
            "Soundex",
            "RussianAVS[8]",
            "RussianAV[8]",
            "Russian1V[8]",
            "Russian1V[4]",
            "Metaphone[4]",
            "Metaphone[8]",
            "BMCyrillic",
            "BMRussian"
    );

    private static final List<String> ENCODERS_WITH_FIXED_LENGTH = Arrays.asList(
            "BMCyrillic",
            "BMRussian",
            "Soundex"
    );

    private static final List<String> ENCODERS_WITH_PARAMETERIZED_LENGTH = Arrays.asList(
            "Metaphone",
            "RussianAV",
            "RussianAVS",
            "Russian1V"
    );

    private static final List<Pair<String, String>> DICTIONARIES = Arrays.asList(
            new ImmutablePair<>("russian_orthography", "dictionary-russian-orthography.txt"),
            new ImmutablePair<>("russian_surnames", "dictionary-russian-surnames.txt"),
            new ImmutablePair<>("russian_morphology", "dictionary-russian-morphology.txt")
    );

    public static Stream<String> getEncodersStream() {
        return PREDEFINED_ENCODERS.stream();
    }

    public static Stream<String> getDictionariesStream() {
        return DICTIONARIES.stream().map(Pair::getLeft);
    }

    public static List<String> getParameterizedLengthEncoders() {
        return Collections.unmodifiableList(ENCODERS_WITH_PARAMETERIZED_LENGTH);
    }

    public static List<String> getFixedLengthEncoders() {
        return Collections.unmodifiableList(ENCODERS_WITH_FIXED_LENGTH);
    }

    public static String getFileName(String dictionary) {
        return DICTIONARIES.stream()
                .filter(pair -> pair.getLeft().equals(dictionary))
                .map(Pair::getRight)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Cannot find dictionary file for %s", dictionary)));
    }

    private static final String ROOT_LOCATION = "assets";

    public static Path resolvePath(String location) {
        Path directory = Paths.get(ROOT_LOCATION, location);
        if (!directory.toFile().exists()) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return directory;
    }


    public static String getEncoderStyle(String encoder) {
        String style;
        switch (encoder) {
            case "BMCyrillic":
                style = "8";
                break;
            case "BMRussian":
                style = "9";
                break;
            case "RussianAV[8]":
                style = "3";
                break;
            case "RussianAVS[8]":
                style = "2";
                break;
            case "Metaphone[8]":
                style = "7";
                break;
            case "Russian1V[8]":
                style = "4";
                break;
            case "Metaphone[4]":
                style = "6";
                break;
            case "Russian1V[4]":
                style = "5";
                break;
            case "Soundex":
                style = "1";
                break;
            case "Levenshtein<3":
                style = "10";
                break;
            default:
                throw new IllegalArgumentException();
        }
        return style;
    }
}
