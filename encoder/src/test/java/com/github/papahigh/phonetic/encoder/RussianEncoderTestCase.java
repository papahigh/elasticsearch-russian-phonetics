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
package com.github.papahigh.phonetic.encoder;

import com.github.papahigh.phonetic.PhoneticEncoder;
import com.github.papahigh.phonetic.VowelsMode;
import com.github.papahigh.phonetic.phoneme.russian.RussianPhonemeExchange;
import com.github.papahigh.phonetic.phoneme.russian.RussianPhonemeRules;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.lucene.util.LuceneTestCase;


public abstract class RussianEncoderTestCase extends LuceneTestCase {

    StringEncoder getEncoder(int codeLength) {
        return new PhoneticEncoder(
                RussianPhonemeExchange.FACTORY,
                RussianPhonemeRules.CONSONANTS,
                RussianPhonemeRules.VOWELS,
                VowelsMode.ENCODE_ALL,
                codeLength
        );
    }

    StringEncoder getEncoder() {
        return getEncoder(8);
    }

    void testEquals(String sample1, String sample2) {
        try {

            // tests for keys length range
            for (int i = 3; i < 15; i++) {

                StringEncoder encoder = getEncoder(i);

                String key1 = encoder.encode(sample1);
                String key2 = encoder.encode(sample2);

                assertNotNull(key1);
                assertNotNull(key2);

                assertEquals(key1, key2);
            }

        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
    }

    void testNOTEquals(String sample1, String sample2) {
        try {

            StringEncoder encoder = getEncoder();

            String key1 = encoder.encode(sample1);
            String key2 = encoder.encode(sample2);

            assertNotEquals(key1, key2);

        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
    }
}
