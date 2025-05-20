package com.ohusiev.bbse;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BBSEncoderTest {

    @Test
    public void testEncodeDecodeVariousRanges() {
        int[] sizes = {2, 3, 5, 8, 16, 257};
        for (int size : sizes) {
            for (int value = 0; value < size; value++) {
                List<Boolean> bits = BBSEncoder.encode(0, size, value);
                int decoded = BBSEncoder.decode(0, size, bits);
                assertEquals(value, decoded, "Failed at value " + value + " in range 0.." + size);
            }
        }
    }

    @Test
    public void testEncodeDecodeSmallRanges() {
        for (int size = 2; size <= 20; size++) {
            for (int value = 0; value < size; value++) {
                List<Boolean> bits = BBSEncoder.encode(0, size, value);
                int decoded = BBSEncoder.decode(0, size, bits);
                assertEquals(value, decoded, "Failed at value " + value + " in range size " + size);
            }
        }
    }

    @Test
    public void testPowerOfTwoPathLengths() {
        for (int k = 1; k <= 16; k++) {
            int size = 1 << k;
            int[] testValues = {0, size / 4, size / 2, size - 1};
            for (int value : testValues) {
                List<Boolean> bits = BBSEncoder.encode(0, size, value);
                assertEquals(k, bits.size(), "Expected " + k + " bits for size " + size);
                int decoded = BBSEncoder.decode(0, size, bits);
                assertEquals(value, decoded);
            }
        }
    }

    @Test
    public void testNonPowerOfTwoRanges() {
        int[] sizes = {3, 5, 6, 10, 17, 31, 100, 257};
        for (int size : sizes) {
            int[] testValues = {0, size / 2, size - 1};
            for (int value : testValues) {
                List<Boolean> bits = BBSEncoder.encode(0, size, value);
                int decoded = BBSEncoder.decode(0, size, bits);
                assertEquals(value, decoded);
            }
        }
    }

    @Test
    public void testSingleElementRange() {
        List<Boolean> bits = BBSEncoder.encode(42, 43, 42);
        assertTrue(bits.isEmpty(), "Single-element range should produce no bits");
        int decoded = BBSEncoder.decode(42, 43, bits);
        assertEquals(42, decoded);
    }

    @Test
    public void testOutOfBoundsTargetAbove() {
        assertThrows(IllegalArgumentException.class, () -> BBSEncoder.encode(10, 20, 25));
    }

    @Test
    public void testOutOfBoundsTargetBelow() {
        assertThrows(IllegalArgumentException.class, () -> BBSEncoder.encode(10, 20, 9));
    }

    @Test
    public void testOutOfBoundsTargetEqualToEnd() {
        assertThrows(IllegalArgumentException.class, () -> BBSEncoder.encode(10, 20, 20));
    }

    @Test
    public void testInvalidDecodePath() {
        List<Boolean> bits = List.of(true);
        assertThrows(IllegalArgumentException.class, () -> BBSEncoder.decode(0, 3, bits));
    }

    @Test
    public void testPathLengthMonotonicity() {
        for (int size = 2; size <= 100; size++) {
            for (int value = 0; value < size; value++) {
                List<Boolean> bits = BBSEncoder.encode(0, size, value);
                assertTrue(bits.size() <= 64, "Path too long: " + bits.size() + " bits");
            }
        }
    }

    @Test
    public void testLargeRangeEdgeValues() {
        int size = 1_000_001;
        List<Boolean> bitsStart = BBSEncoder.encode(0, size, 0);
        List<Boolean> bitsEnd = BBSEncoder.encode(0, size, size - 1);
        assertEquals(0, BBSEncoder.decode(0, size, bitsStart));
        assertEquals(size - 1, BBSEncoder.decode(0, size, bitsEnd));
    }

    @Test
    public void testEncodeFromCustomMidpoint() {
        int start = 0;
        int end = 16;
        int midpoint = (start + end) / 2;
        for (int value = start; value < end; value++) {
            List<Boolean> bits = BBSEncoder.encodeFrom(start, end, value, midpoint);
            int decoded = BBSEncoder.decode(start, end, bits);
            assertEquals(value, decoded, "Failed at value " + value);
        }
    }

    @Test
    public void testMidpointOutOfBoundsStart() {
        assertThrows(IllegalArgumentException.class, () -> BBSEncoder.encodeFrom(0, 10, 5, 0));
    }

    @Test
    public void testMidpointOutOfBoundsEnd() {
        assertThrows(IllegalArgumentException.class, () -> BBSEncoder.encodeFrom(0, 10, 5, 10));
    }

    @Test
    public void testEncodeFromTargetOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> BBSEncoder.encodeFrom(0, 10, 10, 5));
    }
}
