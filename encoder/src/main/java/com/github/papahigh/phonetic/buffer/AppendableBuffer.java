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
package com.github.papahigh.phonetic.buffer;


import java.util.Arrays;


/**
 * Represents a char buffer which is responsible for
 * construction of the resulting phonetic code.
 */
public class AppendableBuffer extends AbstractBuffer {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private int length;
    private final int limit;

    public AppendableBuffer(int length) {
        super(new char[length]);
        this.limit = length;
    }

    public void append(char one) {
        expand(length + 1);
        buffer[length++] = one;
    }

    public void append(char... many) {
        expand(length + many.length);
        System.arraycopy(many, 0, buffer, length, many.length);
        length += many.length;
    }

    public void updateLatest(char newLatest) {
        int i = length - 1;
        if (i >= 0) {
            buffer[i] = newLatest;
        }
    }

    @Override
    public int getLength() {
        return Math.min(length, limit);
    }

    @Override
    public String toString() {
        return new String(buffer, 0, getLength());
    }

    private void expand(int newCapacity) {
        int oldCapacity = buffer.length;
        if (newCapacity - oldCapacity > 0) {
            if (MAX_ARRAY_SIZE - newCapacity < 0) {
                throw new OutOfMemoryError();
            }
            buffer = Arrays.copyOf(buffer, newCapacity);
        }
    }
}
