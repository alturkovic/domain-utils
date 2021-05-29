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
import com.gihub.alturkovic.domain.util.StringUtils;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * The Public Suffix rule.
 * <p>
 * See <a href="https://publicsuffix.org">Public Suffix</a>
 */
@EqualsAndHashCode
public class Rule {
    public static final char EXCEPTION_TOKEN = '!';
    public static final String WILDCARD = "*";

    private final RuleMatcher matcher;
    private final boolean exceptionRule;

    /**
     * Initialize a Rule from a pattern as specified by Public Suffix specification.
     *
     * @param pattern to use for initialization
     */
    public Rule(String pattern) {
        this.exceptionRule = pattern.charAt(0) == Rule.EXCEPTION_TOKEN;
        if (this.exceptionRule) {
            pattern = pattern.substring(1);
        }
        this.matcher = new RuleMatcher(pattern);
    }

    /**
     * Returns the matched public suffix of a domain.
     *
     * @param domain to match
     * @return public suffix for {@code domain}, or {@code null}
     */
    public String match(String domain) {
        if (StringUtils.isBlank(domain)) {
            return null;
        }

        String matchedDomain = matcher.match(domain);
        if (StringUtils.isBlank(matchedDomain)) {
            return null;
        }

        if (!isExceptionRule()) {
            return matchedDomain;
        }

        return exceptionalMatch(matchedDomain);
    }

    /**
     * Returns the rule pattern.
     * <p>
     * The exception token is not included in the pattern!
     *
     * @return rule pattern
     */
    public String getPattern() {
        return matcher.getPattern();
    }

    /**
     * Returns if this rule is an exception rule.
     * <p>
     * Exception rules are always prevailing rules.
     *
     * @return {@code true} if this is an exception rule
     */
    public boolean isExceptionRule() {
        return exceptionRule;
    }

    int size() {
        return matcher.size();
    }

    @Override
    public String toString() {
        String pattern = matcher.toString();
        return isExceptionRule() ? EXCEPTION_TOKEN + pattern : pattern;
    }

    private String exceptionalMatch(String match) {
        List<String> labels = DomainUtils.splitLabels(match);
        return DomainUtils.joinLabels(labels.subList(1, labels.size()));
    }
}
