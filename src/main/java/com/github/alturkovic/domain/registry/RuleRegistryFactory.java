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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to build {@link RuleRegistry} from a list of rules.
 */
public class RuleRegistryFactory {

    /**
     * Build the {@link RuleRegistry} from {@code rules}.
     *
     * @param rules to register
     * @return registry
     */
    public RuleRegistry build(List<Rule> rules) {
        MutableNode root = new MutableNode(null);
        for (Rule rule : rules) {
            MutableNode node = root.getOrCreateDescendant(rule.getPattern());
            node.setRule(rule);
        }

        return new RuleRegistry(convert(root));
    }

    private ImmutableNode convert(MutableNode node) {
        Map<String, ImmutableNode> convertedChildren = new HashMap<>();
        for (MutableNode child : node.getChildren()) {
            Node.addChild(convert(child), convertedChildren);
        }

        return new ImmutableNode(node.getLabel(), convertedChildren, node.getRule());
    }
}