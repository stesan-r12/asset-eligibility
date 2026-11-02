package com.example.eligibility.filter;

import com.example.eligibility.converter.Converter;
import com.example.eligibility.operator.ComparisonOperator;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public record PropertyComparedToPropertyFilter<T extends Comparable<? super T>>(
        String leftProperty, ComparisonOperator operator, String rightProperty, Converter<T> converter)
        implements AssetFilter {

    public PropertyComparedToPropertyFilter {
        Validation.requirePropertyName(leftProperty);
        Objects.requireNonNull(operator, "operator must not be null");
        Validation.requirePropertyName(rightProperty);
        Objects.requireNonNull(converter, "converter must not be null");
    }

    @Override
    public boolean matches(Map<String, String> asset) {
        Validation.requireAsset(asset);
        Optional<T> left = converter.convert(asset.get(leftProperty));
        Optional<T> right = converter.convert(asset.get(rightProperty));
        if (left.isEmpty() || right.isEmpty()) {
            return false;
        }
        return operator.test(left.get().compareTo(right.get()));
    }

    @Override
    public String toString() {
        return leftProperty + " " + operator.getSymbol() + " " + rightProperty;
    }
}
