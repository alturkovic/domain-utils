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

import com.github.alturkovic.domain.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The parser for the Public Suffix List rules.
 */
public class RuleParser {

    /**
     * Parses all rules from a stream.
     *
     * @param stream the stream with lines of rules
     * @return parsed rule list
     */
    public List<Rule> parse(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        return reader.lines()
            .map(String::trim)
            .map(this::parseLine)
            .flatMap(rule -> rule.<Stream<? extends Rule>>map(Stream::of).orElseGet(Stream::empty))
            .collect(Collectors.toList());
    }

    /**
     * Parse a single line to find a {@link Rule} in it.
     *
     * @param line to parse
     * @return parsed line
     */
    public Optional<Rule> parseLine(String line) {
        if (StringUtils.isBlank(line)) {
            return Optional.empty();
        }

        line = line.trim();

        if (isComment(line)) {
            return Optional.empty();
        }

        if (isMultiWorded(line)) {
            line = extractFirstWord(line);
        }

        return Optional.of(new Rule(line));
    }

    private boolean isComment(String line) {
        return line.startsWith("//");
    }

    private boolean isMultiWorded(String line) {
        return line.contains(" ");
    }

    private String extractFirstWord(String line) {
        return line.split(" ")[0];
    }
}