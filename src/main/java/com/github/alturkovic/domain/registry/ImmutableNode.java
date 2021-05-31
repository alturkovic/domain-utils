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
import lombok.Getter;

import java.util.*;

@Getter
class ImmutableNode extends Node<ImmutableNode> {
    private final Rule rule;

    ImmutableNode(String label, Map<String, ImmutableNode> children, Rule rule) {
        super(label, Collections.unmodifiableMap(children));
        this.rule = rule;
    }

    List<ImmutableNode> findNodes(String domain) {
        return findNodes(convertDomain(domain));
    }

    List<ImmutableNode> findNodes(Deque<String> labels) {
        List<ImmutableNode> nodes = new LinkedList<>();
        if (labels.isEmpty()) {
            return nodes;
        }

        String searchLabel = labels.removeLast();
        ImmutableNode child = getChild(searchLabel);
        if (child != null) {
            nodes.add(child);
        }

        ImmutableNode wildcard = getWildcard();
        if (wildcard != null) {
            nodes.add(wildcard);
        }

        for (ImmutableNode node : new ArrayList<>(nodes)) {
            nodes.addAll(node.findNodes(new LinkedList<>(labels)));
        }

        return nodes;
    }

    List<ImmutableNode> getDescendants() {
        List<ImmutableNode> descendants = new ArrayList<>(getChildren());
        for (ImmutableNode child : getChildren()) {
            descendants.addAll(child.getDescendants());
        }

        return descendants;
    }
}