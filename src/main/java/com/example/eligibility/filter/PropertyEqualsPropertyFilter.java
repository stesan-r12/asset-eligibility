package com.example.eligibility.filter;

import java.util.Map;

public record PropertyEqualsPropertyFilter(String leftProperty, String rightProperty) implements AssetFilter {

    public PropertyEqualsPropertyFilter {
        Validation.requirePropertyName(leftProperty);
        Validation.requirePropertyName(rightProperty);
    }

    @Override
    public boolean matches(Map<String, String> asset) {
        Validation.requireAsset(asset);
        String left = asset.get(leftProperty);
        String right = asset.get(rightProperty);
        return left != null && left.equalsIgnoreCase(right);
    }

    @Override
    public String toString() {
        return leftProperty + " = " + rightProperty;
    }
}
