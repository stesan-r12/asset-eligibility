package com.example.eligibility.filter;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlwaysTrueFilterTest {

    private final AssetFilter filter = new AlwaysTrueFilter();

    @Test
    void given_EmptyAsset_when_Matching_then_Matches() {
        Map<String, String> asset = Map.of();

        boolean result = filter.matches(asset);

        assertTrue(result);
    }

    @Test
    void given_NonEmptyAsset_when_Matching_then_Matches() {
        Map<String, String> asset = Map.of("zone", "Lombardy-Plant-A", "isOnline", "false");

        boolean result = filter.matches(asset);

        assertTrue(result);
    }

    @Test
    void given_NullAsset_when_Matching_then_Throws() {
        assertThrows(NullPointerException.class, () -> filter.matches(null));
    }

    @Test
    void given_Filter_when_ToString_then_ReturnsTrue() {
        String result = filter.toString();

        assertEquals("TRUE", result);
    }
}
