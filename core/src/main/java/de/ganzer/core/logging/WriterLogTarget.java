package de.ganzer.core.logging;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

/**
 * A log target that writes log messages into a stream writer.
 *
 * @since 5.4.0
 */
public class WriterLogTarget extends FormattedLogTarget {
    private final Writer writer;

    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param writer The target where to write the messages into.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #WriterLogTarget(int, Writer, LogFormatter, LogFilter, int)
     */
    public WriterLogTarget(int level, Writer writer) {
        this(level, writer, null, null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param writer The target where to write the messages into.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #WriterLogTarget(int, Writer, LogFormatter, LogFilter, int)
     */
    public WriterLogTarget(int level, Writer writer, LogFilter filter) {
        this(level, writer, null, filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param writer The target where to write the messages into.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #WriterLogTarget(int, Writer, LogFormatter, LogFilter, int)
     */
    public WriterLogTarget(int level, Writer writer, int messageWaitTimeout) {
        this(level, writer, null, null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter}.
     *
     * @param level The level of the log messages to write.
     * @param writer The target where to write the messages into.
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
     * @see #WriterLogTarget(int, Writer, LogFormatter, LogFilter, int)
     */
    public WriterLogTarget(int level, Writer writer, LogFilter filter, int messageWaitTimeout) {
        this(level, writer, null, filter, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param writer The target where to write the messages into.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #WriterLogTarget(int, Writer, LogFormatter, LogFilter, int)
     */
    public WriterLogTarget(int level, Writer writer, LogFormatter formatter) {
        this(level, writer, formatter, null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param writer The target where to write the messages into.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #WriterLogTarget(int, Writer, LogFormatter, LogFilter, int)
     */
    public WriterLogTarget(int level, Writer writer, LogFormatter formatter, LogFilter filter) {
        this(level, writer, formatter, filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param writer The target where to write the messages into.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     *
     * @see #WriterLogTarget(int, Writer, LogFormatter, LogFilter, int)
     */
    public WriterLogTarget(int level, Writer writer, LogFormatter formatter, int messageWaitTimeout) {
        this(level, writer, formatter, null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param level The level of the log messages to write.
     * @param writer The target where to write the messages into.
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
    public WriterLogTarget(int level, Writer writer, LogFormatter formatter, LogFilter filter, int messageWaitTimeout) {
        super(level, formatter, filter, messageWaitTimeout);
        Objects.requireNonNull(writer, "target must not be null.");

        this.writer = writer;
    }

    /**
     * Gets the target where to write into.
     *
     * @return The target set at construction.
     */
    public Writer getWriter() {
        return writer;
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
        writer.flush();
        writer.close();
        super.close();
    }

    /**
     * Called by {@link #write(LogInfo[])} to write the messages that are not
     * discarded by a filter into the physical target.
     *
     * @param level The log level of the message to log. This is given as
     *         information and should not be inserted into {@code messsage}.
     *         The level of joined messages is {@link Integer#MIN_VALUE}.
     * @param message The message to write. This is already formatted and
     *        contains a trailing linefeed. This implementation does simply
     *        write it into the target as is.
     *
     * @throws IOException on any I/O error.
     */
    @Override
    protected void write(int level, String message) throws IOException {
        writer.write(message);
    }
}
