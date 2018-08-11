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
package com.github.papahigh.phonetic.phoneme.russian;


/**
 * Represents dictionary of modern Russian alphabet lowercase letters
 * in rage from letter 'а' to 'я'
 * and provides fast access to the bits associated with a letter.
 */
final class RussianAlphaBits {

    private RussianAlphaBits() {
    }

    private static final int[] ABC = new int['ё' - 'а' + 1];
    private static final char offset = 'а';

    static {

        ABC['у' - offset] = 0xc100; // [3] | HARDER(У)
        ABC['ю' - offset] = 0xc080; // [3] | SOFTER(Ю)
        ABC['я' - offset] = 0xa480; // [1] | SOFTER(Я)
        ABC['э' - offset] = 0x9300; // VOW1 | [2] | HARDER(ы,э)
        ABC['и' - offset] = 0x9280; // VOW1 | [2] | SOFTER(и,е)
        ABC['е' - offset] = 0x9280; // VOW1 | [2] | SOFTER(и,е)
        ABC['а' - offset] = 0x8d00; // VOW2 | [1] | HARDER(а,о)
        ABC['о' - offset] = 0x8d00; // VOW2 | [1] | HARDER(а,о)
        ABC['ё' - offset] = 0x8480; // [1] | SOFTER(ё)
        ABC['ы' - offset] = 0x8300; // [2] | HARDER(ы,э)
        ABC['б' - offset] = 0x801a; // DOMINANT | VOICED | SOUND_PAIRED;  
        ABC['г' - offset] = 0x801a; // DOMINANT | VOICED | SOUND_PAIRED;
        ABC['д' - offset] = 0x801a; // DOMINANT | VOICED | SOUND_PAIRED;
        ABC['з' - offset] = 0x801a; // DOMINANT | VOICED | SOUND_PAIRED;
        ABC['ж' - offset] = 0x8019; // DOMINANT | VOICED | SIZZLE;
        ABC['п' - offset] = 0x8016; // DOMINANT | UNVOICED | SOUND_PAIRED;
        ABC['к' - offset] = 0x8016; // DOMINANT | UNVOICED | SOUND_PAIRED;
        ABC['т' - offset] = 0x8016; // DOMINANT | UNVOICED | SOUND_PAIRED;
        ABC['с' - offset] = 0x8016; // DOMINANT | UNVOICED | SOUND_PAIRED;
        ABC['ф' - offset] = 0x8016; // DOMINANT | UNVOICED | SOUND_PAIRED;
        ABC['х' - offset] = 0x8016; // DOMINANT | UNVOICED | SOUND_PAIRED;
        ABC['ц' - offset] = 0x8015; // DOMINANT | UNVOICED | SIZZLE;
        ABC['ч' - offset] = 0x8015; // DOMINANT | UNVOICED | SIZZLE;
        ABC['ш' - offset] = 0x8015; // DOMINANT | UNVOICED | SIZZLE;
        ABC['щ' - offset] = 0x8015; // DOMINANT | UNVOICED | SIZZLE;
        ABC['в' - offset] = 0x800a; // VOICED | SOUND_PAIRED
        ABC['м' - offset] = 0x8002; // SOUND_PAIRED
        ABC['н' - offset] = 0x8002; // SOUND_PAIRED
        ABC['л' - offset] = 0x8002; // SOUND_PAIRED
        ABC['р' - offset] = 0x8002; // SOUND_PAIRED
        ABC['й' - offset] = 0x8000;
        ABC['ь' - offset] = 0x40;
        ABC['ъ' - offset] = 0x40;
    }

    static int bitsForChar(char c) {
        int bits = 0x0;
        int i = c - offset;
        if (i >= 0 && i < ABC.length) {
            bits = ABC[i];
        }
        return bits;
    }
}
