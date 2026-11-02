package com.example.eligibility.filter;

import java.util.Map;

public record PropertyExistsFilter(String propertyName) implements AssetFilter {

    public PropertyExistsFilter {
        Validation.requirePropertyName(propertyName);
    }

    @Override
    public boolean matches(Map<String, String> asset) {
        Validation.requireAsset(asset);
        return asset.get(propertyName) != null;
    }

    @Override
    public String toString() {
        return "EXISTS(" + propertyName + ")";
    }
}
