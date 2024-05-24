package de.ganzer.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    void getFirst() {
        String first = "abc";
        int second = 123;
        Pair<String, Integer> p = new Pair<>(first, second);

        assertEquals(first, p.getFirst());
    }

    @Test
    void getSecond() {
        String first = "abc";
        int second = 123;
        Pair<String, Integer> p = new Pair<>(first, second);

        assertEquals(second, p.getSecond());
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "abc, 1, abc, 1, true",
            "abc, 1, abc, 2, false"})
    void testEquals(String f1, String s1, String f2, String s2, String expected) {
        Pair<String, String> p1 = new Pair<>(f1, s1);
        Pair<String, String> p2 = new Pair<>(f2, s2);
        boolean result = p1.equals(p2);

        assertEquals("true".equals(expected), result);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "abc, 1, abc, 1, true",
            "abd, 1, abc, 2, false"})
    void testHashCode(String f1, String s1, String f2, String s2, String expected) {
        Pair<String, String> p1 = new Pair<>(f1, s1);
        Pair<String, String> p2 = new Pair<>(f2, s2);
        int h1 = p1.hashCode();
        int h2 = p2.hashCode();

        assertEquals("true".equals(expected), h1 == h2);
    }
}
