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
package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.plugin.analysis.RussianPhoneticAnalysisPlugin;
import org.elasticsearch.test.ESTestCase;
import org.junit.Before;

import java.io.IOException;
import java.io.StringReader;

import static org.hamcrest.Matchers.instanceOf;


public class SimpleRussianPhoneticAnalysisTests extends ESTestCase {

    private TestAnalysis analysis;

    @Before
    public void setup() throws IOException {
        String yaml = "/org/elasticsearch/index/analysis/russian_phonetic.yml";
        Settings settings = Settings.builder().loadFromStream(yaml, getClass().getResourceAsStream(yaml), false)
                .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
                .build();
        this.analysis = createTestAnalysis(new Index("test", "_na_"), settings, new RussianPhoneticAnalysisPlugin());
    }

    public void testRussianPhoneticTokenFilterMaxLength4() throws IOException {
        TokenFilterFactory filterFactory = analysis.tokenFilter.get("russian_phonetic_max_length4");

        assertThat(filterFactory, instanceOf(RussianPhoneticTokenFilterFactory.class));

        Tokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader("съешь ещё этих мягких французских булок, да выпей чаю"));
        String[] expected = new String[]{"с2ш", "эщ2", "эт2х", "м2хк", "фр1н", "б3л1", "д1", "в2п2", "ч23"};

        BaseTokenStreamTestCase.assertTokenStreamContents(filterFactory.create(tokenizer), expected);
    }

    public void testRussianPhoneticTokenFilterMaxLength8() throws IOException {
        TokenFilterFactory filterFactory = analysis.tokenFilter.get("russian_phonetic_max_length8");

        assertThat(filterFactory, instanceOf(RussianPhoneticTokenFilterFactory.class));

        Tokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader("съешь ещё этих мягких французских булок, да выпей чаю"));
        String[] expected = new String[]{"с2ш", "эщ2", "эт2х", "м2хк2х", "фр1нц3ск", "б3л1к", "д1", "в2п2й", "ч23"};

        BaseTokenStreamTestCase.assertTokenStreamContents(filterFactory.create(tokenizer), expected);
    }

    public void testRussianPhoneticTokenFilterMaxLength14WithStemmer() throws IOException {
        TokenFilterFactory filterFactory = analysis.tokenFilter.get("russian_phonetic_max_length14_with_stemmer");

        assertThat(filterFactory, instanceOf(RussianPhoneticTokenFilterFactory.class));

        Tokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader("съешь ещё этих мягких французских булок, да выпей чаю"));
        String[] expected = new String[]{"с2ш", "эщ2", "эт", "м2хк", "фр1нц3ск", "б3л1к", "д1", "в2п", "ч2"};

        BaseTokenStreamTestCase.assertTokenStreamContents(filterFactory.create(tokenizer), expected);
    }
}
