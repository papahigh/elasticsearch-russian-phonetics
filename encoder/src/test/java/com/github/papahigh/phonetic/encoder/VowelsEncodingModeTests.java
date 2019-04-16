/*
 * Copyright 2019 Nikolay Papakha
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
import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.EncoderException;
import org.apache.lucene.util.LuceneTestCase;
import org.junit.Test;


public class VowelsEncodingModeTests extends LuceneTestCase {

    @Test
    public void testVowelsEncodingMode() throws EncoderException {

        /* All vowels in word will be encoded */
        Encoder allVowelsEncoder = new PhoneticEncoder(
                RussianPhonemeExchange.FACTORY,
                RussianPhonemeRules.CONSONANTS,
                RussianPhonemeRules.VOWELS,
                VowelsMode.ENCODE_ALL,
                6
        );

        String text11 = "МОЛОКО";
        String text12 = "МАЛАКО";
        String text13 = "МОЛОКУ";

        // МОЛОКО = МАЛАКО
        assertEquals(allVowelsEncoder.encode(text11), allVowelsEncoder.encode(text12));

        // МОЛОКО != МОЛОКУ
        assertNotEquals(allVowelsEncoder.encode(text11), allVowelsEncoder.encode(text13));

        // МАЛАКО != МОЛОКУ
        assertNotEquals(allVowelsEncoder.encode(text12), allVowelsEncoder.encode(text13));

        /* Only first vowel in word will be encoded */
        Encoder firstVowelEncoder = new PhoneticEncoder(
                RussianPhonemeExchange.FACTORY,
                RussianPhonemeRules.CONSONANTS,
                RussianPhonemeRules.VOWELS,
                VowelsMode.ENCODE_FIRST,
                6
        );


        assertEquals(firstVowelEncoder.encode(text11), firstVowelEncoder.encode(text13));
        assertEquals(firstVowelEncoder.encode(text12), firstVowelEncoder.encode(text13));

        String text21 = "ОБРЫВ";
        String text22 = "ОБРУВ";
        String text23 = "УБРЫВ";

        // ОБРЫВ = ОБРУВ
        assertEquals(firstVowelEncoder.encode(text21), firstVowelEncoder.encode(text22));

        // ОБРЫВ != УБРЫВ
        assertNotEquals(firstVowelEncoder.encode(text21), firstVowelEncoder.encode(text23));

        // ОБРУВ != УБРЫВ
        assertNotEquals(firstVowelEncoder.encode(text22), firstVowelEncoder.encode(text23));

        /* Vowels will be ignored */
        Encoder ignoringVowelsEncoder = new PhoneticEncoder(
                RussianPhonemeExchange.FACTORY,
                RussianPhonemeRules.CONSONANTS,
                RussianPhonemeRules.VOWELS,
                VowelsMode.IGNORE,
                6
        );

        // ignoring vowels encoder produces equal keys for all inputs
        assertEquals(ignoringVowelsEncoder.encode(text11), ignoringVowelsEncoder.encode(text12));
        assertEquals(ignoringVowelsEncoder.encode(text11), ignoringVowelsEncoder.encode(text13));
        assertEquals(ignoringVowelsEncoder.encode(text12), ignoringVowelsEncoder.encode(text13));
        assertEquals(ignoringVowelsEncoder.encode(text21), ignoringVowelsEncoder.encode(text22));
        assertEquals(ignoringVowelsEncoder.encode(text21), ignoringVowelsEncoder.encode(text23));
        assertEquals(ignoringVowelsEncoder.encode(text22), ignoringVowelsEncoder.encode(text23));

        String text31 = "УПЧК";
        String text32 = "УЧПК";

        assertNotEquals(ignoringVowelsEncoder.encode(text31), ignoringVowelsEncoder.encode(text32));
    }

    @Test
    public void testIgnoreVowelsEncodingMode() throws EncoderException {

        Encoder encoder = new PhoneticEncoder(
                RussianPhonemeExchange.FACTORY,
                RussianPhonemeRules.CONSONANTS,
                RussianPhonemeRules.VOWELS,
                VowelsMode.IGNORE,
                6
        );


        String text11 = "жужилица";
        String text12 = "жулица";

        // жжлц != жлц
        assertNotEquals(encoder.encode(text11), encoder.encode(text12));
    }

}
