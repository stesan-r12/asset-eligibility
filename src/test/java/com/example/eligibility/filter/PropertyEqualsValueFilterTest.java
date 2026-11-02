package com.example.eligibility.filter;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertyEqualsValueFilterTest {

    private static final Map<String, String> ASSET = Map.of(
            "zone", "Lombardy-Plant-A",
            "isOnline", "true",
            "description", "");

    @Test
    void given_EqualValue_when_Matching_then_Matches() {
        PropertyEqualsValueFilter propertyEqualsValueFilter = new PropertyEqualsValueFilter("zone", "Lombardy-Plant-A");

        boolean result = propertyEqualsValueFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_ValueDifferingInCase_when_Matching_then_Matches() {
        PropertyEqualsValueFilter propertyEqualsValueFilter = new PropertyEqualsValueFilter("zone", "LOMBARDY-plant-a");

        boolean result = propertyEqualsValueFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_DifferentValue_when_Matching_then_DoesNotMatch() {
        PropertyEqualsValueFilter propertyEqualsValueFilter = new PropertyEqualsValueFilter("zone", "Piedmont-Plant-B");

        boolean result = propertyEqualsValueFilter.matches(ASSET);

        assertFalse(result);
    }

    @Test
    void given_PropertyNameDifferingInCase_when_Matching_then_DoesNotMatch() {
        PropertyEqualsValueFilter propertyEqualsValueFilter = new PropertyEqualsValueFilter("Zone", "Lombardy-Plant-A");

        boolean result = propertyEqualsValueFilter.matches(ASSET);

        assertFalse(result);
    }

    @Test
    void given_MissingProperty_when_Matching_then_DoesNotMatch() {
        PropertyEqualsValueFilter propertyEqualsValueFilter = new PropertyEqualsValueFilter("firmwareVersion", "2.4.1");

        boolean result = propertyEqualsValueFilter.matches(ASSET);

        assertFalse(result);
    }

    @Test
    void given_NullValue_when_Matching_then_DoesNotMatch() {
        PropertyEqualsValueFilter propertyEqualsValueFilter = new PropertyEqualsValueFilter("zone", "Lombardy-Plant-A");
        Map<String, String> asset = new HashMap<>();
        asset.put("zone", null);

        boolean result = propertyEqualsValueFilter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_EmptyExpectedValueAndEmptyValue_when_Matching_then_Matches() {
        PropertyEqualsValueFilter propertyEqualsValueFilter = new PropertyEqualsValueFilter("description", "");

        boolean result = propertyEqualsValueFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_NullPropertyName_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyEqualsValueFilter(null, "x"));
    }

    @Test
    void given_NullExpectedValue_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyEqualsValueFilter("zone", null));
    }

    @Test
    void given_Filter_when_ToString_then_ShowsQuotedValue() {
        PropertyEqualsValueFilter propertyEqualsValueFilter = new PropertyEqualsValueFilter("zone", "Lombardy-Plant-A");

        String result = propertyEqualsValueFilter.toString();

        assertEquals("zone = 'Lombardy-Plant-A'", result);
    }
}
