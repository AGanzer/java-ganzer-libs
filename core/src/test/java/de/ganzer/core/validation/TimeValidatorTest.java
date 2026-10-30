package de.ganzer.core.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.junit.jupiter.api.Assertions.*;

class TimeValidatorTest {

    @BeforeAll
    static void initDefaultLocale() {
        Locale.setDefault(GERMANY);
    }

    @Test
    void constructEmpty() {
        var val = new TimeValidator();

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(LocalTime.MIN, val.getMinTime());
        assertEquals(LocalTime.MAX, val.getMaxTime());
        assertEquals(FormatStyle.SHORT, val.getFormatStyle());
        assertNull(val.getPatternString());
        assertNull(val.getLocale());
        assertEquals(TimeValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());
        assertEquals(TimeValidator.DEFAULT_TIME_ERROR_MESSAGE, val.getTimeErrorMessage());
        assertNull(val.getPicture());
    }

    @Test
    void constructWithOptions() {
        var val = new TimeValidator(ValidatorOptions.NONE);

        assertEquals(ValidatorOptions.NONE, val.getOptions());
        assertEquals(LocalTime.MIN, val.getMinTime());
        assertEquals(LocalTime.MAX, val.getMaxTime());
        assertEquals(FormatStyle.SHORT, val.getFormatStyle());
        assertNull(val.getPatternString());
        assertNull(val.getLocale());
    }

    @Test
    void constructWithTimes() {
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);
        var val = new TimeValidator(min, max);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(min, val.getMinTime());
        assertEquals(max, val.getMaxTime());
        assertNull(val.getPatternString());
    }

    @Test
    void constructWithTimesNull() {
        var val = new TimeValidator(null, null);

        assertEquals(LocalTime.MIN, val.getMinTime());
        assertEquals(LocalTime.MAX, val.getMaxTime());
    }

    @Test
    void constructWithOptionsAndTimes() {
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);
        var val = new TimeValidator(ValidatorOptions.AUTO_FILL, min, max);

        assertEquals(ValidatorOptions.AUTO_FILL, val.getOptions());
        assertEquals(min, val.getMinTime());
        assertEquals(max, val.getMaxTime());
        assertNull(val.getPatternString());
    }

    @Test
    void constructIllegalTimes() {
        var min = LocalTime.of(18, 0);
        var max = LocalTime.of(8, 0);

        assertThrows(IllegalArgumentException.class, () -> new TimeValidator(min, max));
        assertThrows(IllegalArgumentException.class, () -> new TimeValidator(ValidatorOptions.NONE, min, max));
    }

    @Test
    void setMinTime() {
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);
        var val = new TimeValidator(min, max);

        var newMin = LocalTime.of(9, 30);
        val.setMinTime(newMin);
        assertEquals(newMin, val.getMinTime());
        assertEquals(max, val.getMaxTime());

        val.setMinTime(null);
        assertEquals(LocalTime.MIN, val.getMinTime());
    }

    @Test
    void setMinTimeGreaterThanMax() {
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);
        var val = new TimeValidator(min, max);

        var newMin = LocalTime.of(20, 0);
        val.setMinTime(newMin);
        assertEquals(newMin, val.getMinTime());
        assertEquals(newMin, val.getMaxTime());
    }

    @Test
    void setMaxTime() {
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);
        var val = new TimeValidator(min, max);

        var newMax = LocalTime.of(17, 0);
        val.setMaxTime(newMax);
        assertEquals(min, val.getMinTime());
        assertEquals(newMax, val.getMaxTime());

        val.setMaxTime(null);
        assertEquals(LocalTime.MAX, val.getMaxTime());
    }

    @Test
    void setMaxTimeLessThanMin() {
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);
        var val = new TimeValidator(min, max);

        var newMax = LocalTime.of(6, 0);
        val.setMaxTime(newMax);
        assertEquals(min, val.getMinTime());
        assertEquals(min, val.getMaxTime());
    }

    @Test
    void setRange() {
        var val = new TimeValidator();
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);

        val.setRange(min, max);
        assertEquals(min, val.getMinTime());
        assertEquals(max, val.getMaxTime());

        val.setRange(null, null);
        assertEquals(LocalTime.MIN, val.getMinTime());
        assertEquals(LocalTime.MAX, val.getMaxTime());
    }

    @Test
    void setRangeIllegal() {
        var val = new TimeValidator();
        var min = LocalTime.of(18, 0);
        var max = LocalTime.of(8, 0);

        assertThrows(IllegalArgumentException.class, () -> val.setRange(min, max));
    }

    @Test
    void setFormatStyle() {
        var val = new TimeValidator();
        val.setFormatStyle(FormatStyle.MEDIUM);

        assertEquals(FormatStyle.MEDIUM, val.getFormatStyle());
        assertNotNull(val.getPicture());
    }

    @Test
    void setPatternString() {
        var val = new TimeValidator();
        val.setPatternString("HH:mm:ss");

        assertEquals("HH:mm:ss", val.getPatternString());
        assertEquals("##:##:##", val.getPicture());

        val.setPatternString(null);
        assertNull(val.getPatternString());
        assertNotNull(val.getPicture());
    }

    @Test
    void setLocale() {
        var val = new TimeValidator();
        val.setLocale(US);

        assertEquals(US, val.getLocale());
        assertNotNull(val.getPicture());

        val.setLocale(null);
        assertNull(val.getLocale());
    }

    @Test
    void setRangeErrorMessage() {
        var val = new TimeValidator();
        val.setRangeErrorMessage("Custom range error: %1$s to %2$s");
        assertEquals("Custom range error: %1$s to %2$s", val.getRangeErrorMessage());

        val.setRangeErrorMessage(null);
        assertEquals(TimeValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());

        val.setRangeErrorMessage("");
        assertEquals(TimeValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());

        val.setRangeErrorMessage("   ");
        assertEquals(TimeValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());
    }

    @Test
    void setTimeErrorMessage() {
        var val = new TimeValidator();
        val.setTimeErrorMessage("Custom time error");
        assertEquals("Custom time error", val.getTimeErrorMessage());

        val.setTimeErrorMessage(null);
        assertEquals(TimeValidator.DEFAULT_TIME_ERROR_MESSAGE, val.getTimeErrorMessage());

        val.setTimeErrorMessage("");
        assertEquals(TimeValidator.DEFAULT_TIME_ERROR_MESSAGE, val.getTimeErrorMessage());

        val.setTimeErrorMessage("   ");
        assertEquals(TimeValidator.DEFAULT_TIME_ERROR_MESSAGE, val.getTimeErrorMessage());
    }

    @Test
    void doValidateSuccess() {
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);
        var val = new TimeValidator(min, max);
        val.setPatternString("HH:mm");

        assertDoesNotThrow(() -> val.validate("08:00"));
        assertDoesNotThrow(() -> val.validate("12:30"));
        assertDoesNotThrow(() -> val.validate("18:00"));
    }

    @Test
    void doValidateInvalidTimeSyntax() {
        var val = new TimeValidator();
        val.setPatternString("HH:mm");

        var ref = new ValidatorExceptionRef();
        assertFalse(val.validate("25:00", ref));
        assertNotNull(ref.getException());
        assertEquals(val.getTimeErrorMessage(), ref.getException().getMessage());
        assertEquals(TimeValidator.class, ref.getException().getSourceClass());
        assertSame(val, ref.getException().getSource());

        assertThrows(ValidatorException.class, () -> val.validate("12:65"));
        assertThrows(ValidatorException.class, () -> val.validate("abc"));
    }

    @Test
    void doValidateOutOfRange() {
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);
        var pattern = "HH:mm";
        var val = new TimeValidator(min, max);
        val.setPatternString(pattern);

        var ref = new ValidatorExceptionRef();
        assertFalse(val.validate("07:59", ref));
        assertNotNull(ref.getException());

        var formatter = DateTimeFormatter.ofPattern(pattern, GERMANY);
        var expectedMessage = String.format(GERMANY, val.getRangeErrorMessage(),
                                            formatter.format(min),
                                            formatter.format(max));
        assertEquals(expectedMessage, ref.getException().getMessage());

        assertThrows(ValidatorException.class, () -> val.validate("18:01"));
    }

    @Test
    void doValidateWithCustomLocale() {
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);
        var val = new TimeValidator(min, max);
        val.setLocale(US);
        val.setFormatStyle(FormatStyle.SHORT);

        var formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(US);
        var validText = formatter.format(LocalTime.of(14, 30));

        assertDoesNotThrow(() -> val.validate(validText));
    }

    @Test
    void doValidateWithGeneralErrorMessage() {
        var min = LocalTime.of(8, 0);
        var max = LocalTime.of(18, 0);
        var val = new TimeValidator(min, max);
        val.setPatternString("HH:mm");
        val.setErrorMessage("Global time error message");

        var ref = new ValidatorExceptionRef();
        assertFalse(val.validate("07:00", ref));
        assertEquals("Global time error message", ref.getException().getMessage());

        var ref2 = new ValidatorExceptionRef();
        assertFalse(val.validate("invalid", ref2));
        assertEquals("Global time error message", ref2.getException().getMessage());
    }

    @Test
    void doValidateEmptyText() {
        var valRequired = new TimeValidator();
        assertThrows(ValidatorException.class, () -> valRequired.validate(""));

        var valOptional = new TimeValidator(ValidatorOptions.NONE);
        assertDoesNotThrow(() -> valOptional.validate(""));
    }

    @Test
    void doInputValidationAutoFill() {
        var val = new TimeValidator();
        val.setPatternString("HH:mm");

        var input = new StringBuilder("12");
        assertTrue(val.isValidInput(input, true));
        assertEquals("12:", input.toString());

        var inputNoAutoFill = new StringBuilder("12");
        assertTrue(val.isValidInput(inputNoAutoFill, false));
        assertEquals("12", inputNoAutoFill.toString());

        var invalidInput = new StringBuilder("abc");
        assertFalse(val.isValidInput(invalidInput, true));
    }
}
