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
        int level = 1;
        JTextArea component = new JTextArea();
        TextComponentLogTarget target = new TextComponentLogTarget(level, component);

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
        int level = 1;
        JTextArea component = new JTextArea();
        MyLogFilter logFilter = new MyLogFilter();
        TextComponentLogTarget target = new TextComponentLogTarget(level, component, logFilter);

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
        int level = 1;
        JTextArea component = new JTextArea();
        int timeout = 2;
        TextComponentLogTarget target = new TextComponentLogTarget(level, component, timeout);

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
        int level = 1;
        JTextArea component = new JTextArea();
        MyLogFilter logFilter = new MyLogFilter();
        int timeout = 2;
        TextComponentLogTarget target = new TextComponentLogTarget(level, component, logFilter, timeout);

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
        int level = 1;
        JTextArea component = new JTextArea();
        MyFormatter formatter = new MyFormatter();
        TextComponentLogTarget target = new TextComponentLogTarget(level, component, formatter);

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
        int level = 1;
        JTextArea component = new JTextArea();
        MyFormatter formatter = new MyFormatter();
        MyLogFilter logFilter = new MyLogFilter();
        TextComponentLogTarget target = new TextComponentLogTarget(level, component, formatter, logFilter);

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
        int level = 1;
        JTextArea component = new JTextArea();
        MyFormatter formatter = new MyFormatter();
        int timeout = 2;
        TextComponentLogTarget target = new TextComponentLogTarget(level, component, formatter, timeout);

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
        int level = 1;
        JTextArea component = new JTextArea();
        MyFormatter formatter = new MyFormatter();
        MyLogFilter logFilter = new MyLogFilter();
        int timeout = 2;
        TextComponentLogTarget target = new TextComponentLogTarget(level, component, formatter, logFilter, timeout);

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
        JTextArea component = new JTextArea();
        TextComponentLogTarget target = new TextComponentLogTarget(2, component);

        target.setFormatHTML(false);

        assertFalse(target.isFormatHTML());
    }
}