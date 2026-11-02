package com.example.eligibility.operator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.example.eligibility.operator.StandardComparisonOperator.GREATER_THAN;
import static com.example.eligibility.operator.StandardComparisonOperator.GREATER_THAN_OR_EQUAL;
import static com.example.eligibility.operator.StandardComparisonOperator.LESS_THAN;
import static com.example.eligibility.operator.StandardComparisonOperator.LESS_THAN_OR_EQUAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardComparisonOperatorTest {

    static Stream<Arguments> comparisonResults() {
        return Stream.of(
                Arguments.of(GREATER_THAN, 1, true),
                Arguments.of(GREATER_THAN, 0, false),
                Arguments.of(GREATER_THAN, -1, false),
                Arguments.of(GREATER_THAN_OR_EQUAL, 1, true),
                Arguments.of(GREATER_THAN_OR_EQUAL, 0, true),
                Arguments.of(GREATER_THAN_OR_EQUAL, -1, false),
                Arguments.of(LESS_THAN, 1, false),
                Arguments.of(LESS_THAN, 0, false),
                Arguments.of(LESS_THAN, -1, true),
                Arguments.of(LESS_THAN_OR_EQUAL, 1, false),
                Arguments.of(LESS_THAN_OR_EQUAL, 0, true),
                Arguments.of(LESS_THAN_OR_EQUAL, -1, true));
    }

    static Stream<Arguments> symbols() {
        return Stream.of(
                Arguments.of(GREATER_THAN, ">"),
                Arguments.of(GREATER_THAN_OR_EQUAL, ">="),
                Arguments.of(LESS_THAN, "<"),
                Arguments.of(LESS_THAN_OR_EQUAL, "<="));
    }

    @ParameterizedTest(name = "{0}: compareTo = {1} -> {2}")
    @MethodSource("comparisonResults")
    void given_ComparisonResult_when_Testing_then_FollowsOperator(StandardComparisonOperator operator,
                                                                  int comparisonResult,
                                                                  boolean expected) {
        boolean result = operator.test(comparisonResult);

        assertEquals(expected, result);
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("symbols")
    void given_Operator_when_GettingSymbol_then_ReturnsMathSymbol(StandardComparisonOperator operator,
                                                                  String expected) {
        String result = operator.getSymbol();

        assertEquals(expected, result);
    }
}
