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

import com.github.alturkovic.domain.registry.RuleRegistry;
import com.github.alturkovic.domain.util.DomainUtils;
import com.github.alturkovic.domain.util.PunycodeCodec;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * API for the Public Suffix List rules.
 * <p>
 * Use {@link DomainRegistryBuilder} for building this registry.
 * <p>
 * You can use all methods with UTF-8 domain names or Punycode encoded ASCII domain names.
 * The API will return the results in the same format as the input was.
 * <p>
 * The API is case insensitive.
 *
 * @see DomainRegistryBuilder
 * @see <a href="https://publicsuffix.org/">https://publicsuffix.org/</a>
 */
@AllArgsConstructor
public class DomainRegistry {
    private final RuleRegistry ruleRegistry;

    /**
     * Find the public suffix of a domain.
     * <p>
     * If the domain is already a public suffix, it will be returned unchanged.
     * <p>
     * This method is case insensitive.
     *
     * @param domain to find public suffix for
     * @return public suffix
     */
    public Optional<String> getPublicSuffix(String domain) {
        PunycodeCodec punycode = new PunycodeCodec();
        String decodedDomain = punycode.decode(domain);

        return ruleRegistry.findRule(decodedDomain)
            .flatMap(rule -> rule.match(decodedDomain))
            .map(punycode::recode);
    }

    /**
     * Find the registrable domain name.
     * <p>
     * This method is case insensitive.
     *
     * @param domain to find registrable name for
     * @return registrable domain name
     */
    public Optional<String> getRegistrableName(String domain) {
        return getPublicSuffix(domain)
            .flatMap(publicSuffix -> extractRegistrableName(domain, publicSuffix));
    }

    /**
     * Find the subdomain.
     * <p>
     * This method is case insensitive.
     *
     * @param domain to find subdomain for
     * @return subdomain
     */
    public Optional<String> getSubDomain(String domain) {
        return getPublicSuffix(domain)
            .flatMap(publicSuffix -> extractSubDomain(domain, publicSuffix));
    }

    /**
     * Domain name without the subdomain.
     * <p>
     * This method is case insensitive.
     *
     * @param domain to remove subdomain from
     * @return stripped domain
     */
    public Optional<String> stripSubDomain(String domain) {
        return getRegistrableName(domain)
            .map(domain::indexOf)
            .map(domain::substring);
    }

    private Optional<String> extractRegistrableName(String domain, String publicSuffix) {
        List<String> labels = extractNonSuffixLabels(domain, publicSuffix);
        if (labels == null) {
            return Optional.empty();
        }

        String registrableName = labels.get(labels.size() - 1);
        return Optional.of(registrableName);
    }

    private Optional<String> extractSubDomain(String domain, String publicSuffix) {
        List<String> labels = extractNonSuffixLabels(domain, publicSuffix);
        if (labels == null) {
            return Optional.empty();
        }

        List<String> stripLastLabel = labels.subList(0, labels.size() - 1);
        String subDomain = DomainUtils.joinLabels(stripLastLabel);
        return Optional.ofNullable(subDomain);
    }

    private List<String> extractNonSuffixLabels(String domain, String publicSuffix) {
        List<String> domainLabels = DomainUtils.splitLabels(domain);
        List<String> suffixLabels = DomainUtils.splitLabels(publicSuffix);

        if (domainLabels.size() == suffixLabels.size()) {
            return null;
        }

        int registrableNameIndex = domainLabels.size() - suffixLabels.size();
        return domainLabels.subList(0, registrableNameIndex);
    }
}