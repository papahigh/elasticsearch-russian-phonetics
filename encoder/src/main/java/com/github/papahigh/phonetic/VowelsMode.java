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
package com.github.papahigh.phonetic;


/**
 * Encoding mode for vowels.
 */
public enum VowelsMode {

    /**
     * Only first vowel in word will be encoded
     */
    ENCODE_FIRST,

    /**
     * All vowels in word will be encoded
     */
    ENCODE_ALL,

    /**
     * Vowels will be ignored
     */
    IGNORE

}
