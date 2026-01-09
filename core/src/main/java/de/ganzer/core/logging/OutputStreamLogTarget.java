package de.ganzer.core.logging;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class OutputStreamLogTarget extends StreamWriterLogTarget {
    private final OutputStream stream;

    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param stream The target where to write the messages into.
     * @param charset The charset to use. If this is {@code null},
     *        {@link Charset#defaultCharset()} is used.
     *
     * @throws NullPointerException {@code stream} is {@code null}.
     *
     * @see #OutputStreamLogTarget(int, OutputStream, Charset, LogFormatter, LogFilter, int)
     */
    public OutputStreamLogTarget(int level, OutputStream stream, Charset charset) {
        this(level, stream, charset, null, null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param stream The target where to write the messages into.
     * @param charset The charset to use. If this is {@code null},
     *        {@link Charset#defaultCharset()} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @throws NullPointerException {@code stream} is {@code null}.
     *
     * @see #OutputStreamLogTarget(int, OutputStream, Charset, LogFormatter, LogFilter, int)
     */
    public OutputStreamLogTarget(int level, OutputStream stream, Charset charset, LogFilter filter) {
        this(level, stream, charset, null, filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param stream The target where to write the messages into.
     * @param charset The charset to use. If this is {@code null},
     *        {@link Charset#defaultCharset()} is used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code stream} is {@code null}.
     *
     * @see #OutputStreamLogTarget(int, OutputStream, Charset, LogFormatter, LogFilter, int)
     */
    public OutputStreamLogTarget(int level, OutputStream stream, Charset charset, int messageWaitTimeout) {
        this(level, stream, charset, null, null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter}.
     *
     * @param level The level of the log messages to write.
     * @param stream The target where to write the messages into.
     * @param charset The charset to use. If this is {@code null},
     *        {@link Charset#defaultCharset()} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code stream} is {@code null}.
     *
     * @see #OutputStreamLogTarget(int, OutputStream, Charset, LogFormatter, LogFilter, int)
     */
    public OutputStreamLogTarget(int level, OutputStream stream, Charset charset, LogFilter filter, int messageWaitTimeout) {
        this(level, stream, charset, null, filter, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param stream The target where to write the messages into.
     * @param charset The charset to use. If this is {@code null},
     *        {@link Charset#defaultCharset()} is used.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     *
     * @throws NullPointerException {@code stream} is {@code null}.
     *
     * @see #OutputStreamLogTarget(int, OutputStream, Charset, LogFormatter, LogFilter, int)
     */
    public OutputStreamLogTarget(int level, OutputStream stream, Charset charset, LogFormatter formatter) {
        this(level, stream, charset, formatter, null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param stream The target where to write the messages into.
     * @param charset The charset to use. If this is {@code null},
     *        {@link Charset#defaultCharset()} is used.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @throws NullPointerException {@code stream} is {@code null}.
     *
     * @see #OutputStreamLogTarget(int, OutputStream, Charset, LogFormatter, LogFilter, int)
     */
    public OutputStreamLogTarget(int level, OutputStream stream, Charset charset, LogFormatter formatter, LogFilter filter) {
        this(level, stream, charset, formatter, filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param stream The target where to write the messages into.
     * @param charset The charset to use. If this is {@code null},
     *        {@link Charset#defaultCharset()} is used.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code stream} is {@code null}.
     *
     * @see #OutputStreamLogTarget(int, OutputStream, Charset, LogFormatter, LogFilter, int)
     */
    public OutputStreamLogTarget(int level, OutputStream stream, Charset charset, LogFormatter formatter, int messageWaitTimeout) {
        this(level, stream, charset, formatter, null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param level The level of the log messages to write.
     * @param stream The target where to write the messages into.
     * @param charset The charset to use. If this is {@code null},
     *        {@link Charset#defaultCharset()} is used.
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
     * @throws NullPointerException {@code stream} is {@code null}.
     */
    public OutputStreamLogTarget(int level, OutputStream stream, Charset charset, LogFormatter formatter, LogFilter filter, int messageWaitTimeout) {
        super(level, new OutputStreamWriter(stream, charset != null ? charset : Charset.defaultCharset()), formatter, filter, messageWaitTimeout);
        this.stream = stream;
    }

    /**
     * Gets the stream that was set at construction.
     *
     * @return The stream.
     */
    public OutputStream getStream() {
        return stream;
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * <p>
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     * <p>
     * This implementation closes the stream.
     *
     * @throws Exception if this resource cannot be closed.
     */
    @Override
    public void close() throws Exception {
        stream.close();
        super.close();
    }
}
