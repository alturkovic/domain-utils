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

package com.github.alturkovic.domain;

import com.github.alturkovic.domain.registry.RuleRegistry;
import com.github.alturkovic.domain.registry.RuleRegistryFactory;
import com.github.alturkovic.domain.rule.Rule;
import com.github.alturkovic.domain.rule.RuleParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Used to build {@link DomainRegistry} instances.
 */
public class DomainRegistryBuilder {
    private final static String DEFAULT_RULES = "https://publicsuffix.org/list/effective_tld_names.dat";

    private final List<Rule> rules = new ArrayList<>();

    /**
     * Add default rules as defined <a href="https://publicsuffix.org/list/effective_tld_names.dat">here</a>.
     *
     * @return this builder
     */
    public DomainRegistryBuilder withDefaultRules() {
        try (InputStream rules = new URL(DEFAULT_RULES).openStream()) {
            return from(rules);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Add rules from {@code fileName} to this builder.
     *
     * @param fileName to read rules from
     * @return this builder
     */
    public DomainRegistryBuilder fromFile(String fileName) {
        return from(getClass().getResourceAsStream(fileName));
    }

    /**
     * Add rules from {@code inputStream} to this builder.
     *
     * @param inputStream to read rules from
     * @return this builder
     */
    public DomainRegistryBuilder from(InputStream inputStream) {
        List<Rule> rules = new RuleParser().parse(inputStream);
        return withRules(rules);
    }

    /**
     * Add {@code rule} to this builder.
     *
     * @param rule to add
     * @return this builder
     */
    public DomainRegistryBuilder withRule(String rule) {
        return withRule(new Rule(rule));
    }

    /**
     * Add {@code rule} to this builder.
     *
     * @param rule to add
     * @return this builder
     */
    public DomainRegistryBuilder withRule(Rule rule) {
        return withRules(Collections.singletonList(rule));
    }

    /**
     * Add {@code rules} to this builder.
     *
     * @param rules to add
     * @return this builder
     */
    public DomainRegistryBuilder withRules(List<Rule> rules) {
        this.rules.addAll(rules);
        return this;
    }

    /**
     * Build a {@link DomainRegistry} using the registered rules from this builder.
     *
     * @return initialized registry
     */
    public DomainRegistry build() {
        RuleRegistry ruleRegistry = new RuleRegistryFactory().build(rules);
        if (ruleRegistry.getRules().isEmpty()) {
            throw new IllegalArgumentException("No rules registered");
        }
        return new DomainRegistry(ruleRegistry);
    }
}