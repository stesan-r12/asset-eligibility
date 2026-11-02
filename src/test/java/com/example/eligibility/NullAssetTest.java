package com.example.eligibility;

import com.example.eligibility.filter.AssetFilter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.example.eligibility.Filters.alwaysFalse;
import static com.example.eligibility.Filters.alwaysTrue;
import static com.example.eligibility.Filters.and;
import static com.example.eligibility.Filters.compareProperties;
import static com.example.eligibility.Filters.eq;
import static com.example.eligibility.Filters.eqProperties;
import static com.example.eligibility.Filters.exists;
import static com.example.eligibility.Filters.compare;
import static com.example.eligibility.Filters.not;
import static com.example.eligibility.Filters.or;
import static com.example.eligibility.operator.StandardComparisonOperator.LESS_THAN;
import static com.example.eligibility.operator.StandardComparisonOperator.GREATER_THAN_OR_EQUAL;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NullAssetTest {

    static Stream<AssetFilter> allFilters() {
        return Stream.of(
                alwaysTrue(),
                alwaysFalse(),
                exists("zone"),
                eq("zone", "A"),
                compare("batteryLevel", GREATER_THAN_OR_EQUAL, BigDecimal.valueOf(20)),
                compareProperties("a", LESS_THAN, "b"),
                eqProperties("a", "b"),
                not(alwaysTrue()),
                and(),
                or());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("allFilters")
    void given_AnyFilter_when_MatchingNullAsset_then_Throws(AssetFilter filter) {
        assertThrows(NullPointerException.class, () -> filter.matches(null));
    }
}
