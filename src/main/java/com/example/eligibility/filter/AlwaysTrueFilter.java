package com.example.eligibility.filter;

import java.util.Map;

public record AlwaysTrueFilter() implements AssetFilter {

    @Override
    public boolean matches(Map<String, String> asset) {
        Validation.requireAsset(asset);
        return true;
    }

    @Override
    public String toString() {
        return "TRUE";
    }
}
