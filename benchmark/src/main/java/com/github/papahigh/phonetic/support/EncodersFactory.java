package com.github.papahigh.phonetic.support;

import com.github.papahigh.phonetic.PhoneticEncoder;
import com.github.papahigh.phonetic.VowelsMode;
import com.github.papahigh.phonetic.phoneme.russian.RussianPhonemeExchange;
import com.github.papahigh.phonetic.phoneme.russian.RussianPhonemeRules;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.Nysiis;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.PhoneticEngine;
import org.apache.commons.codec.language.bm.RuleType;

import java.util.Collections;


public final class EncodersFactory {

    private EncodersFactory() {
    }

    public static StringEncoder createEncoder(String name, int maxLength) {
        StringEncoder encoder;
        switch (name) {
            case "RussianAVS":
                encoder = russianPhoneticWithStemmer(VowelsMode.ENCODE_ALL, maxLength);
                break;
            case "Russian1VS":
                encoder = russianPhoneticWithStemmer(VowelsMode.ENCODE_FIRST, maxLength);
                break;
            case "RussianAV":
                encoder = russianPhonetic(VowelsMode.ENCODE_ALL, maxLength);
                break;
            case "Russian1V":
                encoder = russianPhonetic(VowelsMode.ENCODE_FIRST, maxLength);
                break;
            case "Russian0V":
                encoder = russianPhonetic(VowelsMode.IGNORE, maxLength);
                break;
            case "Metaphone":
                encoder = metaphone(maxLength);
                break;
            case "DoubleMetaphone":
                encoder = doubleMetaphone(maxLength);
                break;
            default:
                encoder = createEncoder(name);
        }
        return encoder;
    }

    public static StringEncoder createEncoder(String name) {
        StringEncoder encoder;
        switch (name) {
            case "NoOp":
                encoder = NoOp;
                break;
            case "RussianAVS[8]":
                encoder = russianPhoneticWithStemmer(VowelsMode.ENCODE_ALL, 8);
                break;
            case "Russian1VS[8]":
                encoder = russianPhoneticWithStemmer(VowelsMode.ENCODE_FIRST, 8);
                break;
            case "Russian1VS[4]":
                encoder = russianPhoneticWithStemmer(VowelsMode.ENCODE_FIRST, 4);
                break;
            case "RussianAV[8]":
                encoder = russianPhonetic(VowelsMode.ENCODE_ALL, 8);
                break;
            case "Russian1V[8]":
                encoder = russianPhonetic(VowelsMode.ENCODE_FIRST, 8);
                break;
            case "Russian1V[4]":
                encoder = russianPhonetic(VowelsMode.ENCODE_FIRST, 4);
                break;
            case "Metaphone[4]":
                encoder = metaphone(4);
                break;
            case "Metaphone[8]":
                encoder = metaphone(8);
                break;
            case "DoubleMetaphone[4]":
                encoder = doubleMetaphone(4);
                break;
            case "DoubleMetaphone[8]":
                encoder = doubleMetaphone(8);
                break;
            case "Soundex":
                encoder = soundex();
                break;
            case "Nysiis":
                encoder = nysiis();
                break;
            case "BMCyrillic":
                encoder = beiderMorseCyrillic();
                break;
            case "BMRussian":
                encoder = beiderMorseRussian();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown encoder name: %s", name));
        }

        return encoder;
    }

    private static StringEncoder NoOp = new StringEncoder() {
        @Override
        public Object encode(Object o) {
            return o;
        }

        @Override
        public String encode(String s) {
            return s;
        }
    };


    public static StringEncoder russianPhonetic(VowelsMode vowelsMode, int codeLength) {
        return new PhoneticEncoder(
                RussianPhonemeExchange.FACTORY,
                RussianPhonemeRules.CONSONANTS,
                RussianPhonemeRules.VOWELS,
                vowelsMode,
                codeLength
        );
    }

    static StringEncoder russianPhoneticWithStemmer(VowelsMode vowelsMode, int codeLength) {
        return new StemmingAdapter(
                vowelsMode,
                codeLength
        );
    }

    private static StringEncoder beiderMorseRussian() {
        return new TransliteratingAdapter(
                new BeiderMorseAdapter(
                        new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, true, 1),
                        Languages.LanguageSet.from(Collections.singleton("russian"))

                )
        );
    }

    private static StringEncoder beiderMorseCyrillic() {
        return new BeiderMorseAdapter(
                new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, true, 1),
                Languages.LanguageSet.from(Collections.singleton("cyrillic"))
        );
    }

    private static StringEncoder doubleMetaphone(int maxCodeLength) {
        DoubleMetaphone encoder = new DoubleMetaphone();
        encoder.setMaxCodeLen(maxCodeLength);
        return new TransliteratingAdapter(encoder);
    }

    private static StringEncoder soundex() {
        return new TransliteratingAdapter(new Soundex());
    }


    private static StringEncoder metaphone(int maxCodeLength) {
        Metaphone encoder = new Metaphone();
        encoder.setMaxCodeLen(maxCodeLength);
        return new TransliteratingAdapter(encoder);
    }

    private static StringEncoder nysiis() {
        return new TransliteratingAdapter(new Nysiis());
    }
}
