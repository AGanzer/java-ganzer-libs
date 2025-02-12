package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilterValidatorTest {
    @Test
    void constructEmpty() {
        FilterValidator val = new FilterValidator();

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals("", val.getValidMask());
        assertEquals("", val.getInvalidMask());
    }

    @Test
    void constructWithOption() {
        FilterValidator val = new FilterValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals("", val.getValidMask());
        assertEquals("", val.getInvalidMask());
    }

    @Test
    void constructWithMask() {
        String vm = "0-9";
        FilterValidator val = new FilterValidator(vm);

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(vm, val.getValidMask());
        assertEquals("", val.getInvalidMask());
    }

    @Test
    void constructWithMaskAndOption() {
        String vm = "0-9";
        FilterValidator val = new FilterValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, vm);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(vm, val.getValidMask());
        assertEquals("", val.getInvalidMask());
    }

    @Test
    void constructWithMasks() {
        String vm = "0-9";
        String im = "56";
        FilterValidator val = new FilterValidator(vm, im);

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(vm, val.getValidMask());
        assertEquals(im, val.getInvalidMask());
    }

    @Test
    void constructWithMasksAndOption() {
        String vm = "0-9";
        String im = "56";
        FilterValidator val = new FilterValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, vm, im);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(vm, val.getValidMask());
        assertEquals(im, val.getInvalidMask());
    }

    @Test
    void setValidMask() {
        String vm = "0-9";
        FilterValidator val = new FilterValidator();

        val.setValidMask(vm);
        assertEquals(vm, val.getValidMask());
        assertEquals("", val.getInvalidMask());
    }

    @Test
    void setInvalidMask() {
        String im = "0-9";
        FilterValidator val = new FilterValidator();

        val.setInvalidMask(im);
        assertEquals("", val.getValidMask());
        assertEquals(im, val.getInvalidMask());
    }

    @Test
    void doInputValidation() {
        String vm = "0-9+-";
        String im = "56";
        FilterValidator val = new FilterValidator(vm, im);

        assertTrue(val.isValidInput(new StringBuilder(), false));
        assertFalse(val.isValidInput(new StringBuilder("12345"), false));
        assertTrue(val.isValidInput(new StringBuilder("-+12347890"), false));
    }

    @Test
    void doValidate() {
        String vm = "0-9+-";
        String im = "56";
        FilterValidator val = new FilterValidator(vm, im);

        assertThrows(ValidatorException.class, () -> val.validate(""));
        assertThrows(ValidatorException.class, () -> val.validate("12345"));
        assertDoesNotThrow(() -> val.validate("-+12347890"));
    }
}