package com.example.eligibility.converter;

import java.util.Optional;

/**
 * Converts a raw asset value into a typed value that can be compared.
 */
public interface Converter<T> {

    /**
     * Returns the converted value, or empty if the value is missing or not valid.
     */
    Optional<T> convert(String rawValue);

    /**
     * Returns the text shown in the filter description.
     */
    default String format(T value) {
        return value.toString();
    }
}
