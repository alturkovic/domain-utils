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

package com.github.alturkovic.domain.registry;

import com.github.alturkovic.domain.rule.Rule;
import com.github.alturkovic.domain.rule.RuleComparator;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Used to find rules that match domains.
 * <p>
 * It will return results in O(log(n)) time complexity.
 */
@AllArgsConstructor
public class RuleRegistry {
    private final ImmutableNode root;

    /**
     * Get all registered rules managed by this registry.
     *
     * @return registered rules
     */
    public List<Rule> getRules() {
        List<ImmutableNode> descendants = root.getDescendants();
        return asRuleList(descendants);
    }

    /**
     * Find the prevailing {@link Rule} for {@code domain}.
     *
     * @param domain to match
     * @return the prevailing {@link Rule}
     */
    public Optional<Rule> findRule(String domain) {
        return findRules(domain).stream().max(RuleComparator.INSTANCE);
    }

    /**
     * Find a list of matching rules.
     * <p>
     * This list may not include all matching rules, but will include the prevailing rule.
     *
     * @param domain to match
     * @return matching rules
     */
    public List<Rule> findRules(String domain) {
        List<ImmutableNode> nodes = root.findNodes(domain.toLowerCase());
        return asRuleList(nodes);
    }

    private List<Rule> asRuleList(List<ImmutableNode> nodes) {
        return nodes.stream()
            .map(Node::getRule)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}