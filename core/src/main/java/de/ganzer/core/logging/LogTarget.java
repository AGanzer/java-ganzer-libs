package de.ganzer.core.logging;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The LogTarget class defines an abstract target for log messages.
 *
 * @see Logger
 *
 * @since 5.4.0
 */
public abstract class LogTarget implements AutoCloseable {
    private final int level;
    private final LogFilter filter;
    private final int messageWaitTimeout;
    private final Queue<LogInfo> pendingMessages = new LinkedList<>();
    private final AtomicBoolean closed = new AtomicBoolean(false);

    private Logger owner;
    private int messageNumber;
    private ReentrantLock writeLock;
    private Condition writeCondition;
    private MessageWorker messageWorker;
    private MessageWakeup messageWakeup;

    /**
     * Creates a new instance from the specified argument with a filter of an
     * instance of {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     *
     * @see #LogTarget(int, int)
     * @see #LogTarget(int, LogFilter, int)
     * @see #getMessageWaitTimeout()
     */
    public LogTarget(int level) {
        this(level,null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a message wait
     * timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param filter the filter to use to determine whether a message is written.
     *        If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *        used.
     *
     * @see #LogTarget(int, int)
     * @see #LogTarget(int, LogFilter, int)
     * @see #getMessageWaitTimeout()
     */
    public LogTarget(int level, LogFilter filter) {
        this(level,filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a filter of an
     *      * instance of {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *        messages are joined to a single message. See {@link #write(LogInfo[])}
     *        for a more detailed explanation. If this is less than 1,
     *        incoming messages are not joined.
     */
    public LogTarget(int level, int messageWaitTimeout) {
        this(level,null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param level The level of the log messages to write.
     * @param filter the filter to use to determine whether a message is written.
     *        If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *        used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *        messages are joined to a single message. See {@link #write(LogInfo[])}
     *        for a more detailed explanation. If this is less than 1,
     *        incoming messages are not joined.
     */
    public LogTarget(int level, LogFilter filter, int messageWaitTimeout) {
        this.level = level;
        this.filter = filter != null ? filter : new DefaultLogFilter();
        this.messageWaitTimeout = messageWaitTimeout;

        if (messageWaitTimeout > 0) {
            writeLock = new ReentrantLock();
            writeCondition = writeLock.newCondition();

            messageWorker = new MessageWorker();
            messageWakeup = new MessageWakeup();

            messageWorker.start();
            messageWakeup.start();
        }
    }

    /**
     * Gets the level set at construction.
     *
     * @return The log level of this target.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the filter set at construction.
     *
     * @return The filter for the messages to write or {@link DefaultLogFilter}
     *         if no filter was set.
     */
    public LogFilter getFilter() {
        return filter;
    }

    /**
     * Gets the timeout to wait for incoming messages.
     *
     * @return The timeout in milliseconds to wait for incoming messages. If
     *         this is less than 1, each message is written as a single message.
     *         See {@link #write(LogInfo[])} for a more detailed explanation.
     *
     * @see #LogTarget(int, int)
     * @see #LogTarget(int, LogFilter, int)
     */
    public int getMessageWaitTimeout() {
        return messageWaitTimeout;
    }

    /**
     * Gets a value indicating whether this target is closed.
     *
     * @return {@code true} if {@link #close()} has been called.
     */
    public boolean isClosed() {
        return closed.get();
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * <p>
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     * <p>
     * This implementation sets {@link #isClosed()} to true. Inheritors that
     * override this must call the base method after it has closed its used
     * resources.
     *
     * @throws Exception if this resource cannot be closed.
     */
    @Override
    public void close() throws Exception {
        if (messageWaitTimeout > 0) {
            messageWakeup.cancel();
            messageWorker.cancel();
        }

        closed.set(true);
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
     *
     * @throws IllegalStateException If this target has been closed.
     */
    public final void write(int level, LocalDateTime time, String message) {
        if (closed.get())
            throw new IllegalStateException("Target has been closed.");

        if (!filter.shouldWrite(level, this.level))
            return;

        var info = new LogInfo(++messageNumber, level, time, Thread.currentThread().getId(), Thread.currentThread().getName(), message);

        if (messageWaitTimeout < 1) {
            try {
                write(new LogInfo[] {info});
            } catch (Exception e) {
                // This must not throw any exception!
                e.printStackTrace(System.err);
            }
        } else {
            synchronized (pendingMessages) {
                pendingMessages.add(info);
            }
        }
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
     *
     * @param info The information about the messages to write.
     *
     * @throws IOException on any I/O error.
     *
     * @see #LogTarget(int, int)
     * @see #LogTarget(int, LogFilter, int)
     */
    protected abstract void write(LogInfo[] info) throws IOException;

    final Logger getOwner() {
        return owner;
    }

    final void setOwner(Logger owner) {
        this.owner = owner;
    }

    private class MessageWorker extends Thread {
        private final AtomicBoolean canceled = new AtomicBoolean(false);

        @Override
        public void run() {
            writeLock.lock();

            try {
                while (!canceled.get()) {
                    var avail = writeCondition.await(100, TimeUnit.MILLISECONDS);

                    if (!avail)
                        continue;

                    List<LogInfo> infos;

                    synchronized (pendingMessages) {
                        infos = new ArrayList<>(pendingMessages);
                    }

                    if (infos.isEmpty())
                        continue;

                    try {
                        write(infos.toArray(new LogInfo[0]));
                    } catch (Exception e) {
                        // This must not throw any exception!
                        e.printStackTrace(System.err);
                    }
                }
            } catch (InterruptedException e) {
                // Ignore.
            } finally {
                writeLock.unlock();
            }
        }

        public void cancel() {
            canceled.set(true);
        }
    }

    private class MessageWakeup extends Thread {
        private final AtomicBoolean canceled = new AtomicBoolean(false);

        @Override
        public void run() {
            writeLock.lock();

            try {
                while (!canceled.get()) {
                    //noinspection BusyWait
                    Thread.sleep(messageNumber);
                    writeCondition.signal();
                }
            } catch (InterruptedException e) {
                // Ignore.
            } finally {
                writeLock.unlock();
            }
        }

        public void cancel() {
            canceled.set(true);
        }
    }
}
