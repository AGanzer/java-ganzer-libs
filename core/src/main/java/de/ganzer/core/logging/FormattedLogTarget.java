package de.ganzer.core.logging;

import java.time.LocalDateTime;

/**
 * A base class for log targets that wont to write all log message information
 * with a single preformatted string.
 */
public abstract class FormattedLogTarget extends LogTarget {
    /**
     * Called by {@link #write(int, LocalDateTime, String)} to write the messages
     * that are not discarded by a filter into the physical target.
     * <p>
     * This implementation formats the messages and calls {@link #write(int, String)}
     * to write it into a physical target.
     *
     * @param info The information about the messages to write.
     */
    @Override
    protected final void write(LogInfo[] info) {
    }

    /**
     * Called by {@link #write(LogInfo[])} to write the messages that are not
     * discarded by a filter into the physical target.
     *
     * @param level The log level of the message to log. This is given as
     *        information and should not be inserted into {@code messsage}.
     *        The level of joined messages is {@link Integer#MIN_VALUE}.
     * @param message The message to write. This is already formatted and must
     *        simply be written into the target as is.
     */
    protected abstract void write(int level, String message);
}
