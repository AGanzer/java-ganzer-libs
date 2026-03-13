package de.ganzer.core.logging;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LogInfoTest {
    @Test
    void create() {
        int num = 1;
        int level = 0;
        LocalDateTime time = LocalDateTime.now();
        long threadId = Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        String message = "test message";
        LogInfo info = new LogInfo(num, level, time, threadId, threadName, message);

        assertEquals(level, info.getLevel());
        assertEquals(num, info.getMessageNumber());
        assertEquals(time, info.getTime());
        assertEquals(threadId, info.getThreadID());
        assertEquals(threadName, info.getThreadName());
        assertEquals(message, info.getMessage());
    }
}