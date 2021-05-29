Inspired by: https://github.com/whois-server-list/public-suffix-list

How would you extract the registered domain name from these domains? How would you determine what is the subdomain?

- net.hr
- user.net.hr
- news.bbc.co.uk
- commoncrawl.s3.amazonaws.com

`domain-utils` aims to solve these issues. It uses a rule list to determine how domains are constructed.
The recommended rule list to use is [Mozilla's Public Suffix List](https://publicsuffix.org/).

A "public suffix" is one under which Internet users can directly register names. Some examples of public suffixes are `com`, `co.uk` and `pvt.k12.ma.us`.

---

## Using the library

Create a `DomainRegistry` using the `DomainRegistryFactory`. There are several ways how to do this explained in one of the following chapters.

- `DomainRegistry.getPublicSuffix`: extract the public suffix or `null`
- `DomainRegistry.getRegisteredName`: extract the registered domain name (one level under the public suffix) or `null`
- `DomainRegistry.getSubDomain`:  extract the subdomain or `null`
- `DomainRegistry.stripSubDomain`: remove the subdomain if the domain is under a public suffix or `null`

### Examples

Assuming you are using the suggested rule list from Mozilla:

1. alturkovic.blogspot.com
- `DomainRegistry.getPublicSuffix`: blogspot.com
- `DomainRegistry.getRegisteredName`: alturkovic
- `DomainRegistry.getSubDomain`:  `null`
- `DomainRegistry.stripSubDomain`: alturkovic.blogspot.com

2. en.wikipedia.org
- `DomainRegistry.getPublicSuffix`: org
- `DomainRegistry.getRegisteredName`: wikipedia
- `DomainRegistry.getSubDomain`:  en
- `DomainRegistry.stripSubDomain`: wikipedia.org

3. alturkovic.invalid
- `DomainRegistry.getPublicSuffix`: `null`
- `DomainRegistry.getRegisteredName`: `null`
- `DomainRegistry.getSubDomain`:  `null`
- `DomainRegistry.stripSubDomain`: `null`

---

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
    <version>1.0.0</version>
  </dependency>
</dependencies>
```

----

## IDN

You can use the API's methods with UTF-8 domain names or [Punycode](https://en.wikipedia.org/wiki/Punycode) encoded ASCII domain names. The API will return the results in the same format as the input was. I.e. if you use a UTF-8 string the result will be a UTF-8 String as well. Same for Punycode.

---

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

Then instantiate the `DomainRegistryFactory`:

```java
DomainRegistry registry = new DomainRegistryFactory()
    .fromFile("/effective_tld_names.dat")
    .build();
```

### By downloading it manually

```java
DomainRegistry registry = new DomainRegistryFactory()
    .from(new URL("https://publicsuffix.org/list/effective_tld_names.dat").openStream())
    .build();
```

---

## Build your own rules

You can build the `DomainRegistry` using any rules you might need.

```java
DomainRegistry registry = new DomainRegistryFactory()
    .withRule("tld")
    .build();
```

### Use case example

You have one set of rules for all `org` domains except for `wikipedia.org`.

You can create the following `DomainRegistry`:

```java
DomainRegistry registry = new DomainRegistryFactory()
    .withRule("org")
    .withRule("wikipedia.org")
    .build();

System.out.println(registry.getPublicSuffix("any.org")); // org
System.out.println(registry.getPublicSuffix("sub.any.org")); // org
System.out.println(registry.getPublicSuffix("wikipedia.org")); // wikipedia.org
System.out.println(registry.getPublicSuffix("en.wikipedia.org")); // wikipedia.org
```

The configured `DomainRegistry` can be used to see which ruleset should be applied depending on the domain name.