# BBSE — Backward Binary Search Encoding (Java)

Minimal and reversible encoding scheme for sorted integer ranges, based on binary search paths.  
Ported from [Rust version](https://crates.io/crates/bbse) with identical behavior and design goals.

> 📦 Maven-ready, deterministic, prefix-free, mid-point configurable.

---

## Purpose

BBSE (Backward Binary Search Encoding) provides a lightweight and deterministic method for encoding integers in known sorted ranges using binary decision paths.  
It is designed for real-world use in indexing, image compression, embedded systems, and anywhere prefix-free representations are beneficial.

This Java version mirrors the original Rust implementation and includes full test coverage.

---

## Structure

Standard Gradle layout:

```
bbse-java/
├── build.gradle.kts
├── settings.gradle.kts
├── README.md
└── src/
    ├── main/java/com/ohusiev/bbse/BBSEncoder.java
    └── test/java/com/ohusiev/bbse/BBSEncoderTest.java
````

---

## Features

- ✅ Prefix-free encoding for discrete domains
- ✅ Reversible `encode(...)` / `decode(...)`
- ✅ Custom midpoint control (`encodeFrom(...)`)
- ✅ Stateless, test-covered, allocation-aware
- ✅ Binary compatible with the original [Rust crate](https://crates.io/crates/bbse)

---

## Installation

```xml
<dependency>
  <groupId>com.ohusiev</groupId>
  <artifactId>bbse</artifactId>
  <version>0.1.0</version>
</dependency>
````

Latest version: [![Maven Central](https://img.shields.io/maven-central/v/com.ohusiev/bbse)](https://central.sonatype.com/artifact/com.ohusiev/bbse)

---

## Example

```java
List<Boolean> bits = BBSEncoder.encode(0, 16, 5);
int decoded = BBSEncoder.decode(0, 16, bits);
System.out.println(decoded); // -> 5
```

```java
List<Boolean> skewed = BBSEncoder.encodeFrom(0, 16, 5, 8);
int value = BBSEncoder.decode(0, 16, skewed);
```

---

## Run tests

```bash
./gradlew test
```

---
## Why BBSE?

Traditional integer encodings often rely on fixed-length or entropy-based schemes.
BBSE offers a middle ground: compact, prefix-free representation without external dictionaries or overhead — ideal for use cases with sorted or bounded domains.

---

## License

[MIT](LICENSE)
