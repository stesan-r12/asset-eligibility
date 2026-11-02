package com.example.eligibility.filter;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertyExistsFilterTest {

    private static final Map<String, String> ASSET = Map.of(
            "zone", "Lombardy-Plant-A",
            "batteryLevel", "85",
            "description", "");

    @Test
    void given_PresentProperty_when_Matching_then_Matches() {
        PropertyExistsFilter propertyExistsFilter = new PropertyExistsFilter("zone");

        boolean result = propertyExistsFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_MissingProperty_when_Matching_then_DoesNotMatch() {
        PropertyExistsFilter propertyExistsFilter = new PropertyExistsFilter("firmwareVersion");

        boolean result = propertyExistsFilter.matches(ASSET);

        assertFalse(result);
    }

    @Test
    void given_PropertyNameDifferingInCase_when_Matching_then_DoesNotMatch() {
        PropertyExistsFilter propertyExistsFilter = new PropertyExistsFilter("Zone");

        boolean result = propertyExistsFilter.matches(ASSET);

        assertFalse(result);
    }

    @Test
    void given_EmptyValue_when_Matching_then_Matches() {
        PropertyExistsFilter propertyExistsFilter = new PropertyExistsFilter("description");

        boolean result = propertyExistsFilter.matches(ASSET);

        assertTrue(result);
    }

    @Test
    void given_NullValue_when_Matching_then_DoesNotMatch() {
        PropertyExistsFilter propertyExistsFilter = new PropertyExistsFilter("notes");
        Map<String, String> asset = new HashMap<>();
        asset.put("notes", null);

        boolean result = propertyExistsFilter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_NullPropertyName_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyExistsFilter(null));
    }

    @Test
    void given_Filter_when_ToString_then_ShowsExists() {
        PropertyExistsFilter propertyExistsFilter = new PropertyExistsFilter("zone");

        String result = propertyExistsFilter.toString();

        assertEquals("EXISTS(zone)", result);
    }
}
