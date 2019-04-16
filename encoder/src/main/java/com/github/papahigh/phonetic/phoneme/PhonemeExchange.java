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
package com.github.papahigh.phonetic.phoneme;


import com.github.papahigh.phonetic.buffer.Buffer;


/**
 * Represents the encoding context (input, output and progress).
 */
public interface PhonemeExchange {

    char getCurr();

    int getBits();

    boolean isFirst();

    boolean isLast();

    boolean isValid();

    boolean hasNext();

    void pickUpCurr();

    void doneWithIt();

    boolean isConsonant();

    boolean canEncodeVowel();

    PhonemeExchange addEncoded(char one);

    PhonemeExchange addEncoded(char... many);

    PhonemeExchange updateEncoded(char one);

    PhonemeExchange drop();

    PhonemeExchange drop(int n);

    PhonemeExchange drop2();

    PhonemeExchange skipNext();

    PhonemeExchange skipNext(int n);

    PhonemeExchange skipNext2();

    char getPrev();

    int getPrevBits();

    char getNext(int n);

    char getNext();

    int getNextBits();

    int getNextBits(int i);

    void flush();

    Buffer getOutputBuffer();

    int getRemainingLength();

    int getRemainingOriginalLength();

}
