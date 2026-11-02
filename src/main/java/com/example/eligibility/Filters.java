package com.example.eligibility;

import com.example.eligibility.converter.Converter;
import com.example.eligibility.converter.StringToNumericConverter;
import com.example.eligibility.filter.AlwaysFalseFilter;
import com.example.eligibility.filter.AlwaysTrueFilter;
import com.example.eligibility.filter.AndFilter;
import com.example.eligibility.filter.AssetFilter;
import com.example.eligibility.filter.NotFilter;
import com.example.eligibility.filter.OrFilter;
import com.example.eligibility.filter.PropertyComparedToPropertyFilter;
import com.example.eligibility.filter.PropertyComparedToValueFilter;
import com.example.eligibility.filter.PropertyEqualsPropertyFilter;
import com.example.eligibility.filter.PropertyEqualsValueFilter;
import com.example.eligibility.filter.PropertyExistsFilter;
import com.example.eligibility.operator.ComparisonOperator;

import java.math.BigDecimal;
import java.util.List;

public final class Filters {

    private Filters() {
    }

    public static AssetFilter alwaysTrue() {
        return new AlwaysTrueFilter();
    }

    public static AssetFilter alwaysFalse() {
        return new AlwaysFalseFilter();
    }

    public static AssetFilter exists(String propertyName) {
        return new PropertyExistsFilter(propertyName);
    }

    public static AssetFilter eq(String propertyName, String expectedValue) {
        return new PropertyEqualsValueFilter(propertyName, expectedValue);
    }

    public static <T extends Comparable<? super T>> AssetFilter compare(String propertyName,
                                                                       ComparisonOperator operator,
                                                                       T value,
                                                                       Converter<T> converter) {
        return new PropertyComparedToValueFilter<>(propertyName, operator, value, converter);
    }

    public static AssetFilter compare(String propertyName, ComparisonOperator operator, BigDecimal value) {
        return compare(propertyName, operator, value, new StringToNumericConverter());
    }

    public static <T extends Comparable<? super T>> AssetFilter compareProperties(String leftProperty,
                                                                                 ComparisonOperator operator,
                                                                                 String rightProperty,
                                                                                 Converter<T> converter) {
        return new PropertyComparedToPropertyFilter<>(leftProperty, operator, rightProperty, converter);
    }

    public static AssetFilter compareProperties(String leftProperty, ComparisonOperator operator,
                                                String rightProperty) {
        return compareProperties(leftProperty, operator, rightProperty, new StringToNumericConverter());
    }

    public static AssetFilter eqProperties(String leftProperty, String rightProperty) {
        return new PropertyEqualsPropertyFilter(leftProperty, rightProperty);
    }

    public static AssetFilter not(AssetFilter operand) {
        return new NotFilter(operand);
    }

    public static AssetFilter and(AssetFilter... operands) {
        return new AndFilter(List.of(operands));
    }

    public static AssetFilter or(AssetFilter... operands) {
        return new OrFilter(List.of(operands));
    }
}
