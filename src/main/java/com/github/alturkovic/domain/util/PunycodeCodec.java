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

import java.net.IDN;

/**
 * Automatic Punycode Codec.
 * <p>
 * This codec remembers at {@link PunycodeCodec#decode(String)} whether the input was encoded or not.
 * <p>
 * The {@link PunycodeCodec#recode(String)} method will return the same format as the original input was.
 *
 * @implNote This codec is stateful and not thread-safe.
 */
public class PunycodeCodec {
    private boolean decoded;

    /**
     * Decodes a domain name into UTF-8.
     *
     * @param domain to decode
     * @return UTF-8 domain name
     */
    public String decode(String domain) {
        String asciiDomain = IDN.toUnicode(domain);
        decoded = !asciiDomain.equals(domain);
        return asciiDomain;
    }

    /**
     * Returns the domain name in the original format.
     * <p>
     * The format is determined in {@link #decode(String)}.
     *
     * @param domain to recode
     * @return domain in the original format
     */
    public String recode(String domain) {
        return decoded ? IDN.toASCII(domain) : domain;
    }
}