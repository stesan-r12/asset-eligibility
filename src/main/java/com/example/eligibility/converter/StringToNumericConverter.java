package com.example.eligibility.converter;

import java.math.BigDecimal;
import java.util.Optional;

public record StringToNumericConverter() implements Converter<BigDecimal> {

    @Override
    public Optional<BigDecimal> convert(String rawValue) {
        if (rawValue == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new BigDecimal(rawValue));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public String format(BigDecimal value) {
        return value.toPlainString();
    }
}
