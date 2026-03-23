package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void setOptions() {
        var val = new Validator();
        val.setOptions(ValidatorOptions.AUTO_FILL);

        assertEquals(ValidatorOptions.AUTO_FILL, val.getOptions());
    }

    @Test
    void setRequiredErrorMessage() {
        var val = new Validator();
        val.setRequiredErrorMessage("m");

        assertEquals("m", val.getRequiredErrorMessage());
    }

    @Test
    void setBlanksErrorMessage() {
        var val = new Validator();
        val.setBlanksErrorMessage("m");

        assertEquals("m", val.getBlanksErrorMessage());
    }

    @Test
    void setErrorMessage() {
        var val = new Validator();
        val.setErrorMessage("m");

        assertEquals("m", val.getErrorMessage());
    }

    @Test
    void hasOption() {
        var val = new Validator();

        assertFalse(val.hasOption(ValidatorOptions.AUTO_FILL));
        assertTrue(val.hasOption(ValidatorOptions.NEEDS_INPUT));
        assertFalse(val.hasOption(ValidatorOptions.BLANKS_VALID));
    }
}