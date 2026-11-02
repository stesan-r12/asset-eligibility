package com.example.eligibility.filter;

import java.util.Map;
import java.util.Objects;

public record PropertyEqualsValueFilter(String propertyName, String expectedValue) implements AssetFilter {

    public PropertyEqualsValueFilter {
        Validation.requirePropertyName(propertyName);
        Objects.requireNonNull(expectedValue, "expectedValue must not be null");
    }

    @Override
    public boolean matches(Map<String, String> asset) {
        Validation.requireAsset(asset);
        String actualValue = asset.get(propertyName);
        return actualValue != null && actualValue.equalsIgnoreCase(expectedValue);
    }

    @Override
    public String toString() {
        return propertyName + " = '" + expectedValue + "'";
    }
}
