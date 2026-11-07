package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyValidatorTest {
    @Test
    void create() {
        var val = new CurrencyValidator();

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertNull(val.getCurrencySymbol());
        assertNull(val.getLocale());
        assertFalse(val.isHideCurrencySymbol());
        assertEquals((double) Long.MIN_VALUE, val.getMinValue());
        assertEquals((double) Long.MAX_VALUE, val.getMaxValue());
        assertEquals(CurrencyValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());
        assertEquals(CurrencyValidator.DEFAULT_NUMBER_ERROR_MESSAGE, val.getNumberErrorMessage());
    }

    @Test
    void createRange() {
        var min = 10.0;
        var max = 20.0;
        var val = new CurrencyValidator(min, max);

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertNull(val.getCurrencySymbol());
        assertNull(val.getLocale());
        assertFalse(val.isHideCurrencySymbol());
        assertEquals(min, val.getMinValue());
        assertEquals(max, val.getMaxValue());
        assertEquals(CurrencyValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());
        assertEquals(CurrencyValidator.DEFAULT_NUMBER_ERROR_MESSAGE, val.getNumberErrorMessage());
    }

    @Test
    void createInvalidRange() {
        var min = 30.0;
        var max = 20.0;

        assertThrows(IllegalArgumentException.class, () -> new CurrencyValidator(min, max));
    }

    @Test
    void createRangeAndHide() {
        var min = 10.0;
        var max = 20.0;
        var val = new CurrencyValidator(min, max, true);

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertNull(val.getCurrencySymbol());
        assertNull(val.getLocale());
        assertTrue(val.isHideCurrencySymbol());
        assertEquals(min, val.getMinValue());
        assertEquals(max, val.getMaxValue());
        assertEquals(CurrencyValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());
        assertEquals(CurrencyValidator.DEFAULT_NUMBER_ERROR_MESSAGE, val.getNumberErrorMessage());
    }

    @Test
    void createRangeAndOptions() {
        var min = 10.0;
        var max = 20.0;
        var options = ValidatorOptions.NONE;
        var val = new CurrencyValidator(options, min, max);

        assertEquals(ValidatorOptions.NONE, val.getOptions());
        assertNull(val.getCurrencySymbol());
        assertNull(val.getLocale());
        assertFalse(val.isHideCurrencySymbol());
        assertEquals(min, val.getMinValue());
        assertEquals(max, val.getMaxValue());
        assertEquals(CurrencyValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());
        assertEquals(CurrencyValidator.DEFAULT_NUMBER_ERROR_MESSAGE, val.getNumberErrorMessage());
    }

    @Test
    void createRangeAndHideAndOptions() {
        var min = 10.0;
        var max = 20.0;
        var options = ValidatorOptions.NONE;
        var val = new CurrencyValidator(options, min, max, true);

        assertEquals(ValidatorOptions.NONE, val.getOptions());
        assertNull(val.getCurrencySymbol());
        assertNull(val.getLocale());
        assertTrue(val.isHideCurrencySymbol());
        assertEquals(min, val.getMinValue());
        assertEquals(max, val.getMaxValue());
        assertEquals(CurrencyValidator.DEFAULT_RANGE_ERROR_MESSAGE, val.getRangeErrorMessage());
        assertEquals(CurrencyValidator.DEFAULT_NUMBER_ERROR_MESSAGE, val.getNumberErrorMessage());
    }

    @Test
    void setRangeErrorMessage() {
        var  val = new CurrencyValidator();
        String msg = "my message";
        val.setRangeErrorMessage(msg);
        assertEquals(msg, val.getRangeErrorMessage());
    }

    @Test
    void setNumberErrorMessage() {
        var val = new CurrencyValidator();
        String msg = "my message";
        val.setNumberErrorMessage(msg);
        assertEquals(msg, val.getNumberErrorMessage());
    }

    @Test
    void setMinValue() {
        var val = new CurrencyValidator();
        double min = 10;
        val.setMinValue(min);
        assertEquals(min, val.getMinValue());
    }

    @Test
    void setMinValueAdjustMax() {
        var val = new CurrencyValidator(10.0, 20.0);
        double min = 30;
        val.setMinValue(min);
        assertEquals(min, val.getMinValue());
        assertEquals(min, val.getMaxValue());
    }

    @Test
    void setMaxValue() {
        var val = new CurrencyValidator();
        double max = 10;
        val.setMaxValue(max);
        assertEquals(max, val.getMaxValue());
    }

    @Test
    void setMaxValueAdjustMin() {
        var val = new CurrencyValidator(10, 20);
        double max = 5;
        val.setMaxValue(max);
        assertEquals(max, val.getMinValue());
        assertEquals(max, val.getMaxValue());
    }

    @Test
    void setHideCurrencySymbol() {
        var val = new CurrencyValidator();
        boolean hide = true;
        val.setHideCurrencySymbol(hide);
        assertEquals(hide, val.isHideCurrencySymbol());
    }

    @Test
    void setCurrencySymbol() {
        var val = new CurrencyValidator();
        String symbol = "€";
        val.setCurrencySymbol(symbol);
        assertEquals(symbol, val.getCurrencySymbol());
    }

    @Test
    void setRange() {
        var val = new CurrencyValidator();
        double min = 10;
        double max = 20;
        val.setRange(min, max);
        assertEquals(min, val.getMinValue());
        assertEquals(max, val.getMaxValue());
    }

    @Test
    void setRangeInvalid() {
        var val = new CurrencyValidator();
        double min = 30;
        double max = 20;

        assertThrows(IllegalArgumentException.class, () -> val.setRange(min, max));
    }

    @Test
    void setLocale() {
        var val = new CurrencyValidator();
        Locale locale = Locale.GERMAN;
        val.setLocale(locale);
        assertEquals(locale, val.getLocale());
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "0,4;true",
            "1000,04;true",
            "0.;false",
            "10€;false"})
    void doInputValidation(String input, String result) {
        var res = Boolean.parseBoolean(result);
        var val = new CurrencyValidator();
        val.setLocale(Locale.GERMAN);

        assertEquals(res, val.isValidInput(new StringBuilder(input), false));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "0,4;true",
            "1000,04;true",
            "-1000,04;false",
            "0.;false",
            "10€;false",
            "0,40 €;true",
            "1.000,04 €;true",
            "-1.000,04 €;false"})
    void doValidate(String input, String result) {
        var res = Boolean.parseBoolean(result);
        var val = new CurrencyValidator(0, 1000);
        val.setLocale(Locale.GERMAN);

        assertEquals(res, val.validate(input, new ValidatorExceptionRef()));
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "0,4;0,40 €;true",
            "1000,04;1.000,04 €;true",
            "-1.000,40 €;-1000,40;false",
            "10,00 €;10,00;false"})
    void doFormatText(String input, String result, String display) {
        var format = Boolean.parseBoolean(display) ? TextFormat.DISPLAY : TextFormat.EDIT;
        var val = new CurrencyValidator(0, 1000);
        val.setLocale(Locale.GERMAN);

        assertEquals(result, val.formatText(input, format));
    }
}