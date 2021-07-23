![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=for-the-badge&labelColor=ED8B00&logo=java&color=808080) ![JitPack](https://img.shields.io/jitpack/v/github/alturkovic/domain-utils?style=for-the-badge&logo=Git&labelColor=007ec5&color=808080&logoColor=white) ![License](https://img.shields.io/github/license/alturkovic/domain-utils?style=for-the-badge&color=808080&logo=Open%20Source%20Initiative&logoColor=white)

Inspired by: https://github.com/whois-server-list/public-suffix-list

How would you extract the registered domain name from these domains? How would you determine what is the subdomain?

- net.hr
- user.net.hr
- news.bbc.co.uk
- commoncrawl.s3.amazonaws.com

`domain-utils` aims to solve these issues. It uses a rule list to determine how domains are constructed.
The recommended rule list to use is [Mozilla's Public Suffix List](https://publicsuffix.org/).

A "public suffix" is one under which Internet users can directly register names. Some examples of public suffixes are `com`, `co.uk` and `pvt.k12.ma.us`.

## Using the library

Create a `DomainRegistry` using the `DomainRegistryBuilder`. There are several ways how to do this explained in one of the following chapters.

API methods all return `Optional` results:

- `DomainRegistry.getPublicSuffix`: extract the public suffix
- `DomainRegistry.getRegistrableName`: extract the registrable domain name (one level under the public suffix)
- `DomainRegistry.getSubDomain`:  extract the subdomain
- `DomainRegistry.stripSubDomain`: remove the subdomain if the domain is under a public suffix

### Examples

Assuming you are using the suggested rule list from Mozilla:

1. alturkovic.blogspot.com
- `DomainRegistry.getPublicSuffix`: blogspot.com
- `DomainRegistry.getRegistrableName`: alturkovic
- `DomainRegistry.getSubDomain`:  `Optional.empty`
- `DomainRegistry.stripSubDomain`: alturkovic.blogspot.com

2. en.wikipedia.org
- `DomainRegistry.getPublicSuffix`: org
- `DomainRegistry.getRegistrableName`: wikipedia
- `DomainRegistry.getSubDomain`:  en
- `DomainRegistry.stripSubDomain`: wikipedia.org

3. alturkovic.invalid
- `DomainRegistry.getPublicSuffix`: `Optional.empty`
- `DomainRegistry.getRegistrableName`: `Optional.empty`
- `DomainRegistry.getSubDomain`:  `Optional.empty`
- `DomainRegistry.stripSubDomain`: `Optional.empty`

### What about www?

The domain name must exclude `www.`. This library will treat it as a subdomain otherwise.

Check out my URL handling project [here](https://github.com/alturkovic/url-utils) to help you with that.

## Importing into your project using Maven

Add the JitPack repository into your `pom.xml`.

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Add the following under your `<dependencies>`:

```xml
<dependencies>
  <dependency>
    <groupId>com.github.alturkovic</groupId>
    <artifactId>domain-utils</artifactId>
    <version>1.1.0</version>
  </dependency>
</dependencies>
```

## IDN

You can use the API's methods with UTF-8 domain names or [Punycode](https://en.wikipedia.org/wiki/Punycode) encoded ASCII domain names. The API will return the results in the same format as the input was. I.e. if you use a UTF-8 string the result will be a UTF-8 String as well. Same for Punycode.

## Keep Public Suffix List up to date

### Using Maven build process

You can integrate the download of the latest list in your maven build process:

```xml
    <plugin>
        <groupId>com.googlecode.maven-download-plugin</groupId>
        <artifactId>download-maven-plugin</artifactId>
        <version>1.6.3</version>
        <executions>
            <execution>
                <phase>generate-resources</phase>
                <goals>
                    <goal>wget</goal>
                </goals>
                <configuration>
                    <url>https://publicsuffix.org/list/effective_tld_names.dat</url>
                    <outputDirectory>${project.build.outputDirectory}</outputDirectory>
                </configuration>
            </execution>
        </executions>
    </plugin>
```

Then instantiate the `DomainRegistryBuilder`:

```java
DomainRegistry registry = new DomainRegistryBuilder()
    .fromFile("/effective_tld_names.dat")
    .build();
```

### By downloading it manually

```java
DomainRegistry registry = new DomainRegistryBuilder()
    .from(new URL("https://publicsuffix.org/list/effective_tld_names.dat").openStream())
    .build();
```

## By using the API call

This is the same as downloading it manually over HTTP.

```java
DomainRegistry registry = new DomainRegistryBuilder()
    .withDefaultRules()
    .build();
```

## Build your own rules

You can build the `DomainRegistry` using any rules you might need.

```java
DomainRegistry registry = new DomainRegistryBuilder()
    .withRule("tld")
    .build();
```

### Use case example

You have one set of rules for all `org` domains except for `wikipedia.org`.

You can create the following `DomainRegistry`:

```java
DomainRegistry registry = new DomainRegistryBuilder()
    .withRule("org")
    .withRule("wikipedia.org")
    .build();

registry.getPublicSuffix("any.org"); // org
registry.getPublicSuffix("sub.any.org"); // org
registry.getPublicSuffix("wikipedia.org"); // wikipedia.org
registry.getPublicSuffix("en.wikipedia.org"); // wikipedia.org
```

The configured `DomainRegistry` can be used to see which ruleset should be applied depending on the domain name.
