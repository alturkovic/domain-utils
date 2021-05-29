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

package com.gihub.alturkovic.domain.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DomainUtils {

    /**
     * Splits a pattern into its labels.
     * <p>
     * Splitting is done at ".".
     *
     * @param pattern to split
     * @return labels
     */
    public static List<String> splitLabels(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return Collections.emptyList();
        }

        return List.of(pattern.split("\\."));
    }

    /**
     * Splits a pattern into its labels and reverses their order.
     * <p>
     * Splitting is done at ".".
     *
     * @param pattern to split
     * @return reversed labels
     */
    public static List<String> reversedDomainLabels(String pattern) {
        List<String> labels = DomainUtils.splitLabels(pattern);
        List<String> reversedLabels = new ArrayList<>(labels);
        Collections.reverse(reversedLabels);
        return reversedLabels;
    }

    /**
     * Joins labels to a pattern.
     * <p>
     * Joining is done with ".".
     *
     * @param labels to join
     * @return joined rule pattern
     */
    public static String joinLabels(Collection<String> labels) {
        if (labels == null || areAllElementsBlank(labels)) {
            return null;
        }

        return String.join(".", labels);
    }

    private static boolean areAllElementsBlank(Collection<String> labels) {
        return labels.stream().allMatch(String::isBlank);
    }
}
