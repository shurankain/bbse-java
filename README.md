# BBSE — Backward Binary Search Encoding (Java)

Minimal and reversible encoding scheme for sorted integer ranges, based on binary search paths.  
Ported from [Rust version](https://crates.io/crates/bbse) with identical behavior and design goals.

> 📦 Maven-ready, deterministic, prefix-free, mid-point configurable.

---

## Purpose

This project aims to **learn, understand, and demonstrate algorithmic concepts**
by building a reusable Java library for binary decision path encoding.  
Originally developed in Rust, this version is API-compatible and test-aligned.

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

## Maven

```xml
<dependency>
  <groupId>com.ohusiev</groupId>
  <artifactId>bbse</artifactId>
  <version>0.1.0</version>
</dependency>
````

Latest version: [Maven Central › com.ohusiev\:bbse](https://central.sonatype.org)

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

## License

[MIT](LICENSE)
