# BBSE — Backward Binary Search Encoding (Java)

[![Maven Central](https://img.shields.io/maven-central/v/com.ohusiev/bbse)](https://central.sonatype.com/artifact/com.ohusiev/bbse)

**Compact, deterministic, and prefix-free encoding for sorted integer domains.**  
Port of my previous [Rust implementation](https://crates.io/crates/bbse) with identical behavior and test coverage.

> 📦 Published to Maven Central · ✅ Reversible · 🎯 Midpoint configurable

---

## Overview

**BBSE (Backward Binary Search Encoding)** encodes values from a sorted integer range `[start, end)` using binary decision paths — the same path taken by a binary search to locate the target.

Unlike fixed-length or entropy-based encoding schemes, BBSE provides:
- ✅ **Prefix-freedom** — no encoded value is a prefix of another.
- ✅ **Determinism** — no randomness, no state.
- ✅ **Reversibility** — fully decodable to the original value.
- ✅ **Midpoint control** — bias the first decision toward a custom value.

It’s designed for real-world use in indexing, image compression, embedded systems, stateless encodings, and bandwidth-constrained protocols.

---

## Installation

**Maven:**

```xml
<dependency>
  <groupId>com.ohusiev</groupId>
  <artifactId>bbse</artifactId>
  <version>0.1.2</version>
</dependency>
````

---

## Usage

```java
import com.ohusiev.bbse.BBSEncoder;

List<Boolean> bits = BBSEncoder.encode(0, 16, 5);
int value = BBSEncoder.decode(0, 16, bits);
System.out.println(value); // -> 5
```

Use a custom midpoint to optimize for skewed distributions:

```java
List<Boolean> path = BBSEncoder.encodeFrom(0, 16, 5, 8);
int decoded = BBSEncoder.decode(0, 16, path);
```

---

## Features

* ✅ Prefix-free encoding of bounded integers
* ✅ Reversible with `encode(...)` / `decode(...)`
* ✅ Custom midpoint support with `encodeFrom(...)`
* ✅ Stateless and test-covered
* ✅ Identical to the [Rust version](https://crates.io/crates/bbse) (bit-for-bit)
* ✅ No external dependencies

---

## Build & Test

Standard Maven workflow:

```bash
mvn clean verify
```

Includes tests and PGP-signed JARs with sources and Javadoc.

---

## Project Layout

```
bbse-java/
├── src/
│   ├── main/java/com/ohusiev/bbse/BBSEncoder.java
│   └── test/java/com/ohusiev/bbse/BBSEncoderTest.java
├── pom.xml
├── LICENSE
└── README.md
```

---

## License

[MIT](LICENSE)

