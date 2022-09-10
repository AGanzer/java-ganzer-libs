package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharCountValidatorTest {
    @Test
    void constructEmpty() {
        var val = new CharCountValidator();

        assertNull(val.getErrorMessage());
        assertNull(val.getTag());
        assertEquals(0, val.getMinLength());
        assertEquals(0, val.getMaxLength());
        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
    }

    @Test
    void constructWithOption() {
        var val = new CharCountValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT);

        assertNull(val.getErrorMessage());
        assertNull(val.getTag());
        assertEquals(0, val.getMinLength());
        assertEquals(0, val.getMaxLength());
        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
    }

    @Test
    void setOptions() {
        var val = new CharCountValidator();
        val.setOptions(ValidatorOptions.AUTO_FILL);

        assertEquals(ValidatorOptions.AUTO_FILL, val.getOptions());
    }

    @Test
    void setMaxLength() {
        var val = new CharCountValidator();
        val.setMinLength(0);
        val.setMaxLength(10);

        assertEquals(10, val.getMaxLength());
        assertEquals(0, val.getMinLength());
    }

    @Test
    void setMaxLess() {
        var val = new CharCountValidator();
        val.setMinLength(10);
        val.setMaxLength(5);

        assertEquals(5, val.getMaxLength());
        assertEquals(5, val.getMinLength());
    }

    @Test
    void setMinLength() {
        var val = new CharCountValidator();
        val.setMaxLength(10);
        val.setMinLength(0);

        assertEquals(0, val.getMinLength());
        assertEquals(10, val.getMaxLength());
    }

    @Test
    void setMinGreat() {
        var val = new CharCountValidator();
        val.setMaxLength(10);
        val.setMinLength(20);

        assertEquals(20, val.getMinLength());
        assertEquals(20, val.getMaxLength());
    }

    @Test
    void setMaxLengthAdjustMinLength() {
        var val = new CharCountValidator();
        val.setMinLength(20);
        val.setMaxLength(10);

        assertEquals(10, val.getMaxLength());
        assertEquals(10, val.getMinLength());
    }

    @Test
    void setMinLengthAdjustMaxLength() {
        var val = new CharCountValidator();
        val.setMaxLength(10);
        val.setMinLength(20);

        assertEquals(20, val.getMinLength());
        assertEquals(20, val.getMaxLength());
    }

    @Test
    void setErrorMessage() {
        var m = "New Message!";
        var val = new CharCountValidator();
        val.setErrorMessage(m);

        assertEquals(m, val.getErrorMessage());
    }

    @Test
    void setErrorMessageNull() {
        var m = "New Message!";
        var val = new CharCountValidator();
        val.setErrorMessage(m);
        val.setErrorMessage(null);

        assertNull(val.getErrorMessage());
    }

    @Test
    void setErrorMessageEmpty() {
        var m = "New Message!";
        var val = new CharCountValidator();
        val.setErrorMessage(m);
        val.setErrorMessage("");

        assertNull(val.getErrorMessage());
    }

    @Test
    void setErrorMessageBlank() {
        var m = "New Message!";
        var val = new CharCountValidator();
        val.setErrorMessage(m);
        val.setErrorMessage("   ");

        assertNull(val.getErrorMessage());
    }

    @Test
    void setTag() {
        var o = new Object();
        var val = new CharCountValidator();
        val.setTag(o);

        assertEquals(o, val.getTag());
    }

    @Test
    void hasOption() {
        var val = new CharCountValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT);

        assertTrue(val.hasOption(ValidatorOptions.NEEDS_INPUT));
        assertTrue(val.hasOption(ValidatorOptions.AUTO_FILL));
        assertTrue(val.hasOption(ValidatorOptions.NEEDS_INPUT | ValidatorOptions.BLANKS_VALID));
        assertFalse(val.hasOption(ValidatorOptions.BLANKS_VALID));
    }

    @Test
    void isInputValidEmpty() {
        var s = new StringBuilder();
        var val = new CharCountValidator(ValidatorOptions.NONE);

        assertTrue(val.isValidInput(s, false));
    }

    @Test
    void isInputValidBlank() {
        var s = new StringBuilder("   ");
        var val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);

        assertTrue(val.isValidInput(s, false));
    }

    @Test
    void isInputValidMinLength() {
        var s1 = new StringBuilder("123456789");
        var s2 = new StringBuilder("1234567890");
        var s3 = new StringBuilder("12345678");
        var val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        val.setMinLength(9);

        assertTrue(val.isValidInput(s1, false));
        assertTrue(val.isValidInput(s2, false));
        assertTrue(val.isValidInput(s3, false));
    }

    @Test
    void isInputValidMaxLength() {
        var s1 = new StringBuilder("123456789");
        var s2 = new StringBuilder("1234567890");
        var s3 = new StringBuilder("12345678");
        var val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        val.setMaxLength(9);

        assertTrue(val.isValidInput(s1, false));
        assertFalse(val.isValidInput(s2, false));
        assertTrue(val.isValidInput(s3, false));
    }

    @Test
    void validateWithExceptionEmptyTrue() {
        var val = new CharCountValidator(ValidatorOptions.NONE);
        assertDoesNotThrow(() -> val.validate(""));
    }

    @Test
    void validateWithExceptionFalse() {
        var val = new CharCountValidator();
        assertThrows(ValidatorException.class, () -> val.validate(""));
    }

    @Test
    void validateWithExceptionBlankTrue() {
        var val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        assertDoesNotThrow(() -> val.validate("   "));
    }

    @Test
    void validateWithExceptionBlankFalse() {
        var val = new CharCountValidator();
        assertThrows(ValidatorException.class, () -> val.validate("   "));
    }

    @Test
    void validateWithExceptionMinLength() {
        var s1 = "123456789";
        var s2 = "1234567890";
        var s3 = "12345678";
        var val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        val.setMinLength(9);

        assertDoesNotThrow(() -> val.validate(s1));
        assertDoesNotThrow(() -> val.validate(s2));
        assertThrows(ValidatorException.class, () -> val.validate(s3));
    }

    @Test
    void validateWithExceptionMaxLength() {
        var s1 = "123456789";
        var s2 = "1234567890";
        var s3 = "12345678";
        var val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        val.setMaxLength(9);

        assertDoesNotThrow(() -> val.validate(s1));
        assertThrows(ValidatorException.class, () -> val.validate(s2));
        assertDoesNotThrow(() -> val.validate(s3));
    }

    @Test
    void validateWithExceptionRef() {
        var s1 = "123456789";
        var s2 = "1234567890";
        var s3 = "12345678";
        var val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        val.setMinLength(9);

        ValidatorExceptionRef er = new ValidatorExceptionRef();
        assertTrue(val.validate(s1, er));
        assertNull(er.getException());

        er = new ValidatorExceptionRef();
        assertTrue(val.validate(s2, er));
        assertNull(er.getException());

        er = new ValidatorExceptionRef();
        assertFalse(val.validate(s3, er));
        assertNotNull(er.getException());
    }

    @Test
    void formatText() {
        var t1 = "1.234,00";
        var t2 = "001234,0000";
        var val = new CharCountValidator();

        assertEquals(t1, val.formatText(t1,TextFormat.EDIT));
        assertEquals(t1, val.formatText(t1,TextFormat.DISPLAY));
        assertEquals(t2, val.formatText(t2,TextFormat.EDIT));
        assertEquals(t2, val.formatText(t2,TextFormat.DISPLAY));
    }
}