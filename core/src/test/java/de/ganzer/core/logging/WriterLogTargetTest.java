package de.ganzer.core.logging;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class WriterLogTargetTest {
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
        var writer = new StringWriter();
        var target = new WriterLogTarget(level, writer);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create2() {
        var level = 1;
        var writer = new StringWriter();
        var logFilter = new MyLogFilter();
        var target = new WriterLogTarget(level, writer, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create3() {
        var level = 1;
        var writer = new StringWriter();
        var timeout = 2;
        var target = new WriterLogTarget(level, writer, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create4() {
        var level = 1;
        var writer = new StringWriter();
        var logFilter = new MyLogFilter();
        var timeout = 2;
        var target = new WriterLogTarget(level, writer, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create5() {
        var level = 1;
        var writer = new StringWriter();
        var formatter = new MyFormatter();
        var target = new WriterLogTarget(level, writer, formatter);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(formatter, target.getFormatter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create6() {
        var level = 1;
        var writer = new StringWriter();
        var formatter = new MyFormatter();
        var logFilter = new MyLogFilter();
        var target = new WriterLogTarget(level, writer, formatter, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(formatter, target.getFormatter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create7() {
        var level = 1;
        var writer = new StringWriter();
        var formatter = new MyFormatter();
        var timeout = 2;
        var target = new WriterLogTarget(level, writer, formatter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(formatter, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create8() {
        var level = 1;
        var writer = new StringWriter();
        var formatter = new MyFormatter();
        var logFilter = new MyLogFilter();
        var timeout = 2;
        var target = new WriterLogTarget(level, writer, formatter, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(formatter, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void write() throws IOException {
        var level = 1;
        var writer = new StringWriter();
        var formatter = new MyFormatter();
        var target = new WriterLogTarget(level, writer, formatter);
        var output = "m1\nm2\n";
        var info = new LogInfo[] {
                new LogInfo(1, 0, LocalDateTime.now(), 0, "", "m1"),
                new LogInfo(2, 1, LocalDateTime.now(), 0, "", "m2")
        };

        target.write(info);

        assertEquals(output, writer.toString());
    }
}