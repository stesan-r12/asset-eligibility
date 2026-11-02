package com.example.eligibility.filter;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlwaysFalseFilterTest {

    private final AssetFilter filter = new AlwaysFalseFilter();

    @Test
    void given_EmptyAsset_when_Matching_then_DoesNotMatch() {
        Map<String, String> asset = Map.of();

        boolean result = filter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_NonEmptyAsset_when_Matching_then_DoesNotMatch() {
        Map<String, String> asset = Map.of("zone", "Lombardy-Plant-A", "isOnline", "true");

        boolean result = filter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_NullAsset_when_Matching_then_Throws() {
        assertThrows(NullPointerException.class, () -> filter.matches(null));
    }

    @Test
    void given_Filter_when_ToString_then_ReturnsFalse() {
        String result = filter.toString();

        assertEquals("FALSE", result);
    }
}
