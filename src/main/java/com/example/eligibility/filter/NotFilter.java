package com.example.eligibility.filter;

import java.util.Map;
import java.util.Objects;

public record NotFilter(AssetFilter operand) implements AssetFilter {

    public NotFilter {
        Objects.requireNonNull(operand, "operand must not be null");
    }

    @Override
    public boolean matches(Map<String, String> asset) {
        Validation.requireAsset(asset);
        return !operand.matches(asset);
    }

    @Override
    public String toString() {
        return "NOT (" + operand + ")";
    }
}
