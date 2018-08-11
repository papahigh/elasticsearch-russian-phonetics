package com.github.papahigh.phonetic.support;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;


public final class DictionaryLoader {

    private DictionaryLoader() {
    }

    public static List<String> loadDictionaryRaw(String location) {
        Path path;
        try {
            path = Paths.get(Objects.requireNonNull(DictionaryLoader.class.getClassLoader().getResource(location)).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        List<String> allLines;
        try {
            allLines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return allLines;
    }

    public static List<String> loadDictionary(String location) {
        return loadDictionaryRaw(location).stream()
                .map(DictionaryLoader::dropComment)
                .flatMap(DictionaryLoader::tokenize)
                .collect(Collectors.toList());
    }

    private static Stream<String> tokenize(String source) {
        return stream(source.split("\\s+|-"))
                .map(String::trim)
                .filter(s -> s.length() > 2);
    }

    private static String dropComment(String line) {
        int indexOfHash = line.indexOf("#"); // remove comments in 'dictionary-russian-orthography.txt'
        if (indexOfHash != -1) {
            line = line.substring(0, indexOfHash);
        }
        return line;
    }
}
