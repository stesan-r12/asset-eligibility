package com.example.eligibility.converter;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringToNumericConverterTest {

    private final StringToNumericConverter stringToNumericConverter = new StringToNumericConverter();

    @Test
    void given_Integer_when_Converting_then_ReturnsNumber() {
        Optional<BigDecimal> result = stringToNumericConverter.convert("85");

        assertEquals(Optional.of(new BigDecimal("85")), result);
    }

    @Test
    void given_NegativeDecimal_when_Converting_then_ReturnsNumber() {
        Optional<BigDecimal> result = stringToNumericConverter.convert("-0.5");

        assertEquals(Optional.of(new BigDecimal("-0.5")), result);
    }

    @Test
    void given_ScientificNotation_when_Converting_then_ReturnsNumber() {
        Optional<BigDecimal> result = stringToNumericConverter.convert("1E3");

        assertEquals(Optional.of(new BigDecimal("1E3")), result);
    }

    @Test
    void given_ExplicitPlusSign_when_Converting_then_ReturnsNumber() {
        Optional<BigDecimal> result = stringToNumericConverter.convert("+5");

        assertEquals(Optional.of(new BigDecimal("5")), result);
    }

    @Test
    void given_Null_when_Converting_then_ReturnsEmpty() {
        Optional<BigDecimal> result = stringToNumericConverter.convert(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void given_Text_when_Converting_then_ReturnsEmpty() {
        Optional<BigDecimal> result = stringToNumericConverter.convert("abc");

        assertTrue(result.isEmpty());
    }

    @Test
    void given_EmptyString_when_Converting_then_ReturnsEmpty() {
        Optional<BigDecimal> result = stringToNumericConverter.convert("");

        assertTrue(result.isEmpty());
    }

    @Test
    void given_SurroundingWhitespace_when_Converting_then_ReturnsEmpty() {
        Optional<BigDecimal> result = stringToNumericConverter.convert(" 85 ");

        assertTrue(result.isEmpty());
    }

    @Test
    void given_CommaDecimalSeparator_when_Converting_then_ReturnsEmpty() {
        Optional<BigDecimal> result = stringToNumericConverter.convert("1,5");

        assertTrue(result.isEmpty());
    }

    @Test
    void given_NaN_when_Converting_then_ReturnsEmpty() {
        Optional<BigDecimal> result = stringToNumericConverter.convert("NaN");

        assertTrue(result.isEmpty());
    }

    @Test
    void given_Infinity_when_Converting_then_ReturnsEmpty() {
        Optional<BigDecimal> result = stringToNumericConverter.convert("Infinity");

        assertTrue(result.isEmpty());
    }

    @Test
    void given_ScientificNotation_when_Formatting_then_ReturnsPlainNumber() {
        String result = stringToNumericConverter.format(new BigDecimal("1E+3"));

        assertEquals("1000", result);
    }
}
