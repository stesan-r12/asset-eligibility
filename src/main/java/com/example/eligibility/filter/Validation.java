package com.example.eligibility.filter;

import java.util.Map;
import java.util.Objects;

final class Validation {

    private Validation() {
    }

    static void requireAsset(Map<String, String> asset) {
        Objects.requireNonNull(asset, "asset must not be null");
    }

    static void requirePropertyName(String propertyName) {
        Objects.requireNonNull(propertyName, "propertyName must not be null");
        if (propertyName.isBlank()) {
            throw new IllegalArgumentException("propertyName must not be blank");
        }
    }
}
