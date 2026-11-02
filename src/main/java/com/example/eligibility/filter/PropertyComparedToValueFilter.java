package com.example.eligibility.filter;

import com.example.eligibility.converter.Converter;
import com.example.eligibility.operator.ComparisonOperator;

import java.util.Map;
import java.util.Objects;

public record PropertyComparedToValueFilter<T extends Comparable<? super T>>(
        String propertyName, ComparisonOperator operator, T value, Converter<T> converter) implements AssetFilter {

    public PropertyComparedToValueFilter {
        Validation.requirePropertyName(propertyName);
        Objects.requireNonNull(operator, "operator must not be null");
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(converter, "converter must not be null");
    }

    @Override
    public boolean matches(Map<String, String> asset) {
        Validation.requireAsset(asset);
        return converter.convert(asset.get(propertyName))
                .map(actualValue -> operator.test(actualValue.compareTo(value)))
                .orElse(false);
    }

    @Override
    public String toString() {
        return propertyName + " " + operator.getSymbol() + " " + converter.format(value);
    }
}
