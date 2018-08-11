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

import com.github.papahigh.phonetic.phoneme.PhonemeExchange;
import com.github.papahigh.phonetic.phoneme.PhonemeRule;
import com.github.papahigh.phonetic.phoneme.PhonemeRuler;

import static com.github.papahigh.phonetic.phoneme.PhonemeRuler.afterNextRule;
import static com.github.papahigh.phonetic.phoneme.PhonemeRuler.currRule;
import static com.github.papahigh.phonetic.phoneme.PhonemeRuler.nextRule;
import static com.github.papahigh.phonetic.phoneme.russian.RussianAlphaBits.bitsForChar;


/**
 * Holds encoding rules for vowels and consonants of Russian Language
 */
public final class RussianPhonemeRules {

    private RussianPhonemeRules() {
    }

    private static final char[] KhK = new char[]{'х', 'к'};
    private static final char[] STV = new char[]{'с', 'т', 'в'};
    private static final char[] Zg = new char[]{'з', 'г'};
    private static final char[] Sk = new char[]{'с', 'к'};
    private static final char[] Sl = new char[]{'с', 'л'};
    private static final char[] Sn = new char[]{'с', 'н'};
    private static final char[] Sts = new char[]{'с', 'ц'};
    private static final char[] Rts = new char[]{'р', 'ц'};
    private static final char[] Rch = new char[]{'р', 'ч'};
    private static final char[] Nts = new char[]{'н', 'ц'};
    private static final char[] Zn = new char[]{'з', 'н'};
    private static final char[] Nk = new char[]{'н', 'к'};
    private static final char[] Nsk = new char[]{'н', 'с', 'к'};
    private static final char[] Nsh = new char[]{'н', 'ш'};
    private static final char[] Ng = new char[]{'н', 'г'};
    private static final char[] NsTv = new char[]{'н', 'с', 'т', 'в'};
    private static final char[] Shk = new char[]{'ш', 'к'};
    private static final char[] Shn = new char[]{'ш', 'н'};
    private static final char[] Sht = new char[]{'ш', 'т'};

    private static final char offset = 'б';
    private static final char[] cPairs = new char['ш' - 'б' + 1];

    static {

        cPairs['б' - offset] = 'п';
        cPairs['п' - offset] = 'б';
        cPairs['в' - offset] = 'ф';
        cPairs['ф' - offset] = 'в';
        cPairs['г' - offset] = 'к';
        cPairs['к' - offset] = 'г';
        cPairs['д' - offset] = 'т';
        cPairs['т' - offset] = 'д';
        cPairs['ж' - offset] = 'ш';
        cPairs['ш' - offset] = 'ж';
        cPairs['з' - offset] = 'с';
        cPairs['с' - offset] = 'з';

    }


    /**
     * Russian Vowels Encoding Rule
     */
    public static final PhonemeRule VOWELS = e -> {
        int vowelBits = e.getBits();
        int nextBits = e.getNextBits();

        char vowelGroup = 0;
        if (nextBits > 0x80ff && (vowelBits == 0x9280 || vowelBits == 0x8300)) {
            // if first-in-word
            if (e.isFirst()) {
                char vowel;
                if (nextBits > 0xc07f) {
                    vowelGroup = 'ю';
                } else if ((vowel = e.getNext()) == 'а') {
                    vowelGroup = 'я';
                } else if (vowel == 'о') {
                    vowelGroup = 'э';
                }
            }
            if (vowelGroup == 0) {
                vowelGroup = encodeVowel(e.isFirst() ? 0x0 : vowelBits, nextBits, e.getNextBits(2), e);
            }
            e.drop().skipNext();
        } else {
            vowelGroup = encodeVowel(e.getPrevBits(), vowelBits, nextBits, e);
        }

        e.addEncoded(vowelGroup).flush();
    };


    /**
     * Russian Consonants Encoding Rules
     */
    public static final PhonemeRule CONSONANTS = consonantRule()

            .preHandler(exchange -> {
                        if (exchange.getRemainingOriginalLength() < 2 && (exchange.getBits() & 0x8) == 0x8
                                && (exchange.getNextBits() < 0x3f || (exchange.getNextBits() & 0x0058) > 0x17)) {
                            // voicing at the end of word
                            int nextBits = exchange.getNextBits();
                            if (nextBits == 0x40 && (exchange.getPrevBits() & 0x8) == 0x8) {
                                exchange.updateEncoded(getPair(exchange.getPrev()));
                            }
                            exchange.addEncoded(getPair(exchange.getCurr()));
                            if ((nextBits & 0x8) == 0x8) {
                                exchange.addEncoded(getPair(exchange.getNext())).skipNext();
                            }
                            exchange.flush();
                            return false;
                        }
                        return true;
                    }

            ).on('с', nextConsonantRule()
                    .on('щ', e -> e.addEncoded('щ').drop().skipNext().flush()) // СЩ → Щ
                    .on('ш', e -> e.addEncoded('ш').drop().skipNext().flush()) // СШ → Ш
                    .on('з', e -> e.addEncoded('з').drop().skipNext().flush()) // СЗ → З
                    .on('ж', e -> e.addEncoded('ж').drop().skipNext().flush()) // СЖ → Ж
                    .on('ч', e -> e.addEncoded('щ').drop().skipNext().flush()) // СЧ → Щ
                    .on('т', afterNextConsonantRule()
                            .on('ч', e -> e.addEncoded('щ').drop2().skipNext2().flush()) // СТЧ → Щ
                            .on('г', e -> e.addEncoded(Zg).drop().skipNext2().flush()) // СТГ → ЗГ
                            .on('к', e -> e.addEncoded(Sk).drop().skipNext2().flush()) // СТК → СК
                            .on('л', e -> e.addEncoded(Sl).drop().skipNext2().flush()) // СТЛ → СЛ
                            .on('н', e -> e.addEncoded(Sn).drop().skipNext2().flush()) // СТН → СН
                            .on('ц', e -> e.addEncoded(Sts).drop().skipNext2().flush()) // СТЦ → СЦ
                            .on('с', e -> {
                                        if (e.getNext(3) == 'к') {
                                            e.addEncoded(Sk).drop2().skipNext(3).flush(); // СТСК → СК
                                        }
                                    }

                            ).build()
                    )
                    .on('д', afterNextConsonantRule()
                            .on('ч', e -> e.addEncoded('щ').drop2().skipNext2().flush()) // СДЧ → Щ
                            .on('г', e -> e.addEncoded(Zg).drop().skipNext2().flush()) // СДГ → ЗГ
                            .on('к', e -> e.addEncoded(Sk).drop().skipNext2().flush()) // СДК → СК
                            .on('л', e -> e.addEncoded(Sl).drop().skipNext2().flush()) // СДЛ → СЛ
                            .on('н', e -> e.addEncoded(Sn).drop().skipNext2().flush()) // СДН → СН
                            .on('ц', e -> e.addEncoded(Sts).drop().skipNext2().flush()) // СДЦ → СЦ
                            .on('с', e -> {
                                        if (e.getNext(3) == 'к') {
                                            e.addEncoded(Sk).drop2().skipNext(3).flush(); // СДСК → СК
                                        }
                                    }

                            ).build()

                    ).build()

            ).on('з', nextConsonantRule()
                    .on('щ', e -> e.addEncoded('щ').drop().skipNext().flush()) // ЗЩ → Щ
                    .on('ш', e -> e.addEncoded('ш').drop().skipNext().flush()) // ЗШ → Ш
                    .on('с', e -> e.addEncoded('с').drop().skipNext().flush()) // ЗС → С
                    .on('ж', e -> e.addEncoded('ж').drop().skipNext().flush()) // ЗЖ → Ж
                    .on('ч', e -> e.addEncoded('щ').drop().skipNext().flush()) // ЗЧ → Щ
                    .on('т', afterNextConsonantRule()
                            .on('ч', e -> e.addEncoded('щ').drop2().skipNext2().flush()) // ЗТЧ → Щ
                            .on('г', e -> e.addEncoded(Zg).drop().skipNext2().flush()) // ЗТГ → ЗГ
                            .on('к', e -> e.addEncoded(Sk).drop().skipNext2().flush()) // ЗТК → СК
                            .on('л', e -> e.addEncoded(Sl).drop().skipNext2().flush()) // ЗТЛ → СЛ
                            .on('н', e -> e.addEncoded(Zn).drop().skipNext2().flush()) // ЗТН → СН
                            .on('ц', e -> e.addEncoded(Sts).drop().skipNext2().flush()) // ЗТЦ → СЦ
                            .on('с', e -> {
                                        if (e.getNext(3) == 'к') {
                                            e.addEncoded(Sk).drop2().skipNext(3).flush(); // ЗТСК → СК
                                        }
                                    }

                            ).build()
                    )
                    .on('д', afterNextConsonantRule()
                            .on('ч', e -> e.addEncoded('щ').drop2().skipNext2().flush()) // ЗДЧ → Щ
                            .on('г', e -> e.addEncoded(Zg).drop().skipNext2().flush()) // ЗДГ → ЗГ
                            .on('к', e -> e.addEncoded(Sk).drop().skipNext2().flush()) // ЗДК → СК | громоздкий -> громоский
                            .on('л', e -> e.addEncoded(Sl).drop().skipNext2().flush()) // ЗТЛ → СЛ
                            .on('н', e -> e.addEncoded(Zn).drop().skipNext2().flush()) // ЗДН → ЗН
                            .on('ц', e -> e.addEncoded(Sts).drop().skipNext2().flush()) // ЗДЦ → СЦ
                            .on('с', e -> {
                                        if (e.getNext(3) == 'к') {
                                            e.addEncoded(Sk).drop2().skipNext(3).flush(); // ЗДСК → СК
                                        }
                                    }

                            ).build()

                    ).build()

            ).on('н', nextConsonantRule()
                    .on('д', afterNextConsonantRule()
                            .on('ч', e -> e.addEncoded('щ').drop2().skipNext2().flush()) // ЗДЧ → Щ
                            .on('г', e -> e.addEncoded(Zg).drop().skipNext2().flush()) // ЗДГ → ЗГ
                            .on('к', e -> e.addEncoded(Nk).drop().skipNext2().flush()) // НДК → НК
                            .on('ц', e -> e.addEncoded(Nts).drop().skipNext2().flush()) // НДЦ → НЦ
                            .on('ш', e -> e.addEncoded(Nsh).drop().skipNext2().flush()) // НДШ → НШ
                            .on('с', e -> {
                                        char afterAfterNext = e.getNext(3);
                                        if (afterAfterNext == 'к') {
                                            e.addEncoded(Nsk).drop().skipNext(3).flush(); // НДСК → НСК
                                        } else if (afterAfterNext == 'т' && e.getNext(4) == 'в') {
                                            e.addEncoded(NsTv).drop().skipNext(4).flush();  // НДСТВ -> НСТВ
                                        }
                                    }

                            ).build()
                    )
                    .on('т', afterNextConsonantRule()
                            .on('ч', e -> e.addEncoded('щ').drop2().skipNext2().flush()) // ЗТЧ → Щ
                            .on('г', e -> e.addEncoded(Ng).drop().skipNext2().flush()) // ЗТГ → ЗГ
                            .on('к', e -> e.addEncoded(Nk).drop().skipNext2().flush()) // НТК → НК
                            .on('ц', e -> e.addEncoded(Nts).drop().skipNext2().flush()) // НТЦ → НЦ
                            .on('ш', e -> e.addEncoded(Nsh).drop().skipNext2().flush()) // НТШ → НШ
                            .on('с', e -> {
                                        char afterAfterNext = e.getNext(3);
                                        if (afterAfterNext == 'к') {
                                            e.addEncoded(Nsk).drop().skipNext(3).flush(); // НТСК → НСК
                                        } else if (afterAfterNext == 'т' && e.getNext(4) == 'в') {
                                            e.addEncoded(NsTv).drop().skipNext(4).flush();  // НТСТВ -> НСТВ
                                        }
                                    }

                            ).build()

                    ).build()

            ).on('д', nextConsonantRule()
                    .on('т', e -> e.addEncoded('т').drop().skipNext().flush()) // ДТ → Т
                    .on('ч', e -> e.addEncoded('ч').drop().skipNext().flush()) // ДЧ → Ч
                    .on('щ', e -> e.addEncoded('щ').drop().skipNext().flush()) // ДЩ → Щ
                    .on('ц', e -> e.addEncoded('ц').drop().skipNext().flush()) // ДЦ → Ц
                    .on('с', e -> { // ТС → Ц
                                e.addEncoded('ц');
                                int nextBits;
                                if (e.canEncodeVowel() && (nextBits = bitsForChar(e.getNext(2))) > 0x80ff) {
                                    // consistency with vowels after 'ц'
                                    e.addEncoded(encodeVowel(0x8015, nextBits, e.getNextBits(3), e)).skipNext();
                                }
                                e.drop().skipNext().flush();
                            }
                    )
                    .on('ь', e -> {
                                if (e.getNext(2) == 'с') {
                                    e.addEncoded('ц');
                                    int nextBits;
                                    if (e.canEncodeVowel() && (nextBits = bitsForChar(e.getNext(3))) > 0x80ff) {
                                        // consistency with vowels after 'ц'
                                        e.addEncoded(encodeVowel(0x8015, nextBits, e.getNextBits(4), e)).skipNext();
                                    }
                                    e.drop2().skipNext2().flush();
                                }
                            }

                    ).build()

            ).on('т', nextConsonantRule()
                    .on('д', e -> e.addEncoded('д').drop().skipNext().flush())
                    .on('ч', e -> e.addEncoded('ч').drop().skipNext().flush()) // ТЧ → Ч
                    .on('щ', e -> e.addEncoded('щ').drop().skipNext().flush()) // ТЩ → Щ
                    .on('ц', e -> e.addEncoded('ц').drop().skipNext().flush()) // ТЦ → Ц
                    .on('с', e -> { // ТС → Ц
                        e.addEncoded('ц');
                        int nextBits;
                        if (e.canEncodeVowel() && (nextBits = bitsForChar(e.getNext(2))) > 0x80ff) {
                            // consistency with vowels after 'ц'
                            e.addEncoded(encodeVowel(0x8015, nextBits, e.getNextBits(3), e)).skipNext();
                        }
                        e.drop().skipNext().flush();
                    })
                    .on('ь', e -> {
                                char afterNext = e.getNext(2);
                                if (afterNext == 'с') { // ТЬС → Ц
                                    e.addEncoded('ц');
                                    int nextBits;
                                    if (e.canEncodeVowel() && (nextBits = bitsForChar(e.getNext(3))) > 0x80ff) {
                                        // consistency with vowels after 'ц'
                                        e.addEncoded(encodeVowel(0x8015, nextBits, e.getNextBits(4), e)).skipNext();
                                    }
                                    e.skipNext2().drop2().flush();
                                } else if (afterNext == 'щ' || afterNext == 'ш') {
                                    e.addEncoded('щ').drop2().skipNext2().flush();
                                }
                            }

                    ).build()

            ).on('в', nextConsonantRule()
                    .on('ф', e -> e.addEncoded('ф').drop().skipNext().flush())
                    .on('с', e -> {
                        if (e.getNext(2) == 'т' && e.getNext(3) == 'в') {
                            e.addEncoded(STV).drop().skipNext(3).flush(); // ВСТВ → СТВ
                        }
                    }).build()

            ).on('ф', nextConsonantRule()
                    .on('в', e -> e.addEncoded('ф').drop().skipNext().flush())
                    .on('с', e -> {
                        if (e.getNext(2) == 'т' && e.getNext(3) == 'в') {
                            e.addEncoded(STV).drop().skipNext(3).flush(); // ФСТВ → СТВ
                        }
                    }).build()

            ).on('ж', nextConsonantRule()
                    .on('ч', e -> e.addEncoded('щ').drop().skipNext().flush()) // ЖЧ → Щ
                    .on('к', e -> e.addEncoded(Shk).skipNext().flush()) // ЖК → ШК
                    .build()

            ).on('р', nextConsonantRule()
                    .on('д', afterNextConsonantRule()
                            .on('ц', e -> e.addEncoded(Rts).drop().skipNext2().flush()) // РДЦ → РЦ
                            .on('ч', e -> e.addEncoded(Rch).drop().skipNext2().flush()) // РДЧ → РЧ
                            .build()
                    ).build()

            ).on('ч', nextConsonantRule()
                    .on('ш', e -> e.addEncoded('щ').drop().skipNext().flush()) // ЧШ → Щ
                    .on('щ', e -> e.addEncoded('щ').drop().skipNext().flush()) // ЧЩ → Щ
                    .on('н', e -> e.addEncoded(Shn).skipNext().flush()) // ЧН → ШН
                    .on('т', e -> e.addEncoded(Sht).skipNext().flush()) // ЧТ → ШТ
                    .build()

            ).on('л', e -> {
                        if (e.getNext() == 'н' && e.getNext(2) == 'ц') {
                            e.addEncoded(Nts).drop().skipNext2().flush(); // ЛНЦ → НЦ
                        }
                    }

            ).on('й', e -> {
                        int nextBits = e.getNextBits();
                        if (e.isFirst() && nextBits > 0x80ff && e.canEncodeVowel()) {
                            char vowel;
                            char mapped;
                            if (nextBits > 0xc07f) {
                                mapped = 'ю';
                            } else if ((vowel = e.getNext()) == 'а') {
                                mapped = 'я';
                            } else if (vowel == 'о') {
                                mapped = 'э';
                            } else {
                                mapped = encodeVowel(0x0, nextBits, e.getNextBits(2), e);
                            }
                            e.addEncoded(mapped).drop().skipNext();
                        }
                        if (nextBits < 0x80ff) {
                            e.addEncoded('й');
                        } else {
                            e.drop();
                        }
                        e.flush();
                    }

            ).on('х', e -> {
                        if (e.getNext() == 'г') {
                            e.addEncoded('г').drop().skipNext().flush(); // ХГ → Г
                        }
                    }

            ).on('ш', e -> {
                        if (e.getNext() == 'ч') {
                            e.addEncoded('щ').drop().skipNext().flush(); // ШЧ → Щ
                        }
                    }

            ).on('г', e -> {
                        char next = e.getNext();
                        if (next == 'к') {
                            e.addEncoded(KhK).skipNext().flush(); // ГК → ХК
                        } else {
                            char previous = e.getPrev();
                            if (e.getRemainingOriginalLength() == 1 && next == 'о' && (previous == 'е' || previous == 'о')) {
                                e.addEncoded('в').flush(); // ОГО, ЕГО → АВА, ЭВА
                            }
                        }
                    }

            ).postHandler(e -> {

                        int bits = e.getBits();
                        int nextBits = e.getNextBits();

                        boolean forceVoicing = false;
                        if (e.getRemainingLength() == 0 && (e.getRemainingOriginalLength() < 3)) {
                            // double voicing on max length reached
                            if ((e.getNextBits(2) < 0x41) && (nextBits & 0x8) == 0x8 && (bits & 0x8) == 0x8) {
                                forceVoicing = true;
                            }
                        }

                        if (nextBits == 0x40) {
                            nextBits = bitsForChar(e.getNext(2));
                        }

                        // consonant voicing within the word
                        if (forceVoicing ||
                                ((nextBits & 0x0018) == 0x0018 && (bits & 0x4) == 0x4) ||
                                ((nextBits & 0x0014) == 0x0014 && (bits & 0x8) == 0x8)) {
                            char currPair = getPair(e.getCurr());
                            if (currPair != 0) {
                                e.updateEncoded(currPair);
                                if (currPair == e.getNext()) {
                                    e.drop().skipNext();
                                }
                            }
                        }
                        e.flush();
                    }

            ).build();

    private static char encodeVowel(int prevBits, int vowelBits, int nextBits, PhonemeExchange e) {
        char encoded;
        if (prevBits < 0x40) {
            // first-in-word vowel mapping
            switch (vowelBits) {
                case 0x8d00:
                    encoded = 'а';
                    break;
                case 0xa480:
                    encoded = 'я';
                    break;
                case 0xc100:
                    encoded = 'у';
                    break;
                case 0xc080:
                    encoded = 'ю';
                    break;
                default:
                    encoded = 'э';
            }

        } else if (vowelBits > 0xc07f) {
            // УЮ vowel
            encoded = '3';
        } else if ((prevBits & 0x1) == 0x1) {
            // not УЮ vowel before SIZZLE
            encoded = '2';
        } else if (vowelBits == 0xa480) {
            // if Я after SOUND_PAIRED
            encoded = (prevBits & 0x2) == 0x2 ? '2' : '1';
        } else {
            // look up vowel group
            encoded = (vowelBits & 0x0fff) > 0x3ff ? '1' : '2';
        }

        ////////////////////////////////////////////////////////
        //
        // Since encoder will drop duplicate adjacent letters
        // we need to keep consistency with:
        //
        //      ч(ии)нка - ч(аи)нка
        //      в(оо)бражение - в(оа)бражение
        //
        ////////////////////////////////////////////////////////

        if (((nextBits & 0x1000) == 0x1000) && (encoded == '2' || encoded == 'э')
                || (encoded == '1' && (nextBits & 0x0800) == 0x0800)) {
            e.drop().skipNext();
        }

        return encoded;
    }

    private static char getPair(char c) {
        char paired = 0;
        int index = c - offset;
        if (index >= 0 && index < cPairs.length) {
            paired = cPairs[index];
        }
        return paired;
    }

    private static PhonemeRuler.Builder consonantRule() {
        return currRule('б', 'ь', 'б');
    }

    private static PhonemeRuler.Builder nextConsonantRule() {
        return nextRule('б', 'ь', 'б');
    }

    private static PhonemeRuler.Builder afterNextConsonantRule() {
        return afterNextRule('б', 'ь', 'б');
    }

}
