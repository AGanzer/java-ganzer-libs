package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListValidatorTest {
    @Test
    void constructEmpty() {
        var val = new ListValidator();

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertNull(val.getValidInputs());
        assertFalse(val.isIgnoreCase());
    }

    @Test
    void constructWithOption() {
        var val = new ListValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertNull(val.getValidInputs());
        assertFalse(val.isIgnoreCase());
    }

    @Test
    void constructWithOptionAndInputs() {
        List<String> vi = new ArrayList<>();
        var val = new ListValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, vi);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(vi, val.getValidInputs());
        assertFalse(val.isIgnoreCase());
    }

    @Test
    void constructWithOptionAndInputsAndCase() {
        List<String> vi = new ArrayList<>();
        var val = new ListValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, vi, true);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(vi, val.getValidInputs());
        assertTrue(val.isIgnoreCase());
    }

    @Test
    void constructWithInputs() {
        List<String> vi = new ArrayList<>();
        var val = new ListValidator(vi);

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(vi, val.getValidInputs());
        assertFalse(val.isIgnoreCase());
    }

    @Test
    void constructWithInputsAndCase() {
        List<String> vi = new ArrayList<>();
        var val = new ListValidator(vi, true);

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(vi, val.getValidInputs());
        assertTrue(val.isIgnoreCase());
    }

    @Test
    void setValidInputs() {
        List<String> vi = new ArrayList<>();
        var val = new ListValidator();

        val.setValidInputs(vi);

        assertEquals(vi, val.getValidInputs());
    }

    @Test
    void setIgnoreCase() {
        var val = new ListValidator();

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertNull(val.getValidInputs());
        assertFalse(val.isIgnoreCase());
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "AAA, false",
            "DDD, false",
            "AA, false",
            "BB, false",
            "CC, false",
            "A, false",
            "B, false",
            "aaa, true",
            "ccc, true",
            "ddd, true",
            "aa, true",
            "bb, true",
            "cc, true",
            "a, true",
            "d, true",
            "e, false",
            "aaaa, false",
            "bbb, true"})
    void doInputValidation(String input, String valid) {
        boolean expected = Boolean.parseBoolean(valid);
        List<String> vi = new ArrayList<>() {{
            add("aaa");
            add("bbb");
            add("ccc");
            add("ddd");
        }};

        var val = new ListValidator(vi);
        assertEquals(expected, val.isValidInput(new StringBuilder(input), false));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "AAA, true",
            "DDD, true",
            "AA, true",
            "BB, true",
            "CC, true",
            "A, true",
            "B, true",
            "aaa, true",
            "ccc, true",
            "ddd, true",
            "aa, true",
            "bb, true",
            "cc, true",
            "a, true",
            "d, true",
            "e, false",
            "aaaa, false",
            "bbb, true"})
    void doInputValidationIgnoreCase(String input, String valid) {
        boolean expected = Boolean.parseBoolean(valid);
        List<String> vi = new ArrayList<>() {{
            add("aaa");
            add("bbb");
            add("ccc");
            add("ddd");
        }};

        var val = new ListValidator(vi, true);
        assertEquals(expected, val.isValidInput(new StringBuilder(input), false));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "AAA, false",
            "DDD, false",
            "AA, false",
            "BB, false",
            "CC, false",
            "A, false",
            "B, false",
            "aaa, true",
            "ccc, true",
            "ddd, true",
            "aa, false",
            "bb, false",
            "a, false",
            "e, false",
            "aaaa, false",
            "bbb, true"})
    void doValidate(String input, String valid) {
        boolean expected = Boolean.parseBoolean(valid);
        List<String> vi = new ArrayList<>() {{
            add("aaa");
            add("bbb");
            add("ccc");
            add("ddd");
        }};

        var val = new ListValidator(vi);
        var ref = new ValidatorExceptionRef();

        assertEquals(expected, val.validate(input, ref));
        assertEquals(expected, ref.getException() == null);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ',', value = {
            "AAA, true",
            "DDD, true",
            "AA, false",
            "A, false",
            "B, false",
            "aaa, true",
            "ccc, true",
            "ddd, true",
            "aa, false",
            "bb, false",
            "a, false",
            "e, false",
            "aaaa, false",
            "bbb, true"})
    void doValidateIgnoreCase(String input, String valid) {
        boolean expected = Boolean.parseBoolean(valid);
        List<String> vi = new ArrayList<>() {{
            add("aaa");
            add("bbb");
            add("ccc");
            add("ddd");
        }};

        var val = new ListValidator(vi, true);
        var ref = new ValidatorExceptionRef();

        assertEquals(expected, val.validate(input, ref));
        assertEquals(expected, ref.getException() == null);
   }
}