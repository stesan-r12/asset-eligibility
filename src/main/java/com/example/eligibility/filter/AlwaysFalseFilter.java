package com.example.eligibility.filter;

import java.util.Map;

public record AlwaysFalseFilter() implements AssetFilter {

    @Override
    public boolean matches(Map<String, String> asset) {
        Validation.requireAsset(asset);
        return false;
    }

    @Override
    public String toString() {
        return "FALSE";
    }
}
