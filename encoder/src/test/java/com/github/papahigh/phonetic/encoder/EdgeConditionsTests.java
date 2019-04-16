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


import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static com.carrotsearch.randomizedtesting.RandomizedTest.between;
import static org.hamcrest.CoreMatchers.instanceOf;

public class EdgeConditionsTests extends RussianEncoderTestCase {

    private String EMPTY_STRING = new String("".getBytes(StandardCharsets.UTF_8));


    @Test
    public void testConsonantVoicingOnMaxLengthReached() throws EncoderException {

        StringEncoder encoder1 = getEncoder(1);
        // в|падэна -> ф
        assertEquals(encoder1.encode("впадина"), encoder1.encode("ф"));

        StringEncoder encoder6 = getEncoder(6);
        // тетрад|ка -> тетрат
        assertEquals(encoder6.encode("тетрадка"), encoder6.encode("тэтрат"));

        StringEncoder encoder5 = getEncoder(5);
        // молот|ьба -> малад
        assertEquals(encoder5.encode("молотьба"), encoder5.encode("маладьба"));

        assertNotEquals(encoder5.encode("молотьба"), encoder5.encode("малад"));

    }

    @Test
    public void testConsonantVoicingOnMaxCodeLength() throws EncoderException {

        StringEncoder en = getEncoder(6);

        // ТЕТРАД -> ТЕТРАТ
        assertEquals(en.encode("тетрадка"), en.encode("тетратка"));
        assertEquals(en.encode("объесдчик"), en.encode("объезчик"));

        en = getEncoder(4);

        // ТЭТР
        assertEquals(en.encode("тетрадка"), en.encode("тетратка"));
        // АБЭЩ
        assertEquals(en.encode("объесдчик"), en.encode("объезчик"));
    }

    @Test
    public void testNullInput() throws EncoderException {
        assertEquals(EMPTY_STRING, getEncoder().encode(null));

        Throwable e = expectThrows(Throwable.class, () -> getEncoder().encode((Object) null));
        assertThat(e, instanceOf(EncoderException.class));
    }

    @Test
    public void testEmptyInput() throws EncoderException {
        assertEquals(EMPTY_STRING, getEncoder().encode(EMPTY_STRING));
    }

    @Test
    public void testInvalidInput() throws EncoderException {
        assertEquals(EMPTY_STRING, getEncoder().encode("王明：這是什麼？"));
    }

    @Test
    public void testMixedInput() throws EncoderException {
        String validInput = "привет";
        String mixedInput = "王明：這是什麼？" + validInput + " \uD83D\uDE03 ";

        Object code1 = getEncoder().encode(validInput);
        Object code2 = getEncoder().encode(mixedInput);

        assertEquals(code1, code2);
    }

    @Test
    public void testInvalidEncodings() throws EncoderException {
        String[] encodings = {"Cp1252", "UTF-16BE", "UTF-16LE"};
        String input = "привет, \\u2260 уууу \uD83D\uDE03 是什是什!";
        for (int i = 0; i < between(1, 10); i++) {
            Charset charset = Charset.forName(encodings[between(0, encodings.length - 1)]);
            Object something = getEncoder().encode(new String(input.getBytes(charset)));

            assertEquals(EMPTY_STRING, something);
        }
    }

    @Test
    public void testMaximumCodeLength() throws EncoderException {
        List<String> dictionary = Arrays.asList(
                "Абрамов",
                "Авдеев",
                "Агафонов",
                "Аксёнов",
                "Александров",
                "Алексеев",
                "Андреев",
                "Анисимов",
                "Антонов",
                "Артемьев",
                "Архипов"
        );

        for (int i = 0; i < between(1, 5); i++) {
            int maxLength = between(1, 15);
            Encoder encoder = getEncoder(maxLength);
            for (String surname : dictionary) {
                assertTrue(maxLength >= encoder.encode(surname).toString().length());
            }
        }
    }
}
