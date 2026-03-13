package de.ganzer.core.logging;

import java.time.LocalDateTime;

/**
 * This class is used by {@link LogTarget} as well as {@link LogFormatter} and
 * provides detailed information about a logged message.
 *
 * @since 1.5.0
 */
public class LogInfo {
    private final int messageNumber;
    private final int level;
    private final LocalDateTime time;
    private final long threadID;
    private final String threadName;
    private final String message;

    /**
     * Creates an instance from the specified arguments.
     *
     * @param messageNumber The sequential number of the message to write.
     * @param level The level of the message to write.
     * @param time The time the message was logged.
     * @param threadID The ID of the thread that has logged the message.
     * @param threadName The name of the thread that has logged the message.
     * @param message The message to write.
     */
    public LogInfo(int messageNumber, int level, LocalDateTime time, long threadID, String threadName, String message) {
        this.messageNumber = messageNumber;
        this.level = level;
        this.time = time;
        this.threadID = threadID;
        this.threadName = threadName;
        this.message = message;
    }

    /**
     * Gets the sequential number of the message to write.
     *
     * @return The sequential number of the message to write.
     */
    public int getMessageNumber() {
        return messageNumber;
    }

    /**
     * Gets the level of the message to write.
     *
     * @return The level of the message to write.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the time the message was logged.
     *
     * @return The time the message was logged.
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Gets the ID of the thread that has logged the message.
     *
     * @return The ID of the thread that has logged the message.
     */
    public long getThreadID() {
        return threadID;
    }

    /**
     * Gets the name of the thread that has logged the message.
     *
     * @return The name of the thread that has logged the message.
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * Gets the message to write.
     *
     * @return The message to write.
     */
    public String getMessage() {
        return message;
    }
}
