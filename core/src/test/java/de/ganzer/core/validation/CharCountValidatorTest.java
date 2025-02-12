package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharCountValidatorTest {
    @Test
    void constructEmpty() {
        CharCountValidator val = new CharCountValidator();

        assertNull(val.getErrorMessage());
        assertNull(val.getTag());
        assertEquals(0, val.getMinLength());
        assertEquals(0, val.getMaxLength());
        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
    }

    @Test
    void constructWithOption() {
        CharCountValidator val = new CharCountValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT);

        assertNull(val.getErrorMessage());
        assertNull(val.getTag());
        assertEquals(0, val.getMinLength());
        assertEquals(0, val.getMaxLength());
        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
    }

    @Test
    void setOptions() {
        CharCountValidator val = new CharCountValidator();
        val.setOptions(ValidatorOptions.AUTO_FILL);

        assertEquals(ValidatorOptions.AUTO_FILL, val.getOptions());
    }

    @Test
    void setMaxLength() {
        CharCountValidator val = new CharCountValidator();
        val.setMinLength(0);
        val.setMaxLength(10);

        assertEquals(10, val.getMaxLength());
        assertEquals(0, val.getMinLength());
    }

    @Test
    void setMaxLess() {
        CharCountValidator val = new CharCountValidator();
        val.setMinLength(10);
        val.setMaxLength(5);

        assertEquals(5, val.getMaxLength());
        assertEquals(5, val.getMinLength());
    }

    @Test
    void setMinLength() {
        CharCountValidator val = new CharCountValidator();
        val.setMaxLength(10);
        val.setMinLength(0);

        assertEquals(0, val.getMinLength());
        assertEquals(10, val.getMaxLength());
    }

    @Test
    void setMinGreat() {
        CharCountValidator val = new CharCountValidator();
        val.setMaxLength(10);
        val.setMinLength(20);

        assertEquals(20, val.getMinLength());
        assertEquals(20, val.getMaxLength());
    }

    @Test
    void setMaxLengthAdjustMinLength() {
        CharCountValidator val = new CharCountValidator();
        val.setMinLength(20);
        val.setMaxLength(10);

        assertEquals(10, val.getMaxLength());
        assertEquals(10, val.getMinLength());
    }

    @Test
    void setMinLengthAdjustMaxLength() {
        CharCountValidator val = new CharCountValidator();
        val.setMaxLength(10);
        val.setMinLength(20);

        assertEquals(20, val.getMinLength());
        assertEquals(20, val.getMaxLength());
    }

    @Test
    void setErrorMessage() {
        String m = "New Message!";
        CharCountValidator val = new CharCountValidator();
        val.setErrorMessage(m);

        assertEquals(m, val.getErrorMessage());
    }

    @Test
    void setErrorMessageNull() {
        String m = "New Message!";
        CharCountValidator val = new CharCountValidator();
        val.setErrorMessage(m);
        val.setErrorMessage(null);

        assertNull(val.getErrorMessage());
    }

    @Test
    void setErrorMessageEmpty() {
        String m = "New Message!";
        CharCountValidator val = new CharCountValidator();
        val.setErrorMessage(m);
        val.setErrorMessage("");

        assertNull(val.getErrorMessage());
    }

    @Test
    void setErrorMessageBlank() {
        String m = "New Message!";
        CharCountValidator val = new CharCountValidator();
        val.setErrorMessage(m);
        val.setErrorMessage("   ");

        assertNull(val.getErrorMessage());
    }

    @Test
    void setTag() {
        Object o = new Object();
        CharCountValidator val = new CharCountValidator();
        val.setTag(o);

        assertEquals(o, val.getTag());
    }

    @Test
    void hasOption() {
        CharCountValidator val = new CharCountValidator(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT);

        assertTrue(val.hasOption(ValidatorOptions.NEEDS_INPUT));
        assertTrue(val.hasOption(ValidatorOptions.AUTO_FILL));
        assertTrue(val.hasOption(ValidatorOptions.NEEDS_INPUT | ValidatorOptions.BLANKS_VALID));
        assertFalse(val.hasOption(ValidatorOptions.BLANKS_VALID));
    }

    @Test
    void isInputValidEmpty() {
        StringBuilder s = new StringBuilder();
        CharCountValidator val = new CharCountValidator(ValidatorOptions.NONE);

        assertTrue(val.isValidInput(s, false));
    }

    @Test
    void isInputValidBlank() {
        StringBuilder s = new StringBuilder("   ");
        CharCountValidator val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);

        assertTrue(val.isValidInput(s, false));
    }

    @Test
    void isInputValidMinLength() {
        StringBuilder s1 = new StringBuilder("123456789");
        StringBuilder s2 = new StringBuilder("1234567890");
        StringBuilder s3 = new StringBuilder("12345678");
        CharCountValidator val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        val.setMinLength(9);

        assertTrue(val.isValidInput(s1, false));
        assertTrue(val.isValidInput(s2, false));
        assertTrue(val.isValidInput(s3, false));
    }

    @Test
    void isInputValidMaxLength() {
        StringBuilder s1 = new StringBuilder("123456789");
        StringBuilder s2 = new StringBuilder("1234567890");
        StringBuilder s3 = new StringBuilder("12345678");
        CharCountValidator val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        val.setMaxLength(9);

        assertTrue(val.isValidInput(s1, false));
        assertFalse(val.isValidInput(s2, false));
        assertTrue(val.isValidInput(s3, false));
    }

    @Test
    void validateWithExceptionEmptyTrue() {
        CharCountValidator val = new CharCountValidator(ValidatorOptions.NONE);
        assertDoesNotThrow(() -> val.validate(""));
    }

    @Test
    void validateWithExceptionFalse() {
        CharCountValidator val = new CharCountValidator();
        assertThrows(ValidatorException.class, () -> val.validate(""));
    }

    @Test
    void validateWithExceptionBlankTrue() {
        CharCountValidator val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        assertDoesNotThrow(() -> val.validate("   "));
    }

    @Test
    void validateWithExceptionBlankFalse() {
        CharCountValidator val = new CharCountValidator();
        assertThrows(ValidatorException.class, () -> val.validate("   "));
    }

    @Test
    void validateWithExceptionMinLength() {
        String s1 = "123456789";
        String s2 = "1234567890";
        String s3 = "12345678";
        CharCountValidator val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        val.setMinLength(9);

        assertDoesNotThrow(() -> val.validate(s1));
        assertDoesNotThrow(() -> val.validate(s2));
        assertThrows(ValidatorException.class, () -> val.validate(s3));
    }

    @Test
    void validateWithExceptionMaxLength() {
        String s1 = "123456789";
        String s2 = "1234567890";
        String s3 = "12345678";
        CharCountValidator val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
        val.setMaxLength(9);

        assertDoesNotThrow(() -> val.validate(s1));
        assertThrows(ValidatorException.class, () -> val.validate(s2));
        assertDoesNotThrow(() -> val.validate(s3));
    }

    @Test
    void validateWithExceptionRef() {
        String s1 = "123456789";
        String s2 = "1234567890";
        String s3 = "12345678";
        CharCountValidator val = new CharCountValidator(ValidatorOptions.BLANKS_VALID);
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
        String t1 = "1.234,00";
        String t2 = "001234,0000";
        CharCountValidator val = new CharCountValidator();

        assertEquals(t1, val.formatText(t1,TextFormat.EDIT));
        assertEquals(t1, val.formatText(t1,TextFormat.DISPLAY));
        assertEquals(t2, val.formatText(t2,TextFormat.EDIT));
        assertEquals(t2, val.formatText(t2,TextFormat.DISPLAY));
    }
}