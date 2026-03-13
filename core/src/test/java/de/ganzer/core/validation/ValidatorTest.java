package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void setOptions() {
        Validator val = new Validator();
        val.setOptions(ValidatorOptions.AUTO_FILL);

        assertEquals(ValidatorOptions.AUTO_FILL, val.getOptions());
    }

    @Test
    void setRequiredErrorMessage() {
        Validator val = new Validator();
        val.setRequiredErrorMessage("m");

        assertEquals("m", val.getRequiredErrorMessage());
    }

    @Test
    void setBlanksErrorMessage() {
        Validator val = new Validator();
        val.setBlanksErrorMessage("m");

        assertEquals("m", val.getBlanksErrorMessage());
    }

    @Test
    void setErrorMessage() {
        Validator val = new Validator();
        val.setErrorMessage("m");

        assertEquals("m", val.getErrorMessage());
    }

    @Test
    void hasOption() {
        Validator val = new Validator();

        assertFalse(val.hasOption(ValidatorOptions.AUTO_FILL));
        assertTrue(val.hasOption(ValidatorOptions.NEEDS_INPUT));
        assertFalse(val.hasOption(ValidatorOptions.BLANKS_VALID));
    }
}