package de.ganzer.core.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleLogTargetTest {
    private static class MyFormatter implements LogFormatter {
        @Override
        public String format(LogInfo info) {
            return "";
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
        ConsoleLogTarget target = new ConsoleLogTarget(level);

        assertEquals(level, target.getLevel());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create2() {
        int level = 1;
        MyLogFilter logFilter = new MyLogFilter();
        ConsoleLogTarget target = new ConsoleLogTarget(level, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create3() {
        int level = 1;
        int timeout = 2;
        ConsoleLogTarget target = new ConsoleLogTarget(level, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create4() {
        int level = 1;
        MyLogFilter logFilter = new MyLogFilter();
        int timeout = 2;
        ConsoleLogTarget target = new ConsoleLogTarget(level, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create5() {
        int level = 1;
        MyFormatter formatter = new MyFormatter();
        ConsoleLogTarget target = new ConsoleLogTarget(level, formatter);

        assertEquals(level, target.getLevel());
        assertEquals(formatter, target.getFormatter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create6() {
        int level = 1;
        MyFormatter formatter = new MyFormatter();
        MyLogFilter logFilter = new MyLogFilter();
        ConsoleLogTarget target = new ConsoleLogTarget(level, formatter, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(formatter, target.getFormatter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create7() {
        int level = 1;
        MyFormatter formatter = new MyFormatter();
        int timeout = 2;
        ConsoleLogTarget target = new ConsoleLogTarget(level, formatter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(formatter, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create8() {
        int level = 1;
        MyFormatter formatter = new MyFormatter();
        MyLogFilter logFilter = new MyLogFilter();
        int timeout = 2;
        ConsoleLogTarget target = new ConsoleLogTarget(level, formatter, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(formatter, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }
}