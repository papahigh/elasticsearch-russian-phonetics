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
package com.github.papahigh.phonetic.phoneme;


import java.util.function.Consumer;
import java.util.function.Predicate;


/**
 * Holds an array of encoding rules indexed by char and dispatches
 * encoding context to a particular rule for current (exchange.getCurr()) character
 * in PhonemeExchange.
 *
 * @see PhonemeExchange
 */
public class PhonemeRuler implements PhonemeRule {

    private final int offset;
    private final PhonemeRule[] rules;
    private final Predicate<PhonemeExchange> rulePreHandler;
    private final Consumer<PhonemeExchange> rulePostHandler;

    PhonemeRuler(int offset, PhonemeRule[] rules, Predicate<PhonemeExchange> rulePreHandler, Consumer<PhonemeExchange> rulePostHandler) {
        this.offset = offset;
        this.rules = rules;
        this.rulePreHandler = rulePreHandler;
        this.rulePostHandler = rulePostHandler;
    }

    PhonemeRule getRule(char letter) {
        int index = letter - offset;
        if (index >= 0 && index < rules.length) {
            return rules[index];
        }
        return null;
    }

    /**
     * Dispatches encoding context to a particular rule.
     *
     * @param exchange encoding context
     */
    @Override
    public void apply(PhonemeExchange exchange) {
        if (rulePreHandler == null || rulePreHandler.test(exchange)) {
            char curr = exchange.getCurr();
            PhonemeRule rule = getRule(curr);
            if (rule != null) {
                rule.apply(exchange);
            }
            if (exchange.isValid()) {
                exchange.addEncoded(curr).flush();
            }
            if (rulePostHandler != null) {
                rulePostHandler.accept(exchange);
            }
        }
    }

    public static Builder currRule(char from, char to, int offset) {
        return new Builder(offset, new PhonemeRule[to - from + 1]);
    }

    public static Builder nextRule(char from, char to, int offset) {
        return new NextPhonemeRuler.NextBuilder(offset, new PhonemeRule[to - from + 1]);
    }

    public static Builder afterNextRule(char from, char to, int offset) {
        return new AfterNextPhonemeRuler.AfterNextBuilder(offset, new PhonemeRule[to - from + 1]);
    }

    public static class Builder {

        final int offset;
        final PhonemeRule[] rules;

        Predicate<PhonemeExchange> rulePreHandler;
        Consumer<PhonemeExchange> rulePostHandler;

        Builder(int offset, PhonemeRule[] rules) {
            this.offset = offset;
            this.rules = rules;
        }

        public Builder on(char letter, PhonemeRule rule) {
            int index = letter - offset;
            if (index >= 0 && index < rules.length) {
                if (rules[index] != null) {
                    throw new IllegalArgumentException(String.format("Rule for letter %c already registered!", letter));
                }
                rules[index] = rule;
            } else {
                throw new IndexOutOfBoundsException();
            }
            return this;
        }

        public Builder preHandler(Predicate<PhonemeExchange> preHandler) {
            this.rulePreHandler = preHandler;
            return this;
        }

        public Builder postHandler(Consumer<PhonemeExchange> postHandler) {
            this.rulePostHandler = postHandler;
            return this;
        }

        public PhonemeRuler build() {
            return new PhonemeRuler(offset, rules, rulePreHandler, rulePostHandler);
        }
    }


    /**
     * Dispatches encoding context to a particular rule
     * for the next (exchange.getNext()) character in PhonemeExchange.
     *
     * @see PhonemeExchange
     */
    static class NextPhonemeRuler extends PhonemeRuler {
        NextPhonemeRuler(int offset, PhonemeRule[] rules) {
            super(offset, rules, null, null);
        }

        @Override
        public void apply(PhonemeExchange exchange) {
            PhonemeRule next = getRule(exchange.getNext());
            if (next != null) {
                next.apply(exchange);
            }
        }

        static class NextBuilder extends Builder {
            NextBuilder(int offset, PhonemeRule[] rules) {
                super(offset, rules);
            }

            public NextPhonemeRuler build() {
                return new NextPhonemeRuler(offset, rules);
            }
        }
    }


    /**
     * Dispatches encoding context to a particular rule
     * for the next after next (exchange.getNext(2)) character in PhonemeExchange.
     *
     * @see PhonemeExchange
     */
    static class AfterNextPhonemeRuler extends PhonemeRuler {
        AfterNextPhonemeRuler(int offset, PhonemeRule[] rules) {
            super(offset, rules, null, null);
        }

        @Override
        public void apply(PhonemeExchange exchange) {
            PhonemeRule afterNext = getRule(exchange.getNext(2));
            if (afterNext != null) {
                afterNext.apply(exchange);
            }
        }

        static class AfterNextBuilder extends Builder {
            AfterNextBuilder(int offset, PhonemeRule[] rules) {
                super(offset, rules);
            }

            public AfterNextPhonemeRuler build() {
                return new AfterNextPhonemeRuler(offset, rules);
            }
        }
    }
}
