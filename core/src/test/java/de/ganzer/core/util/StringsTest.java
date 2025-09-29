package de.ganzer.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantValue")
class StringsTest {
    @Test
    void isNullOrEmptyArray() {
        assertTrue(Strings.isNullOrEmpty((String[]) null));
    }

    @Test
    void isNullOrEmptyArray2() {
        assertTrue(Strings.isNullOrEmpty(new String[0]));
    }

    @Test
    void isNullOrEmptyArrayFalse() {
        assertFalse(Strings.isNullOrEmpty(new String[1]));
    }

    @Test
    void isNullOrEmpty() {
        assertTrue(Strings.isNullOrEmpty((String) null));
    }

    @Test
    void isNullOrEmpty2() {
        assertTrue(Strings.isNullOrEmpty(""));
    }

    @Test
    void isNullOrEmptyFalse() {
        assertFalse(Strings.isNullOrEmpty("  "));
    }

    @Test
    void isNullOrBlank() {
        assertTrue(Strings.isNullOrBlank(null));
    }

    @Test
    void isNullOrBlank2() {
        assertTrue(Strings.isNullOrBlank(""));
    }

    @Test
    void isNullOrBlank3() {
        assertTrue(Strings.isNullOrBlank("  "));
    }

    @Test
    void isNullOrBlankFalse() {
        assertFalse(Strings.isNullOrBlank(" k "));
    }
}