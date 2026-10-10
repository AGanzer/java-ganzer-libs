package de.ganzer.dv;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DVExceptionsTest {
    @Test
    void testDVExceptionConstructors() {
        var ex1 = new DVException();
        assertNull(ex1.getMessage());
        assertNull(ex1.getCause());
        assertInstanceOf(IOException.class, ex1);

        var ex2 = new DVException("Error occurred");
        assertEquals("Error occurred", ex2.getMessage());
        assertNull(ex2.getCause());

        var cause = new RuntimeException("Root cause");
        var ex3 = new DVException("Error occurred", cause);
        assertEquals("Error occurred", ex3.getMessage());
        assertSame(cause, ex3.getCause());
    }

    @Test
    void testDVLoadExceptionConstructors() {
        var ex1 = new DVLoadException("Failed to load");
        assertEquals("Failed to load", ex1.getMessage());
        assertNull(ex1.getCause());
        assertInstanceOf(DVException.class, ex1);

        var cause = new IOException("IO issue");
        var ex2 = new DVLoadException("Failed to load", cause);
        assertEquals("Failed to load", ex2.getMessage());
        assertSame(cause, ex2.getCause());
    }

    @Test
    void testDVSaveExceptionConstructors() {
        var ex1 = new DVSaveException("Failed to save");
        assertEquals("Failed to save", ex1.getMessage());
        assertNull(ex1.getCause());
        assertInstanceOf(DVException.class, ex1);

        var cause = new IOException("Disk full");
        var ex2 = new DVSaveException("Failed to save", cause);
        assertEquals("Failed to save", ex2.getMessage());
        assertSame(cause, ex2.getCause());
    }
}
