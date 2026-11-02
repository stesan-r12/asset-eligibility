package com.example.eligibility.filter;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationTest {

    @Test
    void given_ValidPropertyName_when_Validating_then_DoesNotThrow() {
        assertDoesNotThrow(() -> Validation.requirePropertyName("zone"));
    }

    @Test
    void given_NullPropertyName_when_Validating_then_Throws() {
        assertThrows(NullPointerException.class, () -> Validation.requirePropertyName(null));
    }

    @Test
    void given_EmptyPropertyName_when_Validating_then_Throws() {
        assertThrows(IllegalArgumentException.class, () -> Validation.requirePropertyName(""));
    }

    @Test
    void given_SpacesOnlyPropertyName_when_Validating_then_Throws() {
        assertThrows(IllegalArgumentException.class, () -> Validation.requirePropertyName("   "));
    }

    @Test
    void given_WhitespaceOnlyPropertyName_when_Validating_then_Throws() {
        assertThrows(IllegalArgumentException.class, () -> Validation.requirePropertyName("\t\n"));
    }

    @Test
    void given_Asset_when_Validating_then_DoesNotThrow() {
        assertDoesNotThrow(() -> Validation.requireAsset(Map.of()));
    }

    @Test
    void given_NullAsset_when_Validating_then_Throws() {
        assertThrows(NullPointerException.class, () -> Validation.requireAsset(null));
    }
}
