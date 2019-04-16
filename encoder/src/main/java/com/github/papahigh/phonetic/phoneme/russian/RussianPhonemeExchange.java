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
package com.github.papahigh.phonetic.phoneme.russian;

import com.github.papahigh.phonetic.VowelsMode;
import com.github.papahigh.phonetic.buffer.AppendableBuffer;
import com.github.papahigh.phonetic.buffer.Buffer;
import com.github.papahigh.phonetic.buffer.InputBuffer;
import com.github.papahigh.phonetic.phoneme.PhonemeExchange;
import com.github.papahigh.phonetic.phoneme.PhonemeExchangeFactory;

import static com.github.papahigh.phonetic.phoneme.russian.RussianAlphaBits.bitsForChar;


/**
 * Holds the encoding context.
 * This class is not thread safe and is instantiated by PhonemeExchangeFactory on demand.
 *
 * @see PhonemeExchangeFactory
 */
public final class RussianPhonemeExchange implements PhonemeExchange {

    public static final PhonemeExchangeFactory FACTORY = RussianPhonemeExchange::new;

    private final InputBuffer inputBuffer;
    private final AppendableBuffer outputBuffer;
    private final VowelsMode vowelsMode;

    private char curr;
    private int bits;
    private int index;

    private RussianPhonemeExchange(InputBuffer inputBuffer, VowelsMode vowelsMode) {
        this.inputBuffer = inputBuffer;
        this.vowelsMode = vowelsMode;
        this.outputBuffer = new AppendableBuffer(inputBuffer.getLimit());
    }

    @Override
    public void pickUpCurr() {
        for (; index < inputBuffer.getLength(); index++) {
            char current = inputBuffer.charAt(index);
            if ((index == 0 || current != inputBuffer.charAt(index + 1))
                    && (bits = bitsForChar(current)) > 0x7fff) {
                this.curr = current;
                break;
            } else {
                drop();
            }
        }
    }

    @Override
    public void doneWithIt() {
        this.index++;
    }

    @Override
    public PhonemeExchange addEncoded(char value) {
        outputBuffer.append(value);
        return this;
    }

    @Override
    public PhonemeExchange addEncoded(char... value) {
        outputBuffer.append(value);
        return this;
    }

    @Override
    public PhonemeExchange updateEncoded(char value) {
        outputBuffer.updateLatest(value);
        return this;
    }

    @Override
    public PhonemeExchange drop() {
        inputBuffer.dropChar();
        return this;
    }

    @Override
    public PhonemeExchange drop2() {
        inputBuffer.dropChars2();
        return this;
    }

    @Override
    public PhonemeExchange drop(int n) {
        inputBuffer.dropChars(n);
        return this;
    }

    @Override
    public PhonemeExchange skipNext() {
        index += 1;
        return this;
    }

    @Override
    public PhonemeExchange skipNext2() {
        index += 2;
        return this;
    }

    @Override
    public PhonemeExchange skipNext(int n) {
        index += n;
        return this;
    }

    @Override
    public char getCurr() {
        if (curr == 0) {
            curr = inputBuffer.charAt(index);
        }
        return curr;
    }

    @Override
    public int getBits() {
        if (bits == 0x0) {
            bits = bitsForChar(getCurr());
        }
        return bits;
    }

    @Override
    public boolean isFirst() {
        return index == 0;
    }

    @Override
    public boolean isLast() {
        return inputBuffer.getLength() == (index + 1);
    }

    @Override
    public boolean isValid() {
        return bits > 0x7fff;
    }

    @Override
    public boolean isConsonant() {
        return getBits() < 0x80ff;
    }

    @Override
    public boolean canEncodeVowel() {
        boolean enabled;
        switch (vowelsMode) {
            case ENCODE_ALL:
                enabled = true;
                break;
            case ENCODE_FIRST:
                enabled = index == 0;
                break;
            case IGNORE:
            default:
                enabled = false;
        }
        return enabled;
    }

    @Override
    public int getRemainingLength() {
        return inputBuffer.getLimit() - outputBuffer.getLength();
    }

    @Override
    public int getRemainingOriginalLength() {
        return inputBuffer.getLength() - (index + 1);
    }

    @Override
    public char getPrev() {
        return inputBuffer.charAt(index - 1);
    }

    @Override
    public int getPrevBits() {
        return bitsForChar(getPrev());
    }

    @Override
    public boolean hasNext() {
        return index < inputBuffer.getLength() && inputBuffer.getLimit() > outputBuffer.getLength();
    }

    @Override
    public char getNext(int n) {
        return inputBuffer.charAt(index + n);
    }

    @Override
    public char getNext() {
        return inputBuffer.charAt(index + 1);
    }

    @Override
    public int getNextBits() {
        return bitsForChar(getNext());
    }

    @Override
    public int getNextBits(int i) {
        return bitsForChar(getNext(i));
    }

    @Override
    public void flush() {
        curr = 0;
        bits = 0x0;
    }

    @Override
    public Buffer getOutputBuffer() {
        return outputBuffer;
    }

    @Override
    public String toString() {
        return new String(outputBuffer.getBuffer(), 0, Math.min(inputBuffer.getLimit(), outputBuffer.getLength()));
    }
}
