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

import static com.example.eligibility.operator.StandardComparisonOperator.GREATER_THAN;
import static com.example.eligibility.operator.StandardComparisonOperator.GREATER_THAN_OR_EQUAL;
import static com.example.eligibility.operator.StandardComparisonOperator.LESS_THAN;
import static com.example.eligibility.operator.StandardComparisonOperator.LESS_THAN_OR_EQUAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertyComparedToValueFilterTest {

    private static final StringToNumericConverter NUMERIC = new StringToNumericConverter();
    private static final BigDecimal TWENTY = new BigDecimal("20");

    static Stream<Arguments> propertyValuesAndValues() {
        return Stream.of(
                Arguments.of("85", GREATER_THAN, TWENTY, true),
                Arguments.of("20", GREATER_THAN, TWENTY, false),
                Arguments.of("85", GREATER_THAN_OR_EQUAL, TWENTY, true),
                Arguments.of("20.0", GREATER_THAN_OR_EQUAL, TWENTY, true),
                Arguments.of("10", GREATER_THAN_OR_EQUAL, TWENTY, false),
                Arguments.of("-10", LESS_THAN, TWENTY, true),
                Arguments.of("20", LESS_THAN, TWENTY, false),
                Arguments.of("20", LESS_THAN_OR_EQUAL, TWENTY, true),
                Arguments.of("21", LESS_THAN_OR_EQUAL, TWENTY, false),
                Arguments.of(null, LESS_THAN_OR_EQUAL, TWENTY, false),
                Arguments.of("abc", LESS_THAN_OR_EQUAL, TWENTY, false));
    }

    @ParameterizedTest
    @MethodSource("propertyValuesAndValues")
    void given_PropertyValueAndValue_when_Comparing_then_FollowsOperator(String propertyValue,
                                                                         StandardComparisonOperator operator,
                                                                         BigDecimal value,
                                                                         boolean expected) {
        PropertyComparedToValueFilter<BigDecimal> propertyComparedToValueFilter =
                new PropertyComparedToValueFilter<>("batteryLevel", operator, value, NUMERIC);
        Map<String, String> asset = new HashMap<>();
        asset.put("batteryLevel", propertyValue);

        boolean result = propertyComparedToValueFilter.matches(asset);

        assertEquals(expected, result);
    }

    @Test
    void given_MissingProperty_when_Comparing_then_DoesNotMatch() {
        PropertyComparedToValueFilter<BigDecimal> propertyComparedToValueFilter =
                new PropertyComparedToValueFilter<>("batteryLevel", GREATER_THAN_OR_EQUAL, TWENTY, NUMERIC);
        Map<String, String> asset = Map.of("zone", "Lombardy-Plant-A");

        boolean result = propertyComparedToValueFilter.matches(asset);

        assertFalse(result);
    }

    @Test
    void given_NullPropertyName_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyComparedToValueFilter<>(null, GREATER_THAN, TWENTY, NUMERIC));
    }

    @Test
    void given_NullOperator_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class, () -> new PropertyComparedToValueFilter<>("batteryLevel", null, TWENTY, NUMERIC));
    }

    @Test
    void given_NullValue_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class,
                () -> new PropertyComparedToValueFilter<>("batteryLevel", GREATER_THAN, null, NUMERIC));
    }

    @Test
    void given_NullConverter_when_Creating_then_Throws() {
        assertThrows(NullPointerException.class,
                () -> new PropertyComparedToValueFilter<>("batteryLevel", GREATER_THAN, TWENTY, null));
    }

    @Test
    void given_Filter_when_ToString_then_ShowsPropertyOperatorAndValue() {
        PropertyComparedToValueFilter<BigDecimal> propertyComparedToValueFilter =
                new PropertyComparedToValueFilter<>("batteryLevel", GREATER_THAN_OR_EQUAL, TWENTY, NUMERIC);

        String result = propertyComparedToValueFilter.toString();

        assertEquals("batteryLevel >= 20", result);
    }

    @Test
    void given_ValueInScientificNotation_when_ToString_then_ShowsPlainNumber() {
        PropertyComparedToValueFilter<BigDecimal> propertyComparedToValueFilter =
                new PropertyComparedToValueFilter<>("batteryLevel", GREATER_THAN_OR_EQUAL, new BigDecimal("1E+3"), NUMERIC);

        String result = propertyComparedToValueFilter.toString();

        assertEquals("batteryLevel >= 1000", result);
    }
}
