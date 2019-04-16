package com.github.papahigh.phonetic.support;

public class RussianPhoneticEncoderWithStemmingTests {
//
//    private void testEquals(String source1, String source2) {
//        Arrays.stream(VowelsMode.values())
//                .forEach(vowelsMode ->
//                        IntStream.range(4, 20)
//                                .mapToObj(maxLength -> russianPhoneticWithStemmer(vowelsMode, maxLength))
//                                .forEach(Unchecked.consumer(encoder -> {
//                                            try {
//                                                System.out.println("encoder: " + encoder);
//                                                System.out.println("source1: " + source1);
//                                                System.out.println("source2: " + source2);
//                                                assertEquals(encoder.encode(source1), encoder.encode(source2));
//                                            } catch (Exception e) {
//                                                System.out.println(e);
//                                            }
//
//                                        }
//                                ))
//                );
//
//    }
//
//    @Test
//    public void testMaxLengthGuarantee() throws EncoderException {
//
//        String word = "голландский";
//
//        StringEncoder encoder = russianPhoneticWithStemmer(VowelsMode.ENCODE_ALL, 100);
//
//        RussianStemmer stemmer = new RussianStemmer();
//        stemmer.setCurrent(word);
//        stemmer.stem();
//        int stemmedLength = stemmer.getCurrentBufferLength();
//
//        assertEquals(stemmedLength, word.length() - 2); // -ий
//
//        assertEquals(encoder.encode(word).length(), stemmedLength - 2); // л, д
//
//        encoder = russianPhoneticWithStemmer(VowelsMode.ENCODE_ALL, 4);
//        assertEquals(encoder.encode(word).length(), 4); // limited by max length
//
//        encoder = russianPhoneticWithStemmer(VowelsMode.ENCODE_FIRST, 100);
//        assertEquals(encoder.encode(word).length(), stemmedLength - 4); // о, л, а, д
//
//        encoder = russianPhoneticWithStemmer(VowelsMode.ENCODE_FIRST, 4);
//        assertEquals(encoder.encode(word).length(), 4);  // limited by max length
//    }
//
//    @Test
//    public void testBasicMorphology() {
//        testEquals("ящурным", "ящурные");
//        testEquals("аннигиляционный", "аннигиляционному");
//        testEquals("аннотируешь", "аннотируешься");
//        testEquals("аномальным", "аномально");
//        testEquals("яснооко", "ясноокого");
//    }
//
//    @Test
//    public void testSingularAndPlurals() {
//        testEquals("отгребальщик", "отгребальщики");
//        testEquals("отвиливание", "отвиливания");
//        testEquals("отвечание", "отвечания");
//        testEquals("отвиливание", "отвиливания");
//        testEquals("отгрузка", "отгрузки");
//        testEquals("отгул", "отгулы");
//    }
}
