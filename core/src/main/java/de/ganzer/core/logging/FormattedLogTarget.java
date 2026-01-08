package de.ganzer.core.logging;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A base class for log targets that wont to write all log message information
 * with a single preformatted string.
 */
public abstract class FormattedLogTarget extends LogTarget {
    /**
     * Creates a new instance from the specified argument.
     *
     * @param level The level of the log messages to write.
     *
     * @see #FormattedLogTarget(int, int)
     * @see #FormattedLogTarget(int, LogFilter, int)
     * @see #getMessageWaitTimeout()
     */
    public FormattedLogTarget(int level) {
        super(level);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param level The level of the log messages to write.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @see #FormattedLogTarget(int, int)
     * @see #FormattedLogTarget(int, LogFilter, int)
     * @see #getMessageWaitTimeout()
     */
    public FormattedLogTarget(int level, LogFilter filter) {
        super(level, filter);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param level The level of the log messages to write.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     */
    public FormattedLogTarget(int level, int messageWaitTimeout) {
        super(level, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param level The level of the log messages to write.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     */
    public FormattedLogTarget(int level, LogFilter filter, int messageWaitTimeout) {
        super(level, filter, messageWaitTimeout);
    }

    /**
     * Called by {@link #write(int, LocalDateTime, String)} to write the messages
     * that are not discarded by a filter into the physical target.
     * <p>
     * In some cases many single messages slows down the update behavior of
     * the target or decreases the responsiveness of the application. Joined
     * messages are not written directly into the target, but they are
     * collected until the target is able to work further messages. These
     * messages should be written as one single message.
     * <p>
     * If {@link #getMessageWaitTimeout()} is less than 1, {@code info} contains
     * only one element. In all other cases it may contain multiple elements.
     * These elements should be connected to one message in an implementation of
     * this method to perform only a single write action for each message.
     * <p>
     * This implementation formats and joins the messages and calls
     * {@link #write(int, String)} to write them into a physical target
     * at once.
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
