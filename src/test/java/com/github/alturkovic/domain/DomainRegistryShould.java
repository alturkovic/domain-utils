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

import lombok.Builder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DomainRegistryShould {

    private final DomainRegistry registry = new DomainRegistryBuilder()
        .withRule("com")
        .withRule("blogspot.com")
        .withRule("blogspot.com.co")
        .withRule("*.ck")
        .withRule("!www.ck")
        .withRule("个人.hk")
        .withRule("com.cn")
        .build();

    @Test
    void extractFromRules() {
        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("com")
            .withoutSubDomain(null)
            .subDomain(null)
            .registrableName(null)
            .publicSuffix("com")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("test.com")
            .withoutSubDomain("test.com")
            .subDomain(null)
            .registrableName("test")
            .publicSuffix("com")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("test.blogspot.com")
            .withoutSubDomain("test.blogspot.com")
            .subDomain(null)
            .registrableName("test")
            .publicSuffix("blogspot.com")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("sub.test.blogspot.com")
            .withoutSubDomain("test.blogspot.com")
            .subDomain("sub")
            .registrableName("test")
            .publicSuffix("blogspot.com")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("test.blogspot.com.co")
            .withoutSubDomain("test.blogspot.com.co")
            .subDomain(null)
            .registrableName("test")
            .publicSuffix("blogspot.com.co")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("pub.ck")
            .withoutSubDomain(null)
            .subDomain(null)
            .registrableName(null)
            .publicSuffix("pub.ck")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("test.pub.ck")
            .withoutSubDomain("test.pub.ck")
            .subDomain(null)
            .registrableName("test")
            .publicSuffix("pub.ck")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("sub.test.pub.ck")
            .withoutSubDomain("test.pub.ck")
            .subDomain("sub")
            .registrableName("test")
            .publicSuffix("pub.ck")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("www.ck")
            .withoutSubDomain("www.ck")
            .subDomain(null)
            .registrableName("www")
            .publicSuffix("ck")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("sub.www.ck")
            .withoutSubDomain("www.ck")
            .subDomain("sub")
            .registrableName("www")
            .publicSuffix("ck")
            .build());
    }

    @Test
    void extractRespectingPunycode() {
        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("教育.个人.hk")
            .withoutSubDomain("教育.个人.hk")
            .subDomain(null)
            .registrableName("教育")
            .publicSuffix("个人.hk")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("xn--wcvs22d.xn--ciqpn.hk")
            .withoutSubDomain("xn--wcvs22d.xn--ciqpn.hk")
            .subDomain(null)
            .registrableName("xn--wcvs22d")
            .publicSuffix("xn--ciqpn.hk")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("教育.com.cn")
            .withoutSubDomain("教育.com.cn")
            .subDomain(null)
            .registrableName("教育")
            .publicSuffix("com.cn")
            .build());

        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("xn--wcvs22d.com.cn")
            .withoutSubDomain("xn--wcvs22d.com.cn")
            .subDomain(null)
            .registrableName("xn--wcvs22d")
            .publicSuffix("com.cn")
            .build());
    }

    @Test
    void notExtractInvalid() {
        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain("test.invalid")
            .withoutSubDomain(null)
            .subDomain(null)
            .registrableName(null)
            .publicSuffix(null)
            .build());
    }

    @Test
    void notExtractFromNull() {
        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain(null)
            .withoutSubDomain(null)
            .subDomain(null)
            .registrableName(null)
            .publicSuffix(null)
            .build());
    }

    @Test
    void notExtractFromBlank() {
        validateDomainRegistryForDomain(DomainTestCase.builder()
            .originalDomain(" ")
            .withoutSubDomain(null)
            .subDomain(null)
            .registrableName(null)
            .publicSuffix(null)
            .build());
    }

    private void validateDomainRegistryForDomain(DomainTestCase testCase) {
        assertThat(registry.getPublicSuffix(testCase.originalDomain)).isEqualTo(testCase.publicSuffix);
        assertThat(registry.getRegistrableName(testCase.originalDomain)).isEqualTo(testCase.registrableName);
        assertThat(registry.getSubDomain(testCase.originalDomain)).isEqualTo(testCase.subDomain);
        assertThat(registry.stripSubDomain(testCase.originalDomain)).isEqualTo(testCase.withoutSubDomain);
    }

    @Builder
    private static class DomainTestCase {
        private final String originalDomain;
        private final String publicSuffix;
        private final String registrableName;
        private final String subDomain;
        private final String withoutSubDomain;
    }
}