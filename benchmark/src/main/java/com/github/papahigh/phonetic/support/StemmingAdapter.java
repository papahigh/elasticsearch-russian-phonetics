package com.github.papahigh.phonetic.support;

import com.github.papahigh.phonetic.PhoneticEncoder;
import com.github.papahigh.phonetic.VowelsMode;
import com.github.papahigh.phonetic.buffer.Buffer;
import com.github.papahigh.phonetic.buffer.InputBuffer;
import com.github.papahigh.phonetic.buffer.StemmedInputBuffer;
import com.github.papahigh.phonetic.phoneme.russian.RussianPhonemeExchange;
import com.github.papahigh.phonetic.phoneme.russian.RussianPhonemeRules;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.lucene.analysis.CharacterUtils;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.RussianStemmer;


/**
 * This class is not thread safe since it has a stemmer in it's state.
 * The purpose of this class is to simulate stemming analysis in the same manner as it happens in TokenFilter.
 * For example, in org.apache.lucene.analysis.snowball.SnowballFilter.
 * <p>
 * Result of the stemming analysis is passed to phonetic encoder with StemmedInputBuffer
 * <p>
 * This adapter should be used in single thread test environment.
 *
 * @see org.apache.lucene.analysis.TokenFilter
 * @see StemmedInputBuffer
 */
final class StemmingAdapter implements StringEncoder {

    private final PhoneticEncoder encoder;
    private final SnowballProgram stemmer;
    private final int maxCodeLength;

    StemmingAdapter(VowelsMode vowelsMode, int maxCodeLength) {
        this.encoder = new PhoneticEncoder(
                RussianPhonemeExchange.FACTORY,
                RussianPhonemeRules.CONSONANTS,
                RussianPhonemeRules.VOWELS,
                vowelsMode,
                maxCodeLength
        );
        this.stemmer = new RussianStemmer();
        this.maxCodeLength = maxCodeLength;
    }

    @Override
    public String encode(String source) {

        int termAttLength = source.length();
        char[] termAttBuffer = source.toCharArray();

        CharacterUtils.toLowerCase(termAttBuffer, 0, termAttLength);

        stemmer.setCurrent(termAttBuffer, termAttLength);
        stemmer.stem();

        InputBuffer inputBuffer = new StemmedInputBuffer(
                stemmer.getCurrentBuffer(),
                termAttLength,
                stemmer.getCurrentBufferLength(),
                maxCodeLength
        );

        Buffer code = encoder.encode(inputBuffer);
        if (code.getLength() == 0) {
            return "";
        }
        return new String(code.getBuffer(), 0, code.getLength());
    }

    @Override
    public Object encode(Object source) throws EncoderException {
        if (!(source instanceof String)) {
            throw new EncoderException("Parameter supplied to [PhoneticEncoder] is not of type [java.lang.String]");
        }
        return encode((String) source);
    }
}
