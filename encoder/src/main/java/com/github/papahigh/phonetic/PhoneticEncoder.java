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
package com.github.papahigh.phonetic;

import com.github.papahigh.phonetic.buffer.Buffer;
import com.github.papahigh.phonetic.buffer.InputBuffer;
import com.github.papahigh.phonetic.buffer.LimitedLengthInputBuffer;
import com.github.papahigh.phonetic.buffer.StemmedInputBuffer;
import com.github.papahigh.phonetic.phoneme.PhonemeExchange;
import com.github.papahigh.phonetic.phoneme.PhonemeExchangeFactory;
import com.github.papahigh.phonetic.phoneme.PhonemeRule;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

import java.util.Locale;


/**
 * Transforms supplied input into a phonetic code
 * by sequentially applying the provided rules.
 * <p>
 * This class is thread safe.
 *
 * @see InputBuffer
 * @see VowelsMode
 * @see PhonemeExchange
 * @see PhonemeRule
 */
public final class PhoneticEncoder implements StringEncoder {

    private final PhonemeExchangeFactory exchangeFactory;
    private final PhonemeRule consonantRules;
    private final PhonemeRule vowelRules;
    private final VowelsMode vowelsMode;
    private final int maximumLength;


    public PhoneticEncoder(PhonemeExchangeFactory exchangeFactory, PhonemeRule consonantRules, PhonemeRule vowelRules,
                           VowelsMode vowelsMode, int maximumLength) {
        this.exchangeFactory = exchangeFactory;
        this.consonantRules = consonantRules;
        this.vowelRules = vowelRules;
        this.vowelsMode = vowelsMode;
        this.maximumLength = maximumLength;
    }


    /**
     * Encodes an input String into phonetic code using the provided rules.
     * Maximum length of the result is controlled by maximumLength class field value.
     * The input string is converted to lowercase.
     * Limitations: Input format is expected to be a single ASCII word.
     *
     * @param source String object to encode
     * @return The phonetic code corresponding to the String supplied.
     */
    @Override
    public String encode(String source) {
        if (source == null || source.length() == 0) {
            return "";
        }
        char[] buffer = source.toLowerCase(Locale.getDefault()).toCharArray();
        Buffer outputBuffer = encode(new LimitedLengthInputBuffer(buffer, source.length(), maximumLength));
        return new String(outputBuffer.getBuffer(), 0, outputBuffer.getLength());
    }


    /**
     * Encodes an input Object to phonetic code using the provided rules.
     * <p>
     * This method will throw an EncoderException if the
     * supplied object is not of type java.lang.String or com.github.papahigh.phonetic.buffer.InputBuffer.
     *
     * @param source Object to encode
     * @return An object containing the phonetic code which corresponds to the Object supplied.
     * @throws EncoderException if the parameter supplied is not of type java.lang.String
     *                          or com.github.papahigh.phonetic.buffer.InputBuffer
     */
    @Override
    public Object encode(Object source) throws EncoderException {
        if (source instanceof InputBuffer) {
            return encode((InputBuffer) source);
        }
        if (source instanceof String) {
            return encode((String) source);
        }
        throw new EncoderException("Unsupported parameter type supplied to [PhoneticEncoder]");
    }


    /**
     * Encodes an InputBuffer into phonetic code using the provided rules.
     * Maximum length of the result is controlled by buffer itself via InputBuffer.getLimit()
     * Also input buffer may contain stemming analysis (StemmedInputBuffer).
     * Limitations: Input buffer letters are expected to be in lowercase.
     *
     * @param buffer input buffer to encode
     * @return buffer containing the phonetic code which corresponds to the supplied input.
     * @see StemmedInputBuffer
     * @see LimitedLengthInputBuffer
     */
    public Buffer encode(InputBuffer buffer) {

        PhonemeExchange exchange = exchangeFactory.create(buffer, vowelsMode);

        do {
            exchange.pickUpCurr();
            if (exchange.isConsonant()) {
                consonantRules.apply(exchange);
            } else if (exchange.canEncodeVowel()) {
                vowelRules.apply(exchange);
            } else
                exchange.drop();
            exchange.doneWithIt();
        } while (exchange.hasNext());

        return exchange.getOutputBuffer();
    }

}
