package com.example.eligibility.filter;

import java.util.Map;

/**
 * A criterion that an asset may or may not satisfy.
 */
@FunctionalInterface
public interface AssetFilter {

    /**
     * Returns true if the asset satisfies this filter.
     */
    boolean matches(Map<String, String> asset);
}
