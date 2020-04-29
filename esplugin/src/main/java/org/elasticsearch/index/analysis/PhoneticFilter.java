/*
 * Copyright 2020 Nikolay Papakha
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
import com.github.papahigh.phonetic.buffer.Buffer;
import com.github.papahigh.phonetic.buffer.InputBuffer;
import com.github.papahigh.phonetic.buffer.LimitedLengthInputBuffer;
import com.github.papahigh.phonetic.buffer.StemmedInputBuffer;
import org.apache.lucene.analysis.CharacterUtils;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.tartarus.snowball.SnowballProgram;

import java.io.IOException;
import java.util.Arrays;


public final class PhoneticFilter extends TokenFilter {

    private final PhoneticEncoder encoder;
    private final boolean replace;

    private final SnowballProgram stemmer;
    private final int maxCodeLength;

    private State state = null;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final PositionIncrementAttribute posAtt = addAttribute(PositionIncrementAttribute.class);


    PhoneticFilter(TokenStream input, PhoneticEncoder encoder, SnowballProgram stemmer, int maxCodeLength, boolean replace) {
        super(input);
        this.encoder = encoder;
        this.maxCodeLength = maxCodeLength;
        this.replace = replace;
        this.stemmer = stemmer;
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (state != null) {
            restoreState(state);
            state = null;
            return true;
        }

        if (!input.incrementToken()) return false;

        if (termAtt.length() == 0) return true;

        int termAttLength = termAtt.length();
        char[] termAttBuffer;
        if (replace) {
            termAttBuffer = termAtt.buffer();
        } else {
            termAttBuffer = Arrays.copyOf(termAtt.buffer(), termAttLength);
        }

        CharacterUtils.toLowerCase(termAttBuffer, 0, termAttLength);

        InputBuffer inputBuffer;
        if (stemmer != null) {
            stemmer.setCurrent(termAttBuffer, termAttLength);
            stemmer.stem();
            inputBuffer = new StemmedInputBuffer(stemmer.getCurrentBuffer(), termAtt.length(),
                    stemmer.getCurrentBufferLength(), maxCodeLength);
        } else {
            inputBuffer = new LimitedLengthInputBuffer(termAttBuffer, termAttLength, maxCodeLength);
        }

        Buffer outputBuffer = null;
        try {
            outputBuffer = encoder.encode(inputBuffer);
        } catch (Exception ignore) {
        }

        if (outputBuffer == null || outputBuffer.getLength() == 0 || outputBuffer.equals(inputBuffer))
            return true;

        if (replace) {
            if (outputBuffer.getLength() > 0) {
                termAtt.copyBuffer(outputBuffer.getBuffer(), 0, outputBuffer.getLength());
            } else {
                termAtt.setEmpty();
            }
            return true;
        }

        int origOffset = posAtt.getPositionIncrement();
        posAtt.setPositionIncrement(0);
        state = captureState();

        posAtt.setPositionIncrement(origOffset);
        if (outputBuffer.getLength() > 0) {
            termAtt.copyBuffer(outputBuffer.getBuffer(), 0, outputBuffer.getLength());
        } else {
            termAtt.setEmpty();
        }
        return true;
    }
}
