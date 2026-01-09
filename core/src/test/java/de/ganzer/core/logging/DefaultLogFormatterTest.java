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
        var formatter = new DefaultLogFormatter();
        assertEquals(DefaultLogFormatter.DEFAULT_FORMAT, formatter.getFormatString());
    }

    @Test
    void create() {
        var formatString = "%6$s";
        var formatter = new DefaultLogFormatter(formatString);

        assertEquals(formatString, formatter.getFormatString());
    }

    @Test
    void format() {
        var formatString = "%6$s";
        var message = "message";
        var formatter = new DefaultLogFormatter(formatString);
        var info = new LogInfo(1, 0, LocalDateTime.now(), 0, "", message);

        assertEquals(message, formatter.format(info));
    }

    @Test
    void format1() {
        var formatString = "%4$s%6$s";
        var message = "message";
        var formatter = new MyFormatter(formatString);

        var info1 = new LogInfo(1, 0, LocalDateTime.now(), 0, "", message);
        var expected1 = String.format("%s: %s", "ERROR", message);
        var info2 = new LogInfo(2, 1, LocalDateTime.now(), 0, "", message);
        var expected2 = String.format("%s", message);

        assertEquals(expected1, formatter.format(info1));
        assertEquals(expected2, formatter.format(info2));
    }
}