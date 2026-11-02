package com.example.eligibility.filter;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PropertyEqualsPropertyFilterTest {

    @Test
    void given_EqualValues_when_Matching_then_Matches() {
        PropertyEqualsPropertyFilter propertyEqualsPropertyFilter = new PropertyEqualsPropertyFilter("zone1", "zone2");
        Map<String, String> asset = Map.of(
                "zone1", "Lombardy-Plant-A",
                "zone2", "Lombardy-Plant-A");

        boolean result = propertyEqualsPropertyFilter.matches(asset);

        assertTrue(result);
    }

    @Test
    void given_ValuesDifferingInCase_when_Matching_then_Matches() {
        PropertyEqualsPropertyFilter propertyEqualsPropertyFilter = new PropertyEqualsPropertyFilter("zone1", "zone2");
        Map<String, String> asset = Map.of(
                "zone1", "LOMBARDY-PLANT-A",
                "zone2", "lombardy-plant-a");

        boolean result = propertyEqualsPropertyFilter.matches(asset);

        assertTrue(result);
    }

    @Test
    void given_DifferentValues_when_Matching_then_DoesNotMatch() {
        PropertyEqualsPropertyFilter propertyEqualsPropertyFilter = new PropertyEqualsPropertyFilter("zone1", "zone2");
        Map<String, String> asset = Map.of(
                "zone1", "Lombardy-Plant-A",
                "zone2", "Piedmont-Plant-B");

        boolean result = propertyEqualsPropertyFilter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_NumericallyEqualTexts_when_Matching_then_DoesNotMatch() {
        PropertyEqualsPropertyFilter propertyEqualsPropertyFilter = new PropertyEqualsPropertyFilter("a", "b");
        Map<String, String> asset = Map.of("a", "20", "b", "20.0");

        boolean result = propertyEqualsPropertyFilter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_LeftPropertyMissing_when_Matching_then_DoesNotMatch() {
        PropertyEqualsPropertyFilter propertyEqualsPropertyFilter = new PropertyEqualsPropertyFilter("zone1", "zone2");
        Map<String, String> asset = Map.of("zone2", "Lombardy-Plant-A");

        boolean result = propertyEqualsPropertyFilter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_RightPropertyMissing_when_Matching_then_DoesNotMatch() {
        PropertyEqualsPropertyFilter propertyEqualsPropertyFilter = new PropertyEqualsPropertyFilter("zone1", "zone2");
        Map<String, String> asset = Map.of("zone1", "Lombardy-Plant-A");

        boolean result = propertyEqualsPropertyFilter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_BothPropertiesMissing_when_Matching_then_DoesNotMatch() {
        PropertyEqualsPropertyFilter propertyEqualsPropertyFilter = new PropertyEqualsPropertyFilter("zone1", "zone2");
        Map<String, String> asset = Map.of();

        boolean result = propertyEqualsPropertyFilter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_BothValuesNull_when_Matching_then_DoesNotMatch() {
        PropertyEqualsPropertyFilter propertyEqualsPropertyFilter = new PropertyEqualsPropertyFilter("zone1", "zone2");
        Map<String, String> asset = new HashMap<>();
        asset.put("zone1", null);
        asset.put("zone2", null);

        boolean result = propertyEqualsPropertyFilter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_NullLeftPropertyName_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyEqualsPropertyFilter(null, "b"));
    }

    @Test
    void given_NullRightPropertyName_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyEqualsPropertyFilter("a", null));
    }

    @Test
    void given_Filter_when_ToString_then_ShowsUnquotedPropertyNames() {
        PropertyEqualsPropertyFilter propertyEqualsPropertyFilter = new PropertyEqualsPropertyFilter("zone1", "zone2");

        String result = propertyEqualsPropertyFilter.toString();

        assertEquals("zone1 = zone2", result);
    }
}
