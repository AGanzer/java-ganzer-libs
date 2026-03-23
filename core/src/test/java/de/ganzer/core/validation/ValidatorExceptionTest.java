package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorExceptionTest {
    @Test
    void create() {
        var val = new NumberValidator();
        var msg = "Error";
        var e = new ValidatorException(msg, val.getClass(), val);

        assertEquals(msg, e.getMessage());
        assertEquals(val.getClass(), e.getSourceClass());
        assertEquals(val, e.getSource());
    }
}