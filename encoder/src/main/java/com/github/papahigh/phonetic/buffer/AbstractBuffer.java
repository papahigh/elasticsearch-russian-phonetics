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


import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;

import java.util.Arrays;

public abstract class AbstractBuffer implements Buffer {

    protected char buffer[];

    AbstractBuffer(char[] buffer) {
        this.buffer = buffer;
    }

    public char charAt(int n) {
        char ch = Character.MIN_VALUE;
        if (n >= 0 && n < getLength()) {
            ch = buffer[n];
        }
        return ch;
    }

    @Override
    public char[] getBuffer() {
        return buffer;
    }

    abstract public int getLength();

    @Override
    public boolean equals(Object obj) {
        int length = getLength();
        if (obj instanceof AbstractBuffer && length == ((AbstractBuffer) obj).getLength()) {
            char[] otherBuffer = ((AbstractBuffer) obj).getBuffer();
            if (isAtLeastJava9) {
                // use intrinsic vectorizedMismatch (available since 9)
                return Arrays.equals(buffer, 0, length, otherBuffer, 0, length);
            } else {
                for (int i = 0; i < length; i++) {
                    if (buffer[i] != otherBuffer[i]) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(buffer);
    }

    private static final boolean isAtLeastJava9 = SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_9);

}
