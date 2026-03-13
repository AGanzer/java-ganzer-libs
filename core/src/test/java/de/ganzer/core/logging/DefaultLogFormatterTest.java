package de.ganzer.core.logging;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DefaultLogFormatterTest {
    private static class MyFormatter extends DefaultLogFormatter {
        public MyFormatter(String formatString) {
            super(formatString);
        }

        @Override
        protected String formatLevel(int level) {
            return level == 0 ? "ERROR: " : "";
        }
    }

    @Test
    void createDefault() {
        DefaultLogFormatter formatter = new DefaultLogFormatter();
        assertEquals(DefaultLogFormatter.DEFAULT_FORMAT, formatter.getFormatString());
    }

    @Test
    void create() {
        String formatString = "%6$s";
        DefaultLogFormatter formatter = new DefaultLogFormatter(formatString);

        assertEquals(formatString, formatter.getFormatString());
    }

    @Test
    void format() {
        String formatString = "%6$s";
        String message = "message";
        DefaultLogFormatter formatter = new DefaultLogFormatter(formatString);
        LogInfo info = new LogInfo(1, 0, LocalDateTime.now(), 0, "", message);

        assertEquals(message, formatter.format(info));
    }

    @Test
    void format1() {
        String formatString = "%4$s%6$s";
        String message = "message";
        MyFormatter formatter = new MyFormatter(formatString);

        LogInfo info1 = new LogInfo(1, 0, LocalDateTime.now(), 0, "", message);
        String expected1 = String.format("%s: %s", "ERROR", message);
        LogInfo info2 = new LogInfo(2, 1, LocalDateTime.now(), 0, "", message);
        String expected2 = String.format("%s", message);

        assertEquals(expected1, formatter.format(info1));
        assertEquals(expected2, formatter.format(info2));
    }
}