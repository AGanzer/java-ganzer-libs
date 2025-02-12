package de.ganzer.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GMathTest {
    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "0.4, 0, 0",
            "0.04, 1, 0",
            "0.5, 0, 1",
            "0.05, 1, 0.1",
            "-0.5, 0, 0",
            "-0.05, 1, 0",
            "-0.6, 0, -1",
            "-0.06, 1, -0.1",
            "14, -1, 10",
            "15, -1, 20",
            "-15, -1, -10",
            "-16, -1, -20"})
    void testRoundFloat(String p1, String p2, String exp) {
        float v = Float.parseFloat(p1);
        int d = Integer.parseInt(p2);
        float e = Float.parseFloat(exp);
        float r = GMath.round(v, d);

        assertEquals(e, r);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "0.4, 0, 0",
            "0.04, 1, 0",
            "0.5, 0, 1",
            "0.05, 1, 0.1",
            "-0.5, 0, 0",
            "-0.05, 1, 0",
            "-0.6, 0, -1",
            "-0.06, 1, -0.1",
            "14, -1, 10",
            "15, -1, 20",
            "-15, -1, -10",
            "-16, -1, -20"})
    void testRoundDouble(String p1, String p2, String exp) {
        double v = Double.parseDouble(p1);
        int d = Integer.parseInt(p2);
        double e = Double.parseDouble(exp);
        double r = GMath.round(v, d);

        assertEquals(e, r);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "0, 0, 0, 0",
            "0, 1, 10, 1",
            "5, 5, 10, 5",
            "10, 0, 10, 10",
            "12, 0, 11, 11"
    })
    void testToRangeInt(String value, String minimum, String maximum, String expected) {
        int val = Integer.parseInt(value);
        int min = Integer.parseInt(minimum);
        int max = Integer.parseInt(maximum);
        int exp = Integer.parseInt(expected);
        int res = GMath.toRange(val, min, max);

        assertEquals(exp, res);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "0, 0, 0, 0",
            "0, 1, 10, 1",
            "5, 5, 10, 5",
            "10, 0, 10, 10",
            "12, 0, 11, 11"
    })
    void testToRangeLong(String value, String minimum, String maximum, String expected) {
        long val = Long.parseLong(value);
        long min = Long.parseLong(minimum);
        long max = Long.parseLong(maximum);
        long exp = Long.parseLong(expected);
        long res = GMath.toRange(val, min, max);

        assertEquals(exp, res);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "0, 0, 0, 0",
            "0, 1, 10, 1",
            "5, 5, 10, 5",
            "10, 0, 10, 10",
            "12, 0, 11, 11"
    })
    void testToRangeFloat(String value, String minimum, String maximum, String expected) {
        float val = Float.parseFloat(value);
        float min = Float.parseFloat(minimum);
        float max = Float.parseFloat(maximum);
        float exp = Float.parseFloat(expected);
        float res = GMath.toRange(val, min, max);

        assertEquals(exp, res);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "0, 0, 0, 0",
            "0, 1, 10, 1",
            "5, 5, 10, 5",
            "10, 0, 10, 10",
            "12, 0, 11, 11"
    })
    void testToRangeDouble(String value, String minimum, String maximum, String expected) {
        double val = Double.parseDouble(value);
        double min = Double.parseDouble(minimum);
        double max = Double.parseDouble(maximum);
        double exp = Double.parseDouble(expected);
        double res = GMath.toRange(val, min, max);

        assertEquals(exp, res);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "0, 0, 0, true",
            "0, 1, 10, false",
            "5, 5, 10, true",
            "10, 0, 10, true",
            "12, 0, 11, false"
    })
    void testIsInRangeInt(String value, String minimum, String maximum, String expected) {
        int val = Integer.parseInt(value);
        int min = Integer.parseInt(minimum);
        int max = Integer.parseInt(maximum);
        boolean exp = expected.equals("true");
        boolean res = GMath.isInRange(val, min, max);

        assertEquals(exp, res);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "0, 0, 0, true",
            "0, 1, 10, false",
            "5, 5, 10, true",
            "10, 0, 10, true",
            "12, 0, 11, false"
    })
    void testIsInRangeLong(String value, String minimum, String maximum, String expected) {
        long val = Long.parseLong(value);
        long min = Long.parseLong(minimum);
        long max = Long.parseLong(maximum);
        boolean exp = expected.equals("true");
        boolean res = GMath.isInRange(val, min, max);

        assertEquals(exp, res);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "0, 0, 0, true",
            "0, 1, 10, false",
            "5, 5, 10, true",
            "10, 0, 10, true",
            "12, 0, 11, false"
    })
    void testIsInRangeFloat(String value, String minimum, String maximum, String expected) {
        float val = Float.parseFloat(value);
        float min = Float.parseFloat(minimum);
        float max = Float.parseFloat(maximum);
        boolean exp = expected.equals("true");
        boolean res = GMath.isInRange(val, min, max);

        assertEquals(exp, res);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "0, 0, 0, true",
            "0, 1, 10, false",
            "5, 5, 10, true",
            "10, 0, 10, true",
            "12, 0, 11, false"
    })
    void testIsInRangeDouble(String value, String minimum, String maximum, String expected) {
        double val = Double.parseDouble(value);
        double min = Double.parseDouble(minimum);
        double max = Double.parseDouble(maximum);
        boolean exp = expected.equals("true");
        boolean res = GMath.isInRange(val, min, max);

        assertEquals(exp, res);
    }

    @Test
    void testMin() {
        LocalDateTime a = LocalDateTime.parse("2020-01-01T00:00:00");
        LocalDateTime b = LocalDateTime.parse("2020-01-02T00:00:01");

        assertEquals(a, GMath.min(a, b));
    }

    @Test
    void testMax() {
        LocalDateTime a = LocalDateTime.parse("2020-01-01T00:00:00");
        LocalDateTime b = LocalDateTime.parse("2020-01-02T00:00:01");

        assertEquals(b, GMath.max(a, b));
    }
}
