package com.example.eligibility;

import com.example.eligibility.filter.AndFilter;
import com.example.eligibility.filter.AssetFilter;
import com.example.eligibility.filter.NotFilter;
import com.example.eligibility.filter.OrFilter;
import com.example.eligibility.filter.PropertyEqualsValueFilter;
import com.example.eligibility.filter.PropertyExistsFilter;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eligibility.Filters.and;
import static com.example.eligibility.Filters.eq;
import static com.example.eligibility.Filters.exists;
import static com.example.eligibility.Filters.not;
import static com.example.eligibility.Filters.or;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilterCompositionTest {

    private static final AssetFilter ZONE_AND_ONLINE = and(eq("zone", "Lombardy-Plant-A"), eq("isOnline", "true"));

    // online AND (zone A OR zone B) AND NOT firmware 1.0.0
    private static final AssetFilter NESTED = and(
            eq("isOnline", "true"),
            or(eq("zone", "Lombardy-Plant-A"), eq("zone", "Piedmont-Plant-B")),
            not(eq("firmwareVersion", "1.0.0")));

    private static final AssetFilter HAS_ZONE_AND_NOT_LOMBARDY =
            and(exists("zone"), not(eq("zone", "Lombardy-Plant-A")));

    private static Map<String, String> sensor() {
        Map<String, String> sensor = new HashMap<>();
        sensor.put("zone", "Lombardy-Plant-A");
        sensor.put("isOnline", "true");
        sensor.put("batteryLevel", "85");
        sensor.put("firmwareVersion", "2.4.1");
        return sensor;
    }

    @Test
    void given_FactoryMethods_when_BuildingFilters_then_EqualToConstructorInstances() {
        assertEquals(new PropertyEqualsValueFilter("zone", "X"), eq("zone", "X"));
        assertEquals(new PropertyExistsFilter("zone"), exists("zone"));
        assertEquals(new NotFilter(eq("zone", "X")), not(eq("zone", "X")));
        assertEquals(new AndFilter(List.of(eq("a", "1"), eq("b", "2"))), and(eq("a", "1"), eq("b", "2")));
        assertEquals(new OrFilter(List.of(eq("a", "1"), eq("b", "2"))), or(eq("a", "1"), eq("b", "2")));
    }

    @Test
    void given_OnlineSensorInZone_when_MatchingZoneAndOnline_then_Matches() {
        Map<String, String> sensor = sensor();

        boolean result = ZONE_AND_ONLINE.matches(sensor);

        assertTrue(result);
    }

    @Test
    void given_OfflineSensor_when_MatchingZoneAndOnline_then_DoesNotMatch() {
        Map<String, String> sensor = sensor();
        sensor.put("isOnline", "false");

        boolean result = ZONE_AND_ONLINE.matches(sensor);

        assertFalse(result);
    }

    @Test
    void given_SensorSatisfyingEveryLevel_when_MatchingNestedFilter_then_Matches() {
        Map<String, String> sensor = sensor();

        boolean result = NESTED.matches(sensor);

        assertTrue(result);
    }

    @Test
    void given_SensorInOtherZone_when_MatchingNestedFilter_then_DoesNotMatch() {
        Map<String, String> sensor = sensor();
        sensor.put("zone", "Veneto-Plant-C");

        boolean result = NESTED.matches(sensor);

        assertFalse(result);
    }

    @Test
    void given_SensorWithExcludedFirmware_when_MatchingNestedFilter_then_DoesNotMatch() {
        Map<String, String> sensor = sensor();
        sensor.put("firmwareVersion", "1.0.0");

        boolean result = NESTED.matches(sensor);

        assertFalse(result);
    }

    @Test
    void given_MissingProperty_when_NegationGuardedByExists_then_DoesNotMatch() {
        Map<String, String> asset = Map.of();

        boolean result = HAS_ZONE_AND_NOT_LOMBARDY.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_OtherZone_when_NegationGuardedByExists_then_Matches() {
        Map<String, String> asset = Map.of("zone", "Veneto-Plant-C");

        boolean result = HAS_ZONE_AND_NOT_LOMBARDY.matches(asset);

        assertTrue(result);
    }

    @Test
    void given_OneFilter_when_MatchingDifferentAssets_then_EachIsEvaluatedIndependently() {
        AssetFilter filter = eq("isOnline", "true");

        assertTrue(filter.matches(Map.of("isOnline", "true")));
        assertFalse(filter.matches(Map.of("isOnline", "false")));
        assertTrue(filter.matches(Map.of("isOnline", "TRUE")));
    }

    @Test
    void given_NestedFilter_when_ToString_then_ShowsStructure() {
        AssetFilter filter = and(
                eq("isOnline", "true"),
                or(eq("zone", "A"), eq("zone", "B")),
                not(exists("maintenanceLock")));

        String result = filter.toString();

        assertEquals("(isOnline = 'true' AND (zone = 'A' OR zone = 'B') AND NOT (EXISTS(maintenanceLock)))", result);
    }
}
