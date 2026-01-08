package de.ganzer.core.logging;

import java.time.LocalDateTime;

/**
 * The LogTarget class defines an abstract target for log messages.
 *
 * @see Logger
 */
public abstract class LogTarget {
    private Logger owner;
    private int messageNumber;

    final Logger getOwner() {
        return owner;
    }

    final void setOwner(Logger owner) {
        this.owner = owner;
    }

    /**
     * Writes the specified message into the target by calling
     * {@link #write(LogInfo[])}.
     * <p>
     * The meaning of the level is implementation defined. It depends on the
     * filter that is used by a target to filter messages.
     *
     * @param level The log level of the message to log.
     * @param time The time the message was logged by the logger.
     * @param message The message to write.
     */
    public final void write(int level, LocalDateTime time, String message) {
        var info = new LogInfo(++messageNumber, level, time, Thread.currentThread().getId(), Thread.currentThread().getName(), message);
        write(new LogInfo[]{info});
    }

    /**
     * Called by {@link #write(int, LocalDateTime, String)} to write the messages
     * that are not discarded by a filter into the physical target.
     *
     * @param info The information about the messages to write.
     */
    protected abstract void write(LogInfo[] info);
}
