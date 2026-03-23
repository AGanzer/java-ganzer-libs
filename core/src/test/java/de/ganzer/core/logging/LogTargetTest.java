package de.ganzer.core.logging;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LogTargetTest {
    private static class MyTarget extends LogTarget {
        public MyTarget(int level) {
            super(level);
        }

        public MyTarget(int level, LogFilter filter) {
            super(level, filter);
        }

        public MyTarget(int level, int messageWaitTimeout) {
            super(level, messageWaitTimeout);
        }

        public MyTarget(int level, LogFilter filter, int messageWaitTimeout) {
            super(level, filter, messageWaitTimeout);
        }

        @Override
        protected void write(LogInfo[] info) {
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
        var target = new MyTarget(level);

        assertEquals(level, target.getLevel());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create2() {
        var level = 1;
        var logFilter = new MyLogFilter();
        var target = new MyTarget(level, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create3() {
        var level = 1;
        var timeout = 2;
        var target = new MyTarget(level, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create4() {
        var level = 1;
        var logFilter = new MyLogFilter();
        var timeout = 2;
        var target = new MyTarget(level, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void close() throws Exception {
        var target = new MyTarget(0);
        target.close();

        assertTrue(target.isClosed());
    }

    @Test
    void closedWrite() throws Exception {
        var target = new MyTarget(0);
        target.close();

        assertThrows(IllegalStateException.class, () -> target.write(0, LocalDateTime.now(), ""));
    }
}