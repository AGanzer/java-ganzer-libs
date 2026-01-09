package de.ganzer.core.logging;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

/**
 * A log target that writes log messages into a stream.
 */
public class StreamWriterLogTarget extends FormattedLogTarget {
    private final OutputStreamWriter target;

    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param target The target where to write the messages into.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #StreamWriterLogTarget(int, OutputStreamWriter, LogFormatter, LogFilter, int)
     */
    public StreamWriterLogTarget(int level, OutputStreamWriter target) {
        this(level, target, null, null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param target The target where to write the messages into.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #StreamWriterLogTarget(int, OutputStreamWriter, LogFormatter, LogFilter, int)
     */
    public StreamWriterLogTarget(int level, OutputStreamWriter target, LogFilter filter) {
        this(level, target, null, filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param target The target where to write the messages into.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #StreamWriterLogTarget(int, OutputStreamWriter, LogFormatter, LogFilter, int)
     */
    public StreamWriterLogTarget(int level, OutputStreamWriter target, int messageWaitTimeout) {
        this(level, target, null, null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter}.
     *
     * @param level The level of the log messages to write.
     * @param target The target where to write the messages into.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #StreamWriterLogTarget(int, OutputStreamWriter, LogFormatter, LogFilter, int)
     */
    public StreamWriterLogTarget(int level, OutputStreamWriter target, LogFilter filter, int messageWaitTimeout) {
        this(level, target, null, filter, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param target The target where to write the messages into.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #StreamWriterLogTarget(int, OutputStreamWriter, LogFormatter, LogFilter, int)
     */
    public StreamWriterLogTarget(int level, OutputStreamWriter target, LogFormatter formatter) {
        this(level, target, formatter, null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param target The target where to write the messages into.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #StreamWriterLogTarget(int, OutputStreamWriter, LogFormatter, LogFilter, int)
     */
    public StreamWriterLogTarget(int level, OutputStreamWriter target, LogFormatter formatter, LogFilter filter) {
        this(level, target, formatter, filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param target The target where to write the messages into.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #StreamWriterLogTarget(int, OutputStreamWriter, LogFormatter, LogFilter, int)
     */
    public StreamWriterLogTarget(int level, OutputStreamWriter target, LogFormatter formatter, int messageWaitTimeout) {
        this(level, target, formatter, null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param level The level of the log messages to write.
     * @param target The target where to write the messages into.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    public StreamWriterLogTarget(int level, OutputStreamWriter target, LogFormatter formatter, LogFilter filter, int messageWaitTimeout) {
        super(level, formatter, filter, messageWaitTimeout);
        Objects.requireNonNull(target, "target must not be null.");

        this.target = target;
    }

    /**
     * Gets the target where to write into.
     *
     * @return The target set at construction.
     */
    public OutputStreamWriter getTarget() {
        return target;
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * <p>
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     * <p>
     * This implementation closes the target.
     *
     * @throws Exception if this resource cannot be closed.
     */
    @Override
    public void close() throws Exception {
        target.close();
        super.close();
    }

    /**
     * Called by {@link #write(LogInfo[])} to write the messages that are not
     * discarded by a filter into the physical target.
     *
     * @param level The log level of the message to log. This is given as
     *         information and should not be inserted into {@code messsage}.
     *         The level of joined messages is {@link Integer#MIN_VALUE}.
     * @param message The message to write. This is already formatted and must
     *         simply be written into the target as is.
     *
     * @throws IOException on any I/O error.
     */
    @Override
    protected void write(int level, String message) throws IOException {
        target.write(message);
    }
}
