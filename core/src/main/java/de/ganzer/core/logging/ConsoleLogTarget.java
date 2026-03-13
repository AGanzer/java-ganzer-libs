package de.ganzer.core.logging;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Writes log messages into {@link System#out} respective {@link System#err}
 * if {@link #isErrorLevel(int)} indicates an error.
 *
 * @since 1.5.0
 */
public class ConsoleLogTarget extends FormattedLogTarget {
    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     *
     * @see #ConsoleLogTarget(int, LogFormatter, LogFilter, int)
     */
    public ConsoleLogTarget(int level) {
        super(level);
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
     * @see #ConsoleLogTarget(int, LogFormatter, LogFilter, int)
     */
    public ConsoleLogTarget(int level, LogFilter filter) {
        super(level, filter);
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
     * @see #ConsoleLogTarget(int, LogFormatter, LogFilter, int)
     */
    public ConsoleLogTarget(int level, int messageWaitTimeout) {
        super(level, messageWaitTimeout);
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
     * @see #ConsoleLogTarget(int, LogFormatter, LogFilter, int)
     */
    public ConsoleLogTarget(int level, LogFilter filter, int messageWaitTimeout) {
        super(level, filter, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     *
     * @see #ConsoleLogTarget(int, LogFormatter, LogFilter, int)
     */
    public ConsoleLogTarget(int level, LogFormatter formatter) {
        super(level, formatter);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @see #ConsoleLogTarget(int, LogFormatter, LogFilter, int)
     */
    public ConsoleLogTarget(int level, LogFormatter formatter, LogFilter filter) {
        super(level, formatter, filter);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @see #ConsoleLogTarget(int, LogFormatter, LogFilter, int)
     */
    public ConsoleLogTarget(int level, LogFormatter formatter, int messageWaitTimeout) {
        super(level, formatter, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param level The level of the log messages to write.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     */
    public ConsoleLogTarget(int level, LogFormatter formatter, LogFilter filter, int messageWaitTimeout) {
        super(level, formatter, filter, messageWaitTimeout);
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
     * This implementation calls {@link #write(int, String)} for each single log
     * info to write the formatted message into the physical target.
     *
     * @param info The information about the messages to write.
     *
     * @throws IOException on any I/O error.
     */
    @Override
    protected void write(LogInfo[] info) throws IOException {
        for (LogInfo logInfo : info)
            write(logInfo.getLevel(), getFormatter().format(logInfo));
    }

    /**
     * Called by {@link #write(LogInfo[])} to write the messages that are not
     * discarded by a filter into the physical target.
     * <p>
     * This implementation calls {@link #isErrorLevel(int)} to query whether
     * the message should be written into {@link System#err} instead of
     * {@link System#out}.
     *
     * @param level The log level of the message to log. This is given as
     *         information and should not be inserted into {@code messsage}.
     *         The level of joined messages is {@link Integer#MIN_VALUE}.
     * @param message The message to write. This is already formatted and must
     *         simply be written into the target as is.
     */
    @Override
    protected void write(int level, String message) throws IOException {
        if (isErrorLevel(level))
            System.err.println(message);
        else
            System.out.println(message);
    }

    /**
     * Called to get the information whether a log level indicates an error.
     * <p>
     * Inheritors should override this to implement its own error level
     * detection.
     *
     * @param level The level to query.
     *
     * @return {@code true} if {@code level} indicates an error. This
     *         implementation returns {@code level == 0}.
     */
    protected boolean isErrorLevel(int level) {
        return level == 0;
    }
}
