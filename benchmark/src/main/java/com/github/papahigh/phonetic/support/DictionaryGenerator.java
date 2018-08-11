package com.github.papahigh.phonetic.support;

import com.github.papahigh.phonetic.benchmarks.Config;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.text.CaseUtils;
import org.jooq.lambda.Unchecked;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class DictionaryGenerator {

    public static void main(String[] args) {
        File src = new File("src/main/java");
        Config.getDictionariesStream().forEach(
                Unchecked.consumer(dictionaryName -> {
                    List<String> words = DictionaryLoader.loadDictionary(Config.getFileName(dictionaryName));
                    Collections.shuffle(words);
                    TypeSpec dictionary = TypeSpec.classBuilder(String.format("%sDictionary", CaseUtils.toCamelCase(dictionaryName, true, '_')))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(MethodSpec.methodBuilder("pick")
                                    .returns(String.class)
                                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                    .addCode(CodeBlock.builder()
                                            .addStatement("return DICT.get(new java.util.Random().nextInt(DICT.size()))")
                                            .build())
                                    .build())
                            .addField(ParameterizedTypeName.get(List.class, String.class), "DICT", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .addStaticBlock(CodeBlock.builder()
                                    .add("DICT = java.util.Arrays.asList(\n")
                                    .add(words.stream()
                                            .limit(8_000)
                                            .map(s -> String.format("\"%s\"", s))
                                            .collect(Collectors.joining(",\n")))
                                    .add(");\n")
                                    .build())
                            .build();
                    JavaFile dictionaryFile = JavaFile.builder("com.github.papahigh.phonetic.generated", dictionary).build();
                    dictionaryFile.writeTo(src);
                })
        );
    }
}
