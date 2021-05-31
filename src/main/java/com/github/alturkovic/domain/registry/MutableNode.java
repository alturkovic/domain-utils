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
import lombok.Setter;

import java.util.Deque;

@Getter
@Setter
class MutableNode extends Node<MutableNode> {
    private Rule rule;

    MutableNode(String label) {
        super(label);
    }

    MutableNode getOrCreateDescendant(String rulePattern) {
        return getOrCreateDescendant(convertDomain(rulePattern));
    }

    MutableNode getOrCreateChild(String childLabel) {
        MutableNode child = getChild(childLabel);
        if (child == null) {
            child = new MutableNode(childLabel);
            addChild(child);
        }

        return child;
    }

    private MutableNode getOrCreateDescendant(Deque<String> labels) {
        if (labels.isEmpty()) {
            return this;
        }

        MutableNode child = getOrCreateChild(labels.removeLast());
        return child.getOrCreateDescendant(labels);
    }
}