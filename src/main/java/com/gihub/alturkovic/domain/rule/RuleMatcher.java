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

package com.gihub.alturkovic.domain.rule;

import com.gihub.alturkovic.domain.util.DomainUtils;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(of = "pattern")
class RuleMatcher {
    private final String pattern;
    private final List<String> reversedLabels;

    RuleMatcher(String pattern) {
        this.pattern = pattern;
        this.reversedLabels = DomainUtils.reversedDomainLabels(pattern);
    }

    String match(String domain) {
        ReverseDomainMatcher reverseDomainMatcher = new ReverseDomainMatcher(domain);
        if (reverseDomainMatcher.size() < reversedLabels.size()) {
            return null;
        }

        for (String label : reversedLabels) {
            LabelMatcher matcher = new LabelMatcher(label);
            String domainLabel = reverseDomainMatcher.next();
            if (!matcher.isMatch(domainLabel)) {
                return null;
            }
        }
        return reverseDomainMatcher.matchedDomain();
    }

    String getPattern() {
        return pattern;
    }

    int size() {
        return reversedLabels.size();
    }

    @Override
    public String toString() {
        return getPattern();
    }
}
