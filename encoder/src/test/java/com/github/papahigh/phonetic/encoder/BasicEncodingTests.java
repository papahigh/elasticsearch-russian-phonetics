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

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;


public class BasicEncodingTests extends RussianEncoderTestCase {

    @Test
    public void testSurnames() throws EncoderException {

        testEquals("Шмидт", "Шмит");

        testEquals("Грицюк", "Грецук");
        testEquals("Огольцова", "Агальцова");
        testEquals("Бовт", "Бофт");
        testEquals("Авакумов", "Аввакумав");

        testNOTEquals("Беляев", "Беляков");

        testEquals("Шмидть", "Шмит");
        testEquals("Шмидт", "Шмидт");
        testEquals("Шмидт", "Шмедт");

        testNOTEquals("Авакумов", "Аввекумов");

        StringEncoder encode = getEncoder(4);

        Object key1 = encode.encode("Беляев");
        Object key2 = encode.encode("Беляков");

        assertEquals(key1, key2);

        testNOTEquals("Шмидт", "Шмёдт");
        testNOTEquals("Шмидт", "Шмодт");
        testNOTEquals("Шмидт", "Шмадт");
    }


    @Test
    public void testBasicPhonetic() {

        testEquals("зуб", "зуп");
        testEquals("ошибка", "ошипка");
        testEquals("всегда", "фсегда");
        testEquals("сказка", "скаска");
        testEquals("тетрадка", "титратка");
        testEquals("нож", "нош");
        testEquals("раз", "рас");
        testEquals("плов", "плоф");
        testEquals("ложка", "лошка");
        testEquals("вокзал", "вогзал");
        testEquals("сделать", "зделать");
        testEquals("отдых", "одых");
        testEquals("друг", "друк");
        testEquals("знать", "знат");
        testEquals("животом", "жывотом");
        testEquals("женой", "жыной");
        testEquals("росли", "расли");
        testEquals("дужка", "душка");
        testEquals("везти", "вести");

        testNOTEquals("пара", "фара");
        testNOTEquals("цел", "сел");
        testNOTEquals("цирк", "сыр");
        testNOTEquals("цвет", "свет");
        testNOTEquals("ведёт", "везёт");
        testNOTEquals("плюс", "флюс");
        testNOTEquals("тип", "тиф");
        testNOTEquals("бас", "вас");
        testNOTEquals("порт", "форт");
        testNOTEquals("лук", "рук");
        testNOTEquals("ложки", "рожки");
        testNOTEquals("лектор", "ректор");
        testNOTEquals("пафос", "хаос");
        testNOTEquals("фраза", "храм");
        testNOTEquals("лица", "лиса");
        testNOTEquals("цел", "сел");
    }

}
