package com.github.papahigh.phonetic.support;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.lucene.analysis.CharacterUtils;

import java.util.Arrays;


public final class TransliteratingAdapter implements StringEncoder {

    private final StringEncoder encoder;

    TransliteratingAdapter(StringEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String encode(String source) throws EncoderException {
        return encoder.encode(transliterate(source));
    }

    @Override
    public Object encode(Object source) throws EncoderException {
        return encode((String) source);
    }

    private static final char[][] charMapping = new char['ё' - 'Ё' + 1][];
    private static final char offset = 'Ё';

    private static String transliterate(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            final int index = c - offset;
            if (index >= 0 && index < charMapping.length) {
                char[] result = charMapping[index];
                if (result != null) {
                    sb.append(result);
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    static {

        // http://gostrf.com/normadata/1/4294816/4294816248.pdf

        charMapping['А' - offset] = "A".toCharArray();
        charMapping['Б' - offset] = "B".toCharArray();
        charMapping['В' - offset] = "V".toCharArray();
        charMapping['Г' - offset] = "G".toCharArray();
        charMapping['Д' - offset] = "D".toCharArray();
        charMapping['Е' - offset] = "E".toCharArray();
        charMapping['Ё' - offset] = "Yo".toCharArray();
        charMapping['Ж' - offset] = "Zh".toCharArray();
        charMapping['З' - offset] = "Z".toCharArray();
        charMapping['И' - offset] = "I".toCharArray();
        charMapping['Й' - offset] = "J".toCharArray();
        charMapping['К' - offset] = "K".toCharArray();
        charMapping['Л' - offset] = "L".toCharArray();
        charMapping['М' - offset] = "M".toCharArray();
        charMapping['Н' - offset] = "N".toCharArray();
        charMapping['О' - offset] = "O".toCharArray();
        charMapping['П' - offset] = "P".toCharArray();
        charMapping['Р' - offset] = "R".toCharArray();
        charMapping['С' - offset] = "S".toCharArray();
        charMapping['Т' - offset] = "T".toCharArray();
        charMapping['У' - offset] = "U".toCharArray();
        charMapping['Ф' - offset] = "F".toCharArray();
        charMapping['Х' - offset] = "Kh".toCharArray();
        charMapping['Ц' - offset] = "Cz".toCharArray();
        charMapping['Ч' - offset] = "Ch".toCharArray();
        charMapping['Ш' - offset] = "Sh".toCharArray();
        charMapping['Щ' - offset] = "Shh".toCharArray();
        charMapping['Ъ' - offset] = "".toCharArray();
        charMapping['Ы' - offset] = "Y".toCharArray();
        charMapping['Ь' - offset] = "".toCharArray();
        charMapping['Э' - offset] = "E".toCharArray();
        charMapping['Ю' - offset] = "Yu".toCharArray();
        charMapping['Я' - offset] = "Ya".toCharArray();

        for (char i = 0; i < charMapping.length; i++) {
            char[] chars = charMapping[i];
            if (chars != null) {
                char[] copy = Arrays.copyOf(chars, chars.length);
                CharacterUtils.toLowerCase(copy, 0, copy.length);
                charMapping[Character.toLowerCase(i + offset) - offset] = copy;
            }
        }
    }
}
