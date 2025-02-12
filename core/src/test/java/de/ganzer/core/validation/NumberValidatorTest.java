package de.ganzer.core.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static java.util.Locale.*;
import static org.junit.jupiter.api.Assertions.*;

class NumberValidatorTest {
    @BeforeAll
    static void setLocale() {
        Locale.setDefault(GERMANY);
    }

    @Test
    void constructEmpty() {
        NumberValidator val = new NumberValidator();

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(Long.MIN_VALUE, val.getMinValue());
        assertEquals(Long.MAX_VALUE, val.getMaxValue());
        assertEquals(0, val.getNumDecimals());
        assertNull(val.getDisplayFormat());
        assertNull(val.getEditFormat());
    }

    @Test
    void constructWithOption() {
        NumberValidator val = new NumberValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(Long.MIN_VALUE, val.getMinValue());
        assertEquals(Long.MAX_VALUE, val.getMaxValue());
        assertEquals(0, val.getNumDecimals());
        assertNull(val.getDisplayFormat());
        assertNull(val.getEditFormat());
    }

    @Test
    void constructWithValues() {
        double min = -100.0;
        double max = 100.0;
        NumberValidator val = new NumberValidator(min, max);

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(min, val.getMinValue());
        assertEquals(max, val.getMaxValue());
        assertEquals(0, val.getNumDecimals());
        assertNull(val.getDisplayFormat());
        assertNull(val.getEditFormat());
    }

    @Test
    void constructWithValuesAndOption() {
        double min = -100.0;
        double max = 100.0;
        NumberValidator val = new NumberValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, min, max);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(min, val.getMinValue());
        assertEquals(max, val.getMaxValue());
        assertEquals(0, val.getNumDecimals());
        assertNull(val.getDisplayFormat());
        assertNull(val.getEditFormat());
    }

    @Test
    void setMinValue() {
        double min = -100.0;
        double max = 100.0;
        double newMin = 0.0;
        NumberValidator val = new NumberValidator(min, max);

        val.setMinValue(newMin);
        assertEquals(newMin, val.getMinValue());
        assertEquals(max, val.getMaxValue());
    }

    @Test
    void setMinValueGreaterThanMax() {
        double min = -100.0;
        double max = 100.0;
        double newMin = 200.0;
        NumberValidator val = new NumberValidator(min, max);

        val.setMinValue(newMin);
        assertEquals(newMin, val.getMinValue());
        assertEquals(newMin, val.getMaxValue());
    }

    @Test
    void setMaxValue() {
        double min = -100.0;
        double max = 100.0;
        double newMax = 0.0;
        NumberValidator val = new NumberValidator(min, max);

        val.setMaxValue(newMax);
        assertEquals(min, val.getMinValue());
        assertEquals(newMax, val.getMaxValue());
    }

    @Test
    void setMaxValueLessThanMin() {
        double min = -100.0;
        double max = 100.0;
        double newMax = -200.0;
        NumberValidator val = new NumberValidator(min, max);

        val.setMaxValue(newMax);
        assertEquals(newMax, val.getMinValue());
        assertEquals(newMax, val.getMaxValue());
    }

    @Test
    void setRange() {
        NumberValidator val = new NumberValidator();
        double min = -100.0;
        double max = 100.0;

        val.setRange(min, max);

        assertEquals(min, val.getMinValue());
        assertEquals(max, val.getMaxValue());
    }

    @Test
    void setRangeIllegal() {
        NumberValidator val = new NumberValidator();
        double min = -100.0;
        double max = 100.0;

        assertThrows(IllegalArgumentException.class, () -> val.setRange(max, min));
    }

    @Test
    void setNumDecimals() {
        NumberValidator val = new NumberValidator();

        val.setNumDecimals(2);
        assertEquals(2, val.getNumDecimals());
    }

    @Test
    void setDisplayFormat() {
        String format = "%.2f";
        NumberValidator val = new NumberValidator();

        val.setDisplayFormat(format);
        assertEquals(format, val.getDisplayFormat());
        assertEquals("12345,00", val.formatText("12345", TextFormat.DISPLAY));

        val.setDisplayFormat("");
        assertNull(val.getDisplayFormat());
    }

    @Test
    void setEditFormat() {
        String format = "%.2f";
        NumberValidator val = new NumberValidator();

        val.setEditFormat(format);
        assertEquals(format, val.getEditFormat());
        assertEquals("12345,00", val.formatText("12345", TextFormat.EDIT));

        val.setEditFormat("");
        assertNull(val.getEditFormat());
    }

    @Test
    void doInputValidation() {
        double min = -100.0;
        double max = 100.0;
        NumberValidator val = new NumberValidator(min, max);

        assertTrue(val.isValidInput(new StringBuilder("-100"), false));
        assertTrue(val.isValidInput(new StringBuilder("100"), false));
        assertTrue(val.isValidInput(new StringBuilder("-"), false));
        assertFalse(val.isValidInput(new StringBuilder("+"), false));
        assertFalse(val.isValidInput(new StringBuilder("a"), false));
    }

    @Test
    void doInputValidationNegativeOnly() {
        double min = -100.0;
        double max = -50.0;
        NumberValidator val = new NumberValidator(min, max);

        assertTrue(val.isValidInput(new StringBuilder("-"), false));
        assertFalse(val.isValidInput(new StringBuilder("+"), false));
        assertTrue(val.isValidInput(new StringBuilder("-100"), false));
    }

    @Test
    void doInputValidationPositiveOnly() {
        double min = 0.0;
        double max = 100.0;
        NumberValidator val = new NumberValidator(min, max);

        assertFalse(val.isValidInput(new StringBuilder("+"), false));
        assertFalse(val.isValidInput(new StringBuilder("-"), false));
        assertTrue(val.isValidInput(new StringBuilder("100"), false));
    }

    @Test
    void doInputValidationDecimalsAllowed() {
        double min = 0.0;
        double max = 100.0;
        NumberValidator val = new NumberValidator(min, max);

        val.setNumDecimals(1);
        assertTrue(val.isValidInput(new StringBuilder("100,0"), false));
        assertTrue(val.isValidInput(new StringBuilder(",1"), false));
    }

    @Test
    void doValidate() {
        double min = -100.0;
        double max = 100.0;
        NumberValidator val = new NumberValidator(min, max);

        assertDoesNotThrow(() -> val.validate("-100"));
        assertDoesNotThrow(() -> val.validate("100"));
        assertThrows(ValidatorException.class, () -> val.validate("-"));
        assertThrows(ValidatorException.class, () -> val.validate("+"));
        assertThrows(ValidatorException.class, () -> val.validate("-101"));
        assertThrows(ValidatorException.class, () -> val.validate("101"));
        assertThrows(ValidatorException.class, () -> val.validate("1,1"));
        assertThrows(ValidatorException.class, () -> val.validate(",1"));
    }

    @Test
    void doValidateNegativeOnly() {
        double min = -100.0;
        double max = -50.0;
        NumberValidator val = new NumberValidator(min, max);

        assertDoesNotThrow(() -> val.validate("-100"));
        assertThrows(ValidatorException.class, () -> val.validate("101"));
    }

    @Test
    void doValidatePositiveOnly() {
        double min = 0.0;
        double max = 50.0;
        NumberValidator val = new NumberValidator(min, max);

        assertDoesNotThrow(() -> val.validate("50"));
        assertThrows(ValidatorException.class, () -> val.validate("-1"));
    }

    @Test
    void doValidateDecimalsAllowed() {
        double min = 0.0;
        double max = 50.0;
        NumberValidator val = new NumberValidator(min, max);

        val.setNumDecimals(1);
        assertDoesNotThrow(() -> val.validate("5,1"));
        assertDoesNotThrow(() -> val.validate(",1"));
    }

    @Test
    void doFormatTextDisplay() {
        NumberValidator val = new NumberValidator();

        assertEquals("12.345", val.formatText("12345", TextFormat.DISPLAY));
        assertEquals("12.456", val.formatText("12456,01", TextFormat.DISPLAY));
    }

    @Test
    void doFormatTextEdit() {
        NumberValidator val = new NumberValidator();

        assertEquals("12345", val.formatText("12.345", TextFormat.EDIT));
        assertEquals("12456", val.formatText("12.456,01", TextFormat.EDIT));
    }

    @Test
    void doFormatTextDisplayWithDecimals() {
        NumberValidator val = new NumberValidator();

        val.setNumDecimals(2);
        assertEquals("12.345,00", val.formatText("12345", TextFormat.DISPLAY));
        assertEquals("12.456,01", val.formatText("12456,01", TextFormat.DISPLAY));
    }

    @Test
    void doFormatTextEditWithDecimals() {
        NumberValidator val = new NumberValidator();

        val.setNumDecimals(2);
        assertEquals("12345", val.formatText("12.345", TextFormat.EDIT));
        assertEquals("12456,01", val.formatText("12.456,01", TextFormat.EDIT));
    }
}