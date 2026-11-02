package com.example.eligibility.filter;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrFilterTest {

    private static final Map<String, String> ASSET = Map.of();
    private static final AssetFilter TRUE = new AlwaysTrueFilter();
    private static final AssetFilter FALSE = new AlwaysFalseFilter();

    @Test
    void given_OneOperandMatching_when_Matching_then_Matches() {
        OrFilter orFilter = new OrFilter(List.of(FALSE, TRUE, FALSE));

        boolean result = orFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_NoOperandMatching_when_Matching_then_DoesNotMatch() {
        OrFilter orFilter = new OrFilter(List.of(FALSE, FALSE));

        boolean result = orFilter.matches(ASSET);

        assertFalse(result);
    }

    @Test
    void given_NoOperands_when_Matching_then_DoesNotMatch() {
        OrFilter orFilter = new OrFilter(List.of());

        boolean result = orFilter.matches(ASSET);

        assertFalse(result);
    }

    @Test
    void given_MissingPropertyInOneBranch_when_OtherBranchMatches_then_Matches() {
        OrFilter orFilter = new OrFilter(List.of(
                new PropertyEqualsValueFilter("batteryLevel", "100"),
                new PropertyEqualsValueFilter("isOnline", "true")));
        Map<String, String> asset = Map.of("isOnline", "true");

        boolean result = orFilter.matches(asset);

        assertTrue(result);
    }

    @Test
    void given_NullOperandList_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new OrFilter(null));
    }

    @Test
    void given_NullOperandInList_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new OrFilter(Arrays.asList(TRUE, null)));
    }

    @Test
    void given_TwoOperands_when_ToString_then_JoinsWithOr() {
        OrFilter orFilter = new OrFilter(List.of(
                new PropertyEqualsValueFilter("zone", "X"),
                new PropertyEqualsValueFilter("zone", "Y")));

        String result = orFilter.toString();

        assertEquals("(zone = 'X' OR zone = 'Y')", result);
    }
}
