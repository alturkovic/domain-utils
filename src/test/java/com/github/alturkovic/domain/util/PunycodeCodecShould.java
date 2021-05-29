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

import com.gihub.alturkovic.domain.util.PunycodeCodec;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PunycodeCodecShould {

    @Test
    void recodeToOriginalFormat() {
        assertPunycodeRecode("个人.hk", "个人.hk");
        assertPunycodeRecode("xn--ciqpn.hk", "个人.hk");
        assertPunycodeRecode("test.com", "test.com");
    }

    private void assertPunycodeRecode(String originalDomain, String expectedDecodedDomain) {
        PunycodeCodec codec = new PunycodeCodec();
        assertThat(codec.decode(originalDomain)).isEqualTo(expectedDecodedDomain);
        assertThat(codec.recode(originalDomain)).isEqualTo(originalDomain);
    }
}