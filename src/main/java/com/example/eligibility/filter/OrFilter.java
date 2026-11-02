package com.example.eligibility.filter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record OrFilter(List<AssetFilter> operands) implements AssetFilter {

    public OrFilter {
        operands = List.copyOf(operands);
    }

    @Override
    public boolean matches(Map<String, String> asset) {
        Validation.requireAsset(asset);
        return operands.stream().anyMatch(operand -> operand.matches(asset));
    }

    @Override
    public String toString() {
        return operands.stream()
                .map(AssetFilter::toString)
                .collect(Collectors.joining(" OR ", "(", ")"));
    }
}
