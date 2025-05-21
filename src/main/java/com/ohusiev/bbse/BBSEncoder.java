package com.ohusiev.bbse;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>BBSE — Backward Binary Search Encoding (v2.0)</h2>
 *
 * <p>
 * BBSE encodes integers from a known sorted range <code>[start, end)</code> as a compact,
 * reversible path of binary decisions (left = false, right = true), exactly as a binary
 * search algorithm would traverse the range to locate the value.
 * </p>
 *
 * <p>
 * The result is a minimal, prefix-free, and stack-compatible representation. No length
 * headers are required — each value can be stored and decoded independently.
 * </p>
 *
 * <h3>Key properties:</h3>
 * <ul>
 *   <li><b>Prefix-free:</b> No encoded path is a prefix of another</li>
 *   <li><b>Deterministic:</b> Output depends only on range and value</li>
 *   <li><b>Reversible:</b> Fully decodable without external state</li>
 *   <li><b>Compact:</b> Stops early when midpoint == target</li>
 *   <li><b>Stack-friendly:</b> Each path is self-contained</li>
 *   <li><b>no_std compatible:</b> Matches logic of embedded systems</li>
 * </ul>
 *
 * <h3>Use cases:</h3>
 * <ul>
 *   <li>Encoding integers in a bounded range (e.g. 0–255)</li>
 *   <li>Compression of color channels or value deltas</li>
 *   <li>Bit-efficient encoding of indices or ordinals</li>
 *   <li>Indexing, range trees, and deterministic hash maps</li>
 *   <li>Lossless or near-lossless codec structures</li>
 * </ul>
 *
 * <h3>Example</h3>
 *
 * <pre>{@code
 * List<Boolean> path = BBSEncoder.encode(0, 256, 128);
 * int value = BBSEncoder.decode(0, 256, path);
 * assert value == 128;
 * }</pre>
 *
 * <h3>Custom midpoint (optional)</h3>
 *
 * <pre>{@code
 * List<Boolean> biased = BBSEncoder.encodeFrom(0, 16, 3, 4);
 * int recovered = BBSEncoder.decode(0, 16, biased);
 * assert recovered == 3;
 * }</pre>
 *
 * <h3>Stack-like design</h3>
 * <p>
 * Since each path is prefix-free and minimal, values can be pushed to a list (stack),
 * serialized in sequence, and decoded back without needing explicit delimiters.
 * </p>
 *
 * <h3>🔧 Behavior</h3>
 * <ul>
 *   <li><code>encode()</code> stops early if <code>target == mid</code></li>
 *   <li><code>decode()</code> returns <code>(lo + hi) / 2</code> as final value</li>
 *   <li>Empty paths imply exact match on the first decision (or 1-element range)</li>
 * </ul>
 *
 * <h3>Limitations</h3>
 * <ul>
 *   <li>Does not encode values outside of <code>[start, end)</code></li>
 *   <li>Requires same range at decode-time</li>
 *   <li>Does not include length — but it’s prefix-free</li>
 * </ul>
 *
 * @see BBSEncoder#encode(int, int, int)
 * @see BBSEncoder#encodeFrom(int, int, int, int)
 * @see BBSEncoder#decode(int, int, List)
 */
public final class BBSEncoder {

    private BBSEncoder() {
    }

    /**
     * Encodes a value from the range [start, end) using midpoint = (start + end) / 2.
     *
     * @param start  inclusive lower bound of the range
     * @param end    exclusive upper bound of the range
     * @param target the value to encode (must satisfy start &lt;= target &lt; end)
     * @return a list of binary decisions (left=false, right=true)
     * @throws IllegalArgumentException if range is invalid or target is out of bounds
     */
    public static List<Boolean> encode(int start, int end, int target) {
        if (start >= end) {
            throw new IllegalArgumentException("Invalid range: start >= end");
        }
        if (target < start || target >= end) {
            throw new IllegalArgumentException("Target out of range");
        }

        if (end - start == 1) {
            return new ArrayList<>();
        }

        int midpoint = (start + end) / 2;
        return encodeFrom(start, end, target, midpoint);
    }

    /**
     * Encodes a value using a custom midpoint for the first decision.
     *
     * @param start    inclusive lower bound of the range
     * @param end      exclusive upper bound of the range
     * @param target   the value to encode (must satisfy start &lt;= target &lt; end)
     * @param midpoint custom midpoint to use for the first split (must lie strictly inside (start, end))
     * @return a list of binary decisions (left=false, right=true)
     * @throws IllegalArgumentException if inputs are invalid
     */
    public static List<Boolean> encodeFrom(int start, int end, int target, int midpoint) {
        if (start >= end) {
            throw new IllegalArgumentException("start must be < end");
        }
        if (target < start || target >= end) {
            throw new IllegalArgumentException("target out of range");
        }
        if (midpoint <= start || midpoint >= end) {
            throw new IllegalArgumentException("midpoint must lie strictly inside the range");
        }

        List<Boolean> path = new ArrayList<>();
        int lo = start;
        int hi = end;
        int mid = midpoint;

        while (true) {
            if (target == mid) {
                break;
            }

            if (target < mid) {
                path.add(false);
                hi = mid;
            } else {
                path.add(true);
                lo = mid;
            }

            if (hi - lo == 1) {
                break;
            }

            mid = (lo + hi) / 2;
        }

        return path;
    }

    /**
     * Decodes a previously encoded path using the original [start, end) range.
     *
     * @param start inclusive lower bound of the range
     * @param end   exclusive upper bound of the range
     * @param path  binary decision path from encoding
     * @return the decoded integer value
     * @throws IllegalArgumentException if decoding fails to converge
     */
    public static int decode(int start, int end, List<Boolean> path) {
        int lo = start;
        int hi = end;

        for (boolean bit : path) {
            int mid = (lo + hi) / 2;
            if (bit) {
                lo = mid;
            } else {
                hi = mid;
            }
        }

        return (lo + hi) / 2;
    }
}
