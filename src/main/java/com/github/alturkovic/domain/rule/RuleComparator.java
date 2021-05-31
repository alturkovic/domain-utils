/*
 * MIT License
 *
 * Copyright (c) 2021 Alen Turkovic
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.alturkovic.domain.rule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;

/**
 * Orders prevailing rules higher.
 * <p>
 * The rule with the highest {@link Rule#size()} is the prevailing rule.
 * <p>
 * An exception rule is always the prevailing rule.
 *
 * @see Rule#isExceptionRule()
 * @see Rule#size()
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RuleComparator implements Comparator<Rule> {
    public static final RuleComparator INSTANCE = new RuleComparator();

    @Override
    public int compare(Rule rule1, Rule rule2) {
        if (rule1.isExceptionRule() && rule2.isExceptionRule()) {
            if (!rule1.equals(rule2)) {
                throw new IllegalArgumentException("You can't compare two exception rules.");
            }
            return 0;
        }

        if (rule1.isExceptionRule()) {
            return 1;
        }

        if (rule2.isExceptionRule()) {
            return -1;
        }

        return Integer.compare(rule1.size(), rule2.size());
    }
}