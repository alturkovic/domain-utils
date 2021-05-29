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

package com.github.alturkovic.domain.util;

import com.gihub.alturkovic.domain.util.DomainUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DomainUtilsShould {

    @Test
    void joinLabels() {
        assertThat(DomainUtils.joinLabels(null)).isNull();
        assertThat(DomainUtils.joinLabels(Collections.emptyList())).isNull();
        assertThat(DomainUtils.joinLabels(List.of("", " "))).isNull();
        assertThat(DomainUtils.joinLabels(List.of("com"))).isEqualTo("com");
        assertThat(DomainUtils.joinLabels(List.of("test", "com"))).isEqualTo("test.com");
        assertThat(DomainUtils.joinLabels(List.of("sub", "test", "com"))).isEqualTo("sub.test.com");
        assertThat(DomainUtils.joinLabels(List.of("个人", "hk"))).isEqualTo("个人.hk");
    }

    @Test
    void splitLabels() {
        assertThat(DomainUtils.splitLabels(null)).isEmpty();
        assertThat(DomainUtils.splitLabels("")).isEmpty();
        assertThat(DomainUtils.splitLabels("com")).containsExactly("com");
        assertThat(DomainUtils.splitLabels("test.com")).containsExactly("test", "com");
        assertThat(DomainUtils.splitLabels("sub.test.com")).containsExactly("sub", "test", "com");
        assertThat(DomainUtils.splitLabels("个人.hk")).containsExactly("个人", "hk");

    }
}