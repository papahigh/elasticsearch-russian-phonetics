/*
 * Copyright 2018 Nikolay Papakha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.elasticsearch.index.analysis;

import com.github.papahigh.phonetic.PhoneticEncoder;
import com.github.papahigh.phonetic.VowelsMode;
import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;
import org.tartarus.snowball.ext.RussianStemmer;

import static com.github.papahigh.phonetic.phoneme.russian.RussianPhonemeExchange.FACTORY;
import static com.github.papahigh.phonetic.phoneme.russian.RussianPhonemeRules.CONSONANTS;
import static com.github.papahigh.phonetic.phoneme.russian.RussianPhonemeRules.VOWELS;


public class RussianPhoneticTokenFilterFactory extends AbstractTokenFilterFactory {

    private final PhoneticEncoder encoder;
    private final boolean enableStemmer;
    private final int maxCodeLength;
    private final boolean replace;

    public RussianPhoneticTokenFilterFactory(IndexSettings indexSettings, Environment environment, String name, Settings settings) {
        super(indexSettings, name, settings);
        this.replace = settings.getAsBoolean("replace", true);
        this.maxCodeLength = settings.getAsInt("max_code_len", 8);
        this.enableStemmer = settings.getAsBoolean("enable_stemmer", false);
        VowelsMode vowelsMode;
        String vowelsModeName = settings.get("vowels", "encode_first");
        if ("ENCODE_ALL".equalsIgnoreCase(vowelsModeName)) {
            vowelsMode = VowelsMode.ENCODE_ALL;
        } else if ("ENCODE_FIRST".equalsIgnoreCase(vowelsModeName)) {
            vowelsMode = VowelsMode.ENCODE_FIRST;
        } else if ("IGNORE".equalsIgnoreCase(vowelsModeName)) {
            vowelsMode = VowelsMode.IGNORE;
        } else {
            throw new IllegalArgumentException("No matching vowels mode [" + vowelsModeName + "] found for Russian Phonetic Encoder");
        }
        this.encoder = new PhoneticEncoder(FACTORY, CONSONANTS, VOWELS, vowelsMode, maxCodeLength);
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        return new PhoneticFilter(tokenStream, encoder, enableStemmer ? new RussianStemmer() : null, maxCodeLength, replace);
    }
}
