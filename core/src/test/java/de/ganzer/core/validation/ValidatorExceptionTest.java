package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorExceptionTest {
    @Test
    void create() {
        NumberValidator val = new NumberValidator();
        String msg = "Error";
        ValidatorException e = new ValidatorException(msg, val.getClass(), val);

        assertEquals(msg, e.getMessage());
        assertEquals(val.getClass(), e.getSourceClass());
        assertEquals(val, e.getSource());
    }
}