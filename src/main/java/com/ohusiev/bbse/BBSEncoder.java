package com.ohusiev.bbse;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>BBSE — Backward Binary Search Encoding</h2>
 *
 * <p>A compact, reversible, and prefix-free encoding scheme for values in a sorted integer range [start, end).
 * This scheme encodes the binary decision path taken during a binary search to locate the target value.
 *
 * <p><strong>Key properties:</strong>
 * <ul>
 *     <li><strong>Prefix-free</strong>: no encoded path is a prefix of another</li>
 *     <li><strong>Deterministic</strong>: depends only on range and value</li>
 *     <li><strong>Reversible</strong>: fully decodable to the original value</li>
 *     <li><strong>Midpoint-biasable</strong>: supports custom midpoint for skewed distributions</li>
 * </ul>
 *
 * <p><strong>Use cases:</strong>
 * <ul>
 *     <li>Compact encoding of bounded integers</li>
 *     <li>Compression, entropy coding, and indexing</li>
 *     <li>Stateless encoding of sorted-domain values</li>
 * </ul>
 *
 * <p>This class is static-only and cannot be instantiated.
 *
 * <p>See {@link #encode}, {@link #encodeFrom}, and {@link #decode}.
 */
public class BBSEncoder {

    private BBSEncoder() {
        // Utility class, do not instantiate
    }

    /**
     * Encodes a value within the given range using the default midpoint.
     * The midpoint is calculated as {@code (start + end) / 2}.
     *
     * <p>If the range contains only one value (i.e. {@code end - start == 1}),
     * the returned path will be empty.
     *
     * @param start  inclusive start of the range
     * @param end    exclusive end of the range
     * @param target the value to encode; must lie in [start, end)
     * @return list of boolean decisions (left = false, right = true)
     * @throws IllegalArgumentException if the range is invalid or target is out of bounds
     */
    public static List<Boolean> encode(int start, int end, int target) {
        if (end - start == 1 && target == start) {
            return new ArrayList<>();
        }

        int midpoint = (start + end) / 2;
        return encodeFrom(start, end, target, midpoint);
    }

    /**
     * Encodes a value within the given range using a custom initial midpoint.
     * Useful for skewed or non-uniform distributions.
     *
     * @param start    inclusive start of the range
     * @param end      exclusive end of the range
     * @param target   the value to encode; must lie in [start, end)
     * @param midpoint custom midpoint to use as the first decision
     * @return list of boolean decisions representing the search path
     * @throws IllegalArgumentException if inputs are invalid or midpoint lies outside (start, end)
     */
    public static List<Boolean> encodeFrom(int start, int end, int target, int midpoint) {
        if (start >= end) {
            throw new IllegalArgumentException("Invalid range: start >= end");
        }
        if (target < start || target >= end) {
            throw new IllegalArgumentException("Target out of range");
        }
        if (midpoint <= start || midpoint >= end) {
            throw new IllegalArgumentException("Midpoint must lie strictly inside the range");
        }

        List<Boolean> path = new ArrayList<>();
        int left = start;
        int right = end;
        int mid = midpoint;

        while (right - left > 1) {
            if (target < mid) {
                path.add(false);
                right = mid;
            } else {
                path.add(true);
                left = mid;
            }

            if (right - left == 1) {
                break;
            }

            mid = (left + right) / 2;
        }

        return path;
    }

    /**
     * Decodes a binary search path back into the original target value,
     * given the initial [start, end) range.
     *
     * @param start inclusive start of the original range
     * @param end   exclusive end of the original range
     * @param path  list of binary decisions taken during encoding
     * @return the reconstructed target value
     * @throws IllegalArgumentException if the path does not uniquely narrow the range to a single value
     */
    public static int decode(int start, int end, List<Boolean> path) {
        for (boolean bit : path) {
            int mid = (start + end) / 2;
            if (bit) {
                start = mid;
            } else {
                end = mid;
            }
        }

        if (end - start != 1) {
            throw new IllegalArgumentException("Incomplete or invalid path");
        }

        return start;
    }
}
