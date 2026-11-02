package com.example.eligibility.filter;

import com.example.eligibility.converter.StringToNumericConverter;
import com.example.eligibility.operator.StandardComparisonOperator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.example.eligibility.operator.StandardComparisonOperator.GREATER_THAN_OR_EQUAL;
import static com.example.eligibility.operator.StandardComparisonOperator.LESS_THAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertyComparedToPropertyFilterTest {

    private static final StringToNumericConverter NUMERIC = new StringToNumericConverter();

    static Stream<Arguments> leftAndRightValues() {
        return Stream.of(
                Arguments.of("60", LESS_THAN, "80", true),
                Arguments.of("80", LESS_THAN, "80", false),
                Arguments.of("79.9", LESS_THAN, "80", true),
                Arguments.of("-5", LESS_THAN, "0", true),
                Arguments.of("20", GREATER_THAN_OR_EQUAL, "20.0", true),
                Arguments.of("19", GREATER_THAN_OR_EQUAL, "20", false),
                Arguments.of(null, LESS_THAN, "80", false),
                Arguments.of("60", LESS_THAN, null, false),
                Arguments.of("hot", LESS_THAN, "80", false),
                Arguments.of("60", LESS_THAN, "n/a", false));
    }

    @ParameterizedTest
    @MethodSource("leftAndRightValues")
    void given_LeftAndRightValues_when_Comparing_then_FollowsOperator(String leftValue,
                                                                      StandardComparisonOperator operator,
                                                                      String rightValue,
                                                                      boolean expected) {
        PropertyComparedToPropertyFilter<BigDecimal> propertyComparedToPropertyFilter =
                new PropertyComparedToPropertyFilter<>("currentTemperature", operator, "maxAllowedTemperature", NUMERIC);
        Map<String, String> asset = new HashMap<>();
        asset.put("currentTemperature", leftValue);
        asset.put("maxAllowedTemperature", rightValue);

        boolean result = propertyComparedToPropertyFilter.matches(asset);

        assertEquals(expected, result);
    }

    @Test
    void given_MissingProperty_when_Comparing_then_DoesNotMatch() {
        PropertyComparedToPropertyFilter<BigDecimal> propertyComparedToPropertyFilter =
                new PropertyComparedToPropertyFilter<>("currentTemperature", LESS_THAN, "maxAllowedTemperature", NUMERIC);
        Map<String, String> asset = Map.of("currentTemperature", "60");

        boolean result = propertyComparedToPropertyFilter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_NullLeftPropertyName_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyComparedToPropertyFilter<>(null, LESS_THAN, "b", NUMERIC));
    }

    @Test
    void given_NullRightPropertyName_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyComparedToPropertyFilter<>("a", LESS_THAN, null, NUMERIC));
    }

    @Test
    void given_NullOperator_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyComparedToPropertyFilter<>("a", null, "b", NUMERIC));
    }

    @Test
    void given_NullConverter_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyComparedToPropertyFilter<>("a", LESS_THAN, "b", null));
    }

    @Test
    void given_Filter_when_ToString_then_ShowsBothPropertyNames() {
        PropertyComparedToPropertyFilter<BigDecimal> propertyComparedToPropertyFilter =
                new PropertyComparedToPropertyFilter<>("currentTemperature", LESS_THAN, "maxAllowedTemperature", NUMERIC);

        String result = propertyComparedToPropertyFilter.toString();

        assertEquals("currentTemperature < maxAllowedTemperature", result);
    }
}
