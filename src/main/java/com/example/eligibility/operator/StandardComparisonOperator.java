package com.example.eligibility.operator;

import java.util.function.IntPredicate;

public enum StandardComparisonOperator implements ComparisonOperator {

    GREATER_THAN(">", c -> c > 0),
    GREATER_THAN_OR_EQUAL(">=", c -> c >= 0),
    LESS_THAN("<", c -> c < 0),
    LESS_THAN_OR_EQUAL("<=", c -> c <= 0);

    private final String symbol;
    private final IntPredicate predicate;

    StandardComparisonOperator(String symbol, IntPredicate predicate) {
        this.symbol = symbol;
        this.predicate = predicate;
    }

    @Override
    public boolean test(int comparisonResult) {
        return predicate.test(comparisonResult);
    }

    @Override
    public String getSymbol() {
        return symbol;
    }
}
