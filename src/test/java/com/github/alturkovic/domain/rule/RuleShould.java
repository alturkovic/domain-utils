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

import com.gihub.alturkovic.domain.rule.Rule;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RuleShould {

    @Test
    void matchExact() {
        Rule rule = new Rule("test.com");
        assertThat(rule.match("test.com")).isEqualTo("test.com");
        assertThat(rule.match("sub.test.com")).isEqualTo("test.com");
        assertThat(rule.match("com")).isNull();
        assertThat(rule.match("example.com")).isNull();
        assertThat(rule.match("test.com.co")).isNull();
        assertThat(rule.match("")).isNull();
        assertThat(rule.match(null)).isNull();
    }

    @Test
    void matchWildcard() {
        Rule rule = new Rule("*.com");
        assertThat(rule.match("test.com")).isEqualTo("test.com");
        assertThat(rule.match("sub.test.com")).isEqualTo("test.com");
        assertThat(rule.match("example.com")).isEqualTo("example.com");
        assertThat(rule.match("com")).isNull();
        assertThat(rule.match("test.org")).isNull();
        assertThat(rule.match("test.com.co")).isNull();
        assertThat(rule.match("")).isNull();
        assertThat(rule.match(null)).isNull();
    }

    @Test
    void matchExceptional() {
        Rule rule = new Rule("!test.com");
        assertThat(rule.match("test.com")).isEqualTo("com");
        assertThat(rule.match("sub.test.com")).isEqualTo("com");
        assertThat(rule.match("com")).isNull();
        assertThat(rule.match("example.com")).isNull();
        assertThat(rule.match("test.com.co")).isNull();
        assertThat(rule.match("")).isNull();
        assertThat(rule.match(null)).isNull();
    }
}