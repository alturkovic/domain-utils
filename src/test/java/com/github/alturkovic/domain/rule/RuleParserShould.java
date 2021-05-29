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
import com.gihub.alturkovic.domain.rule.RuleParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RuleParserShould {

    private final RuleParser parser = new RuleParser();

    @Test
    void notParseNullLine() {
        assertThat(parser.parseLine(null)).isEmpty();
    }

    @Test
    void notParseEmptyLine() {
        assertThat(parser.parseLine("")).isEmpty();
    }

    @Test
    void notParseBlankLine() {
        assertThat(parser.parseLine(" ")).isEmpty();
    }

    @Test
    void notParseNewLine() {
        assertThat(parser.parseLine(System.lineSeparator())).isEmpty();
    }

    @Test
    void notParseCommentLine() {
        assertThat(parser.parseLine("//comment")).isEmpty();
        assertThat(parser.parseLine(" //comment")).isEmpty();
    }

    @Test
    void parseRuleLine() {
        Optional<Rule> rule = parser.parseLine("com");
        assertThat(rule).isPresent();
        assertThat(rule.get().getPattern()).isEqualTo("com");
        assertThat(rule.get().isExceptionRule()).isFalse();
    }

    @Test
    void parseWildcardRuleLine() {
        Optional<Rule> rule = parser.parseLine("*.com");
        assertThat(rule).isPresent();
        assertThat(rule.get().getPattern()).isEqualTo("*.com");
        assertThat(rule.get().isExceptionRule()).isFalse();
    }

    @Test
    void parseExceptionRuleLine() {
        Optional<Rule> rule = parser.parseLine("!test.com");
        assertThat(rule).isPresent();
        assertThat(rule.get().getPattern()).isEqualTo("test.com");
        assertThat(rule.get().isExceptionRule()).isTrue();
    }

    @Test
    void parseEdgeRules() {
        assertRulePattern("/", "/");
        assertRulePattern("/\n", "/");
        assertRulePattern(" com ", "com");
        assertRulePattern("com\n", "com");
        assertRulePattern("com //comment", "com");
        assertRulePattern("com comment", "com");
    }

    @Test
    void parseFromInputStream() {
        List<Rule> rules = parser.parse(RuleParserShould.class.getResourceAsStream("/rule-parser.dat"));
        assertThat(rules).containsExactly(
            new Rule("ac"),
            new Rule("com.ac"),
            new Rule("*.ck"),
            new Rule("!www.ck"),
            new Rule("hk"),
            new Rule("个人.hk")
        );
    }

    private void assertRulePattern(String ruleToParse, String expectedPattern) {
        Optional<Rule> rule = parser.parseLine(ruleToParse);
        assertThat(rule).isPresent();
        assertThat(rule.get().getPattern()).isEqualTo(expectedPattern);
    }
}