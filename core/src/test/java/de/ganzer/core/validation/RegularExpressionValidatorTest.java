package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class RegularExpressionValidatorTest {
    @Test
    void constructEmpty() {
        var val = new RegularExpressionValidator();

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertNull(val.getPattern());
    }

    @Test
    void constructWithOption() {
        var val = new RegularExpressionValidator(ValidatorOptions.BLANKS_VALID);

        assertEquals(ValidatorOptions.BLANKS_VALID, val.getOptions());
        assertNull(val.getPattern());
    }

    @Test
    void constructWithPic() {
        var pat = Pattern.compile("\\d*");
        var val = new RegularExpressionValidator(pat);

        assertEquals(ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(pat, val.getPattern());
    }

    @Test
    void constructWithPicAndOption() {
        var pat = Pattern.compile("\\d*");
        var val = new RegularExpressionValidator(ValidatorOptions.BLANKS_VALID, pat);

        assertEquals(ValidatorOptions.BLANKS_VALID, val.getOptions());
        assertEquals(pat, val.getPattern());
    }
    
    @Test
    void setPattern() {
        var pat = Pattern.compile("\\d*");
        var val = new RegularExpressionValidator();

        val.setPattern(pat);
        assertEquals(pat, val.getPattern());
    }

    @Test
    void doInputValidation() {
        var val = new RegularExpressionValidator(Pattern.compile("\\d\\d\\d \\d"));

        assertTrue(val.isValidInput(new StringBuilder("0"), false));
        assertTrue(val.isValidInput(new StringBuilder("456"), false));
        assertTrue(val.isValidInput(new StringBuilder("456 3"), false));
        assertTrue(val.isValidInput(new StringBuilder("456 "), false));
        assertFalse(val.isValidInput(new StringBuilder("a"), false));
        assertFalse(val.isValidInput(new StringBuilder("1234"), false));
        assertFalse(val.isValidInput(new StringBuilder("123 b"), false));
        assertFalse(val.isValidInput(new StringBuilder("123 00"), false));
    }

    @Test
    void doValidate() {
        var val = new RegularExpressionValidator(Pattern.compile("\\d*3 \\d"));

        assertDoesNotThrow(() -> val.validate("123 5"));
        assertThrows(ValidatorException.class, () -> val.validate("123 "));
        assertThrows(ValidatorException.class, () -> val.validate("123"));
        assertThrows(ValidatorException.class, () -> val.validate("1"));
        assertThrows(ValidatorException.class, () -> val.validate("123 40"));
    }

    @Test
    void doValidate2() {
        var val = new RegularExpressionValidator(Pattern.compile("\\d*3 "));

        assertDoesNotThrow(() -> val.validate("123 "));
        assertThrows(ValidatorException.class, () -> val.validate("123"));
        assertThrows(ValidatorException.class, () -> val.validate("1"));
        assertThrows(ValidatorException.class, () -> val.validate("123 40"));
    }
}
