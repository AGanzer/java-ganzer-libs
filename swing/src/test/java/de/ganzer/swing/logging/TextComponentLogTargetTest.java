package de.ganzer.swing.logging;

import de.ganzer.core.logging.*;
import org.junit.jupiter.api.Test;

import javax.swing.JTextArea;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("resource")
class TextComponentLogTargetTest {
    private static class MyFormatter implements LogFormatter {
        @Override
        public String format(LogInfo info) {
            return info.getMessage();
        }
    }

    private static class MyLogFilter implements LogFilter {
        @Override
        public boolean shouldWrite(int messageLevel, int targetLevel) {
            return true;
        }
    }

    @Test
    void create1() {
        var level = 1;
        var component = new JTextArea();
        var target = new TextComponentLogTarget(level, component);

        assertEquals(level, target.getLevel());
        assertEquals(component, target.getComponent());
        assertTrue(target.isFormatHTML());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create2() {
        var level = 1;
        var component = new JTextArea();
        var logFilter = new MyLogFilter();
        var target = new TextComponentLogTarget(level, component, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(component, target.getComponent());
        assertTrue(target.isFormatHTML());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create3() {
        var level = 1;
        var component = new JTextArea();
        var timeout = 2;
        var target = new TextComponentLogTarget(level, component, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(component, target.getComponent());
        assertTrue(target.isFormatHTML());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create4() {
        var level = 1;
        var component = new JTextArea();
        var logFilter = new MyLogFilter();
        var timeout = 2;
        var target = new TextComponentLogTarget(level, component, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(component, target.getComponent());
        assertTrue(target.isFormatHTML());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create5() {
        var level = 1;
        var component = new JTextArea();
        var formatter = new MyFormatter();
        var target = new TextComponentLogTarget(level, component, formatter);

        assertEquals(level, target.getLevel());
        assertEquals(component, target.getComponent());
        assertTrue(target.isFormatHTML());
        assertEquals(formatter, target.getFormatter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create6() {
        var level = 1;
        var component = new JTextArea();
        var formatter = new MyFormatter();
        var logFilter = new MyLogFilter();
        var target = new TextComponentLogTarget(level, component, formatter, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(component, target.getComponent());
        assertTrue(target.isFormatHTML());
        assertEquals(formatter, target.getFormatter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create7() {
        var level = 1;
        var component = new JTextArea();
        var formatter = new MyFormatter();
        var timeout = 2;
        var target = new TextComponentLogTarget(level, component, formatter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(component, target.getComponent());
        assertTrue(target.isFormatHTML());
        assertEquals(formatter, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create8() {
        var level = 1;
        var component = new JTextArea();
        var formatter = new MyFormatter();
        var logFilter = new MyLogFilter();
        var timeout = 2;
        var target = new TextComponentLogTarget(level, component, formatter, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(component, target.getComponent());
        assertTrue(target.isFormatHTML());
        assertEquals(formatter, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void setFormatHTML() {
        var component = new JTextArea();
        var target = new TextComponentLogTarget(2, component);

        target.setFormatHTML(false);

        assertFalse(target.isFormatHTML());
    }
}