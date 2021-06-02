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
import com.github.alturkovic.domain.util.DomainUtils;

import java.util.*;

abstract class Node<T extends Node<T>> {
    private final String label;
    private final Map<String, T> children;

    Node(String label) {
        this(label, new HashMap<>());
    }

    Node(String label, Map<String, T> children) {
        this.label = (label == null) ? null : label.toLowerCase();
        this.children = children;
    }

    Collection<T> getChildren() {
        return children.values();
    }

    T getChild(String childLabel) {
        return children.get(childLabel.toLowerCase());
    }

    void addChild(T node) {
        addChild(node, children);
    }

    String getLabel() {
        return label;
    }

    abstract Rule getRule();

    T getWildcard() {
        return children.get(Rule.WILDCARD);
    }

    Deque<String> convertDomain(String domain) {
        return new LinkedList<>(DomainUtils.splitLabels(domain));
    }

    static <T extends Node<T>> void addChild(T child, Map<String, T> children) {
        children.put(child.getLabel(), child);
    }

    @Override
    public String toString() {
        return label;
    }
}