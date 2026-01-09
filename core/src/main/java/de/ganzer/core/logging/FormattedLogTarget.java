package de.ganzer.core.logging;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * A base class for log targets that wont to write all log message information
 * with a single preformatted string.
 */
public abstract class FormattedLogTarget extends LogTarget {
    private final LogFormatter formatter;

    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     *
     * @see #FormattedLogTarget(int, LogFormatter, LogFilter, int)
     */
    public FormattedLogTarget(int level) {
        this(level, null, null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @see #FormattedLogTarget(int, LogFormatter, LogFilter, int)
     */
    public FormattedLogTarget(int level, LogFilter filter) {
        this(level, null, filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @see #FormattedLogTarget(int, LogFormatter, LogFilter, int)
     */
    public FormattedLogTarget(int level, int messageWaitTimeout) {
        this(level, null, null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter}.
     *
     * @param level The level of the log messages to write.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @see #FormattedLogTarget(int, LogFormatter, LogFilter, int)
     */
    public FormattedLogTarget(int level, LogFilter filter, int messageWaitTimeout) {
        this(level, null, filter, messageWaitTimeout);
    }
    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *        of {@link DefaultLogFormatter} is used.
     *
     * @see #FormattedLogTarget(int, LogFormatter, LogFilter, int)
     */
    public FormattedLogTarget(int level, LogFormatter formatter) {
        this(level, formatter, null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *        of {@link DefaultLogFormatter} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @see #FormattedLogTarget(int, LogFormatter, LogFilter, int)
     */
    public FormattedLogTarget(int level, LogFormatter formatter, LogFilter filter) {
        this(level, formatter, filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *        of {@link DefaultLogFormatter} is used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @see #FormattedLogTarget(int, LogFormatter, LogFilter, int)
     */
    public FormattedLogTarget(int level, LogFormatter formatter, int messageWaitTimeout) {
        this(level, formatter, null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param level The level of the log messages to write.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *        of {@link DefaultLogFormatter} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     */
    public FormattedLogTarget(int level, LogFormatter formatter, LogFilter filter, int messageWaitTimeout) {
        super(level, filter, messageWaitTimeout);
        this.formatter = formatter != null ? formatter : new DefaultLogFormatter();
    }

    /**
     * Gets the used formatter.
     *
     * @return The used formatter.
     */
    public LogFormatter getFormatter() {
        return formatter;
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
     * This implementation formats and joins the messages (separated by '\n')
     * and calls {@link #write(int, String)} to write them into a physical
     * target at once.
     *
     * @param info The information about the messages to write.
     *
     * @throws IOException on any I/O error.
     */
    @Override
    protected final void write(LogInfo[] info) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (var i = 0; i < info.length; i++) {
            if (i > 0)
                sb.append('\n');

            sb.append(formatter.format(info[i]));
        }

        write(info.length == 1 ? info[0].getLevel() : Integer.MIN_VALUE, sb.toString());
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
    protected abstract void write(int level, String message) throws IOException;
}
