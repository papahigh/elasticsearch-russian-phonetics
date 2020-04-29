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
package com.github.papahigh.phonetic.buffer;


/**
 * Represents input buffer with fixed max-length limit.
 */
public class LimitedLengthInputBuffer extends InputBuffer {

    private final int maxLength;

    public LimitedLengthInputBuffer(char[] buffer, int bufferLength, int maxLength) {
        super(buffer, bufferLength);
        this.maxLength = maxLength;
    }

    @Override
    public void dropChar() {
        // do nothing
    }

    @Override
    public void dropChars(int n) {
        // do nothing
    }

    @Override
    public void dropChars2() {
        // do nothing
    }

    @Override
    public int getLimit() {
        return maxLength;
    }

}
