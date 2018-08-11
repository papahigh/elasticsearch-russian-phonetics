package com.github.papahigh.phonetic.support;

import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.PhoneticEngine;


public final class BeiderMorseAdapter implements StringEncoder {

    private final PhoneticEngine phoneticEngine;
    private final Languages.LanguageSet languageSet;

    BeiderMorseAdapter(PhoneticEngine phoneticEngine, Languages.LanguageSet languageSet) {
        this.phoneticEngine = phoneticEngine;
        this.languageSet = languageSet;
    }

    @Override
    public String encode(String source) {
        return phoneticEngine.encode(source, languageSet);
    }

    @Override
    public Object encode(Object source) {
        return encode((String) source);
    }
}
