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
        int level = 1;
        StringWriter writer = new StringWriter();
        WriterLogTarget target = new WriterLogTarget(level, writer);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create2() {
        int level = 1;
        StringWriter writer = new StringWriter();
        MyLogFilter logFilter = new MyLogFilter();
        WriterLogTarget target = new WriterLogTarget(level, writer, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create3() {
        int level = 1;
        StringWriter writer = new StringWriter();
        int timeout = 2;
        WriterLogTarget target = new WriterLogTarget(level, writer, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertFalse(target.isClosed());
    }

    @Test
    void create4() {
        int level = 1;
        StringWriter writer = new StringWriter();
        MyLogFilter logFilter = new MyLogFilter();
        int timeout = 2;
        WriterLogTarget target = new WriterLogTarget(level, writer, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertInstanceOf(DefaultLogFormatter.class, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create5() {
        int level = 1;
        StringWriter writer = new StringWriter();
        MyFormatter formatter = new MyFormatter();
        WriterLogTarget target = new WriterLogTarget(level, writer, formatter);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(formatter, target.getFormatter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create6() {
        int level = 1;
        StringWriter writer = new StringWriter();
        MyFormatter formatter = new MyFormatter();
        MyLogFilter logFilter = new MyLogFilter();
        WriterLogTarget target = new WriterLogTarget(level, writer, formatter, logFilter);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(formatter, target.getFormatter());
        assertEquals(0, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create7() {
        int level = 1;
        StringWriter writer = new StringWriter();
        MyFormatter formatter = new MyFormatter();
        int timeout = 2;
        WriterLogTarget target = new WriterLogTarget(level, writer, formatter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(formatter, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertInstanceOf(DefaultLogFilter.class, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void create8() {
        int level = 1;
        StringWriter writer = new StringWriter();
        MyFormatter formatter = new MyFormatter();
        MyLogFilter logFilter = new MyLogFilter();
        int timeout = 2;
        WriterLogTarget target = new WriterLogTarget(level, writer, formatter, logFilter, timeout);

        assertEquals(level, target.getLevel());
        assertEquals(writer, target.getWriter());
        assertEquals(formatter, target.getFormatter());
        assertEquals(timeout, target.getMessageWaitTimeout());
        assertEquals(logFilter, target.getFilter());
        assertFalse(target.isClosed());
    }

    @Test
    void write() throws IOException {
        int level = 1;
        StringWriter writer = new StringWriter();
        MyFormatter formatter = new MyFormatter();
        WriterLogTarget target = new WriterLogTarget(level, writer, formatter);
        String output = "m1\nm2\n";
        LogInfo[] info = new LogInfo[] {
                new LogInfo(1, 0, LocalDateTime.now(), 0, "", "m1"),
                new LogInfo(2, 1, LocalDateTime.now(), 0, "", "m2")
        };

        target.write(info);

        assertEquals(output, writer.toString());
    }
}