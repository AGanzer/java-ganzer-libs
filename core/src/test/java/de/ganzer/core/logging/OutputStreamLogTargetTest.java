package de.ganzer.core.logging;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class OutputStreamLogTargetTest {
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
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        java.nio.charset.Charset cs = StandardCharsets.UTF_16;
        OutputStreamLogTarget target = new OutputStreamLogTarget(level, stream, cs);

        assertEquals(level, target.getLevel());
        assertEquals(stream, target.getStream());
        assertEquals(cs, target.getCharset());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create2() {
        int level = 1;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        java.nio.charset.Charset cs = StandardCharsets.UTF_16;
        MyLogFilter logFilter = new MyLogFilter();
        OutputStreamLogTarget target = new OutputStreamLogTarget(level, stream, cs, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(stream, target.getStream());
        assertEquals(cs, target.getCharset());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create3() {
        int level = 1;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        java.nio.charset.Charset cs = StandardCharsets.UTF_16;
        int timeout = 2;
        OutputStreamLogTarget target = new OutputStreamLogTarget(level, stream, cs, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(stream, target.getStream());
        assertEquals(cs, target.getCharset());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create4() {
        int level = 1;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        java.nio.charset.Charset cs = StandardCharsets.UTF_16;
        MyLogFilter logFilter = new MyLogFilter();
        int timeout = 2;
        OutputStreamLogTarget target = new OutputStreamLogTarget(level, stream, cs, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(stream, target.getStream());
        assertEquals(cs, target.getCharset());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create5() {
        int level = 1;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Charset cs = StandardCharsets.UTF_16;
        MyFormatter formatter = new MyFormatter();
        OutputStreamLogTarget target = new OutputStreamLogTarget(level, stream, cs, formatter);

        assertEquals(level, target.getLevel());
        assertEquals(stream, target.getStream());
        assertEquals(cs, target.getCharset());
        assertEquals(formatter, target.getFormatter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create6() {
        int level = 1;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Charset cs = StandardCharsets.UTF_16;
        MyFormatter formatter = new MyFormatter();
        MyLogFilter logFilter = new MyLogFilter();
        OutputStreamLogTarget target = new OutputStreamLogTarget(level, stream, cs, formatter, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(stream, target.getStream());
        assertEquals(cs, target.getCharset());
        assertEquals(formatter, target.getFormatter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create7() {
        int level = 1;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Charset cs = StandardCharsets.UTF_16;
        MyFormatter formatter = new MyFormatter();
        int timeout = 2;
        OutputStreamLogTarget target = new OutputStreamLogTarget(level, stream, cs, formatter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(stream, target.getStream());
        assertEquals(cs, target.getCharset());
        assertEquals(formatter, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create8() {
        int level = 1;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Charset cs = StandardCharsets.UTF_16;
        MyFormatter formatter = new MyFormatter();
        MyLogFilter logFilter = new MyLogFilter();
        int timeout = 2;
        OutputStreamLogTarget target = new OutputStreamLogTarget(level, stream, cs, formatter, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(stream, target.getStream());
        assertEquals(cs, target.getCharset());
        assertEquals(formatter, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }
}