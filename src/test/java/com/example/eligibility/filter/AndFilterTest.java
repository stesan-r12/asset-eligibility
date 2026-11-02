package com.example.eligibility.filter;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AndFilterTest {

    private static final Map<String, String> ASSET = Map.of();
    private static final AssetFilter TRUE = new AlwaysTrueFilter();
    private static final AssetFilter FALSE = new AlwaysFalseFilter();

    @Test
    void given_AllOperandsMatching_when_Matching_then_Matches() {
        AndFilter andFilter = new AndFilter(List.of(TRUE, TRUE, TRUE));

        boolean result = andFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_OneOperandNotMatching_when_Matching_then_DoesNotMatch() {
        AndFilter andFilter = new AndFilter(List.of(TRUE, FALSE, TRUE));

        boolean result = andFilter.matches(ASSET);

        assertFalse(result);
    }

    @Test
    void given_NoOperands_when_Matching_then_Matches() {
        AndFilter andFilter = new AndFilter(List.of());

        boolean result = andFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_SourceListModifiedAfterCreation_when_Matching_then_FilterIsUnchanged() {
        List<AssetFilter> source = new ArrayList<>(List.of(TRUE));
        AndFilter andFilter = new AndFilter(source);
        source.add(FALSE);

        boolean result = andFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_NullOperandList_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new AndFilter(null));
    }

    @Test
    void given_NullOperandInList_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new AndFilter(Arrays.asList(TRUE, null)));
    }

    @Test
    void given_TwoOperands_when_ToString_then_JoinsWithAnd() {
        AndFilter andFilter = new AndFilter(List.of(
                new PropertyEqualsValueFilter("zone", "X"),
                new PropertyExistsFilter("batteryLevel")));

        String result = andFilter.toString();

        assertEquals("(zone = 'X' AND EXISTS(batteryLevel))", result);
    }

    @Test
    void given_NoOperands_when_ToString_then_ReturnsEmptyParentheses() {
        AndFilter andFilter = new AndFilter(List.of());

        String result = andFilter.toString();

        assertEquals("()", result);
    }
}
