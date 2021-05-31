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

import com.github.alturkovic.domain.util.DomainUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ReverseDomainMatcher {
    private final String domain;
    private final List<String> reversedLabels;

    private int matchedLabelsCount = 0;

    ReverseDomainMatcher(String domain) {
        this.domain = domain;
        this.reversedLabels = DomainUtils.reversedDomainLabels(domain);
    }

    String next() {
        if (matchedLabelsCount > reversedLabels.size()) {
            throw new IndexOutOfBoundsException(String.format("%d > %s", matchedLabelsCount, domain));
        }

        String nextLabel = reversedLabels.get(matchedLabelsCount);
        matchedLabelsCount++;
        return nextLabel;
    }

    int size() {
        return reversedLabels.size();
    }

    String matchedDomain() {
        List<String> matchedDomain = new ArrayList<>(reversedLabels.subList(0, matchedLabelsCount));
        Collections.reverse(matchedDomain);
        return DomainUtils.joinLabels(matchedDomain);
    }

    @Override
    public String toString() {
        return domain;
    }
}
