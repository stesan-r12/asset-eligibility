package com.example.eligibility.operator;

/**
 * An ordering operator such as {@code >} or {@code <=}.
 */
public interface ComparisonOperator {

    /**
     * Returns true if the result of {@code compareTo} satisfies this operator.
     */
    boolean test(int comparisonResult);

    /**
     * Returns the symbol shown in the filter description, e.g. {@code ">="}.
     */
    String getSymbol();
}
