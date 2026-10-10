package de.ganzer.core.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.junit.jupiter.api.Assertions.*;

class DateValidatorTest {

    @BeforeAll
    static void initDefaultLocale() {
        Locale.setDefault(GERMANY);
    }

    @Test
    void constructEmpty() {
        var val = new DateValidator();

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(LocalDate.MIN, val.getMinDate());
        assertEquals(LocalDate.MAX, val.getMaxDate());
        assertEquals(FormatStyle.SHORT, val.getFormatStyle());
        assertNull(val.getPatternString());
        assertNull(val.getLocale());
        assertEquals(DateValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());
        assertEquals(DateValidator.DEFAULT_DATE_ERROR_MESSAGE, val.getDateErrorMessage());
        assertNull(val.getPicture());
    }

    @Test
    void constructWithOptions() {
        var val = new DateValidator(ValidatorOptions.NONE);

        assertEquals(ValidatorOptions.NONE, val.getOptions());
        assertEquals(LocalDate.MIN, val.getMinDate());
        assertEquals(LocalDate.MAX, val.getMaxDate());
        assertEquals(FormatStyle.SHORT, val.getFormatStyle());
        assertNull(val.getPatternString());
        assertNull(val.getLocale());
    }

    @Test
    void constructWithDates() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var val = new DateValidator(min, max);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(min, val.getMinDate());
        assertEquals(max, val.getMaxDate());
        assertNull(val.getPatternString());
    }

    @Test
    void constructWithDatesNull() {
        var val = new DateValidator(null, null);

        assertEquals(LocalDate.MIN, val.getMinDate());
        assertEquals(LocalDate.MAX, val.getMaxDate());
    }

    @Test
    void constructWithDatesAndPattern() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var pattern = "yyyy-MM-dd";
        var val = new DateValidator(min, max, pattern);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(min, val.getMinDate());
        assertEquals(max, val.getMaxDate());
        assertEquals(pattern, val.getPatternString());
        assertEquals("####-##-##", val.getPicture());
    }

    @Test
    void constructWithOptionsAndDates() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var val = new DateValidator(ValidatorOptions.AUTO_FILL, min, max);

        assertEquals(ValidatorOptions.AUTO_FILL, val.getOptions());
        assertEquals(min, val.getMinDate());
        assertEquals(max, val.getMaxDate());
        assertNull(val.getPatternString());
    }

    @Test
    void constructWithOptionsDatesAndPattern() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var pattern = "dd.MM.yyyy";
        var val = new DateValidator(ValidatorOptions.AUTO_FILL, min, max, pattern);

        assertEquals(ValidatorOptions.AUTO_FILL, val.getOptions());
        assertEquals(min, val.getMinDate());
        assertEquals(max, val.getMaxDate());
        assertEquals(pattern, val.getPatternString());
    }

    @Test
    void constructIllegalDates() {
        var min = LocalDate.of(2025, 1, 1);
        var max = LocalDate.of(2020, 1, 1);

        assertThrows(IllegalArgumentException.class, () -> new DateValidator(min, max));
        assertThrows(IllegalArgumentException.class, () -> new DateValidator(min, max, "yyyy-MM-dd"));
        assertThrows(IllegalArgumentException.class, () -> new DateValidator(ValidatorOptions.NONE, min, max));
        assertThrows(IllegalArgumentException.class, () -> new DateValidator(ValidatorOptions.NONE, min, max, "yyyy-MM-dd"));
    }

    @Test
    void setMinDate() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var val = new DateValidator(min, max);

        var newMin = LocalDate.of(2021, 5, 1);
        val.setMinDate(newMin);
        assertEquals(newMin, val.getMinDate());
        assertEquals(max, val.getMaxDate());

        val.setMinDate(null);
        assertEquals(LocalDate.MIN, val.getMinDate());
    }

    @Test
    void setMinDateGreaterThanMax() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var val = new DateValidator(min, max);

        var newMin = LocalDate.of(2026, 1, 1);
        val.setMinDate(newMin);
        assertEquals(newMin, val.getMinDate());
        assertEquals(newMin, val.getMaxDate());
    }

    @Test
    void setMaxDate() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var val = new DateValidator(min, max);

        var newMax = LocalDate.of(2024, 6, 30);
        val.setMaxDate(newMax);
        assertEquals(min, val.getMinDate());
        assertEquals(newMax, val.getMaxDate());

        val.setMaxDate(null);
        assertEquals(LocalDate.MAX, val.getMaxDate());
    }

    @Test
    void setMaxDateLessThanMin() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var val = new DateValidator(min, max);

        var newMax = LocalDate.of(2019, 1, 1);
        val.setMaxDate(newMax);
        assertEquals(min, val.getMinDate());
        assertEquals(min, val.getMaxDate());
    }

    @Test
    void setRange() {
        var val = new DateValidator();
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);

        val.setRange(min, max);
        assertEquals(min, val.getMinDate());
        assertEquals(max, val.getMaxDate());

        val.setRange(null, null);
        assertEquals(LocalDate.MIN, val.getMinDate());
        assertEquals(LocalDate.MAX, val.getMaxDate());
    }

    @Test
    void setRangeIllegal() {
        var val = new DateValidator();
        var min = LocalDate.of(2025, 1, 1);
        var max = LocalDate.of(2020, 1, 1);

        assertThrows(IllegalArgumentException.class, () -> val.setRange(min, max));
    }

    @Test
    void setFormatStyle() {
        var val = new DateValidator();
        val.setFormatStyle(FormatStyle.MEDIUM);

        assertEquals(FormatStyle.MEDIUM, val.getFormatStyle());
        assertNotNull(val.getPicture());
    }

    @Test
    void setPatternString() {
        var val = new DateValidator();
        val.setPatternString("yyyy/MM/dd");

        assertEquals("yyyy/MM/dd", val.getPatternString());
        assertEquals("####/##/##", val.getPicture());

        val.setPatternString(null);
        assertNull(val.getPatternString());
        assertNotNull(val.getPicture());
    }

    @Test
    void setLocale() {
        var val = new DateValidator();
        val.setLocale(US);

        assertEquals(US, val.getLocale());
        assertNotNull(val.getPicture());

        val.setLocale(null);
        assertNull(val.getLocale());
    }

    @Test
    void setRangeErrorMessage() {
        var val = new DateValidator();
        val.setRangeErrorMessage("Custom range error: %1$s to %2$s");
        assertEquals("Custom range error: %1$s to %2$s", val.getRangeErrorMessage());

        val.setRangeErrorMessage(null);
        assertEquals(DateValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());

        val.setRangeErrorMessage("");
        assertEquals(DateValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());

        val.setRangeErrorMessage("   ");
        assertEquals(DateValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());
    }

    @Test
    void setDateErrorMessage() {
        var val = new DateValidator();
        val.setDateErrorMessage("Custom date error");
        assertEquals("Custom date error", val.getDateErrorMessage());

        val.setDateErrorMessage(null);
        assertEquals(DateValidator.DEFAULT_DATE_ERROR_MESSAGE, val.getDateErrorMessage());

        val.setDateErrorMessage("");
        assertEquals(DateValidator.DEFAULT_DATE_ERROR_MESSAGE, val.getDateErrorMessage());

        val.setDateErrorMessage("   ");
        assertEquals(DateValidator.DEFAULT_DATE_ERROR_MESSAGE, val.getDateErrorMessage());
    }

    @Test
    void doValidateSuccess() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var pattern = "dd.MM.yyyy";
        var val = new DateValidator(min, max, pattern);

        assertDoesNotThrow(() -> val.validate("01.01.2020"));
        assertDoesNotThrow(() -> val.validate("15.06.2022"));
        assertDoesNotThrow(() -> val.validate("31.12.2025"));
    }

    @Test
    void doValidateInvalidDateSyntax() {
        var pattern = "dd.MM.yyyy";
        var val = new DateValidator(LocalDate.MIN, LocalDate.MAX, pattern);

        var ref = new ValidatorExceptionRef();
        assertFalse(val.validate("32.01.2024", ref));
        assertNotNull(ref.getException());
        assertEquals(val.getDateErrorMessage(), ref.getException().getMessage());
        assertEquals(DateValidator.class, ref.getException().getSourceClass());
        assertSame(val, ref.getException().getSource());

        assertThrows(ValidatorException.class, () -> val.validate("99.99.9999"));
        assertThrows(ValidatorException.class, () -> val.validate("abc"));
    }

    @Test
    void doValidateOutOfRange() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var pattern = "dd.MM.yyyy";
        var val = new DateValidator(min, max, pattern);

        var ref = new ValidatorExceptionRef();
        assertFalse(val.validate("31.12.2019", ref));
        assertNotNull(ref.getException());

        var formatter = DateTimeFormatter.ofPattern(pattern, GERMANY);
        var expectedMessage = String.format(GERMANY, val.getRangeErrorMessage(),
                                            formatter.format(min),
                                            formatter.format(max));
        assertEquals(expectedMessage, ref.getException().getMessage());

        assertThrows(ValidatorException.class, () -> val.validate("01.01.2026"));
    }

    @Test
    void doValidateWithCustomLocale() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var val = new DateValidator(min, max);
        val.setLocale(US);
        val.setFormatStyle(FormatStyle.SHORT);

        var formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(US);
        var validText = formatter.format(LocalDate.of(2022, 5, 20));

        assertDoesNotThrow(() -> val.validate(validText));
    }

    @Test
    void doValidateWithGeneralErrorMessage() {
        var min = LocalDate.of(2020, 1, 1);
        var max = LocalDate.of(2025, 12, 31);
        var pattern = "dd.MM.yyyy";
        var val = new DateValidator(min, max, pattern);
        val.setErrorMessage("Global error message");

        var ref = new ValidatorExceptionRef();
        assertFalse(val.validate("31.12.2019", ref));
        assertEquals("Global error message", ref.getException().getMessage());

        var ref2 = new ValidatorExceptionRef();
        assertFalse(val.validate("invalid", ref2));
        assertEquals("Global error message", ref2.getException().getMessage());
    }

    @Test
    void doValidateEmptyText() {
        var valRequired = new DateValidator();
        assertThrows(ValidatorException.class, () -> valRequired.validate(""));

        var valOptional = new DateValidator(ValidatorOptions.NONE);
        assertDoesNotThrow(() -> valOptional.validate(""));
    }

    @Test
    void doInputValidationAutoFill() {
        var val = new DateValidator(LocalDate.MIN, LocalDate.MAX, "dd.MM.yyyy");

        var input = new StringBuilder("12");
        assertTrue(val.isValidInput(input, true));
        assertEquals("12.", input.toString());

        var inputNoAutoFill = new StringBuilder("12");
        assertTrue(val.isValidInput(inputNoAutoFill, false));
        assertEquals("12", inputNoAutoFill.toString());

        var invalidInput = new StringBuilder("abc");
        assertFalse(val.isValidInput(invalidInput, true));
    }
}
