package de.ganzer.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
        var val = Integer.parseInt(value);
        var min = Integer.parseInt(minimum);
        var max = Integer.parseInt(maximum);
        var exp = Integer.parseInt(expected);
        var res = GMath.toRange(val, min, max);

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
        var val = Long.parseLong(value);
        var min = Long.parseLong(minimum);
        var max = Long.parseLong(maximum);
        var exp = Long.parseLong(expected);
        var res = GMath.toRange(val, min, max);

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
        var val = Float.parseFloat(value);
        var min = Float.parseFloat(minimum);
        var max = Float.parseFloat(maximum);
        var exp = Float.parseFloat(expected);
        var res = GMath.toRange(val, min, max);

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
        var val = Double.parseDouble(value);
        var min = Double.parseDouble(minimum);
        var max = Double.parseDouble(maximum);
        var exp = Double.parseDouble(expected);
        var res = GMath.toRange(val, min, max);

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
        var val = Integer.parseInt(value);
        var min = Integer.parseInt(minimum);
        var max = Integer.parseInt(maximum);
        var exp = expected.equals("true");
        var res = GMath.isInRange(val, min, max);

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
        var val = Long.parseLong(value);
        var min = Long.parseLong(minimum);
        var max = Long.parseLong(maximum);
        var exp = expected.equals("true");
        var res = GMath.isInRange(val, min, max);

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
        var val = Float.parseFloat(value);
        var min = Float.parseFloat(minimum);
        var max = Float.parseFloat(maximum);
        var exp = expected.equals("true");
        var res = GMath.isInRange(val, min, max);

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
        var val = Double.parseDouble(value);
        var min = Double.parseDouble(minimum);
        var max = Double.parseDouble(maximum);
        var exp = expected.equals("true");
        var res = GMath.isInRange(val, min, max);

        assertEquals(exp, res);
    }
}