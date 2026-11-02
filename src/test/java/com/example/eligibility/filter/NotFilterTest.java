package com.example.eligibility.filter;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotFilterTest {

    private static final Map<String, String> ASSET = Map.of("zone", "Lombardy-Plant-A");

    @Test
    void given_MatchingOperand_when_Matching_then_DoesNotMatch() {
        NotFilter notFilter = new NotFilter(new AlwaysTrueFilter());

        boolean result = notFilter.matches(ASSET);

        assertFalse(result);
    }

    @Test
    void given_NonMatchingOperand_when_Matching_then_Matches() {
        NotFilter notFilter = new NotFilter(new AlwaysFalseFilter());

        boolean result = notFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_DoubleNegation_when_Matching_then_ReturnsOriginalResult() {
        NotFilter notFilter = new NotFilter(new NotFilter(new PropertyEqualsValueFilter("zone", "Lombardy-Plant-A")));

        boolean result = notFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_MissingProperty_when_NegatedEquality_then_Matches() {
        NotFilter notFilter = new NotFilter(new PropertyEqualsValueFilter("zone", "Lombardy-Plant-A"));
        Map<String, String> asset = Map.of();

        boolean result = notFilter.matches(asset);

        assertTrue(result);
    }

    @Test
    void given_NullOperand_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new NotFilter(null));
    }

    @Test
    void given_Operand_when_ToString_then_WrapsWithNot() {
        NotFilter notFilter = new NotFilter(new PropertyEqualsValueFilter("zone", "X"));

        String result = notFilter.toString();

        assertEquals("NOT (zone = 'X')", result);
    }
}
