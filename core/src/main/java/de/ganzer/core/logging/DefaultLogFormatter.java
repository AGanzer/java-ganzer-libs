package de.ganzer.core.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * This class defines the default formatter for formatting log outputs.
 * <p>
 * This class do the default formatting and is used by each target if no other
 * formatter is specified for a target.
 */
public class DefaultLogFormatter extends LogFormatter {
    /**
     * The format string that is used if no other is specified.
     * <p>
     * Ths string separates each information by Tab characters an includes the
     * thread information into parenthesis. Escape sequences in the format
     * string are used in the following way:
     *
     * <ul>
     *     <li>Sequence %1$s is used for the sequence number gotten by
     *        {@link #formatMessageNumber(int)}
     *     <li>Sequence %2$s is used for the thread ID gotten by
     *        {@link #formatThreadID(long)}
     *     <li>Sequence %3$s is used for the thread name gotten by
     *        {@link #formatThreadName(String)}
     *     <li>Sequence %4$s is used for the message level gotten by
     *        {@link #formatLevel(int)}
     *     <li>Sequence %5$s is used for the message time gotten by
     *        {@link #formatTime(LocalDateTime)}
     *     <li>Sequence %6$s is used for the message itself gotten by
     *        {@link #formatMessage(String)}
     * </ul>
     */
    public static final String DEFAULT_FORMAT = "%1$s\t(%2$s - %3$s)\t%4$s\t%5$s\t%6$s";

    private final String formatString;

    /**
     * Creates a new instance with the default locale.
     */
    public DefaultLogFormatter() {
        formatString = DEFAULT_FORMAT;
    }

    /**
     * Creates a new instance from the specified argument.
     *
     * @param locale The locale to use for formatting numbers and times.
     */
    public DefaultLogFormatter(Locale locale) {
        super(locale);
        formatString = DEFAULT_FORMAT;
    }

    /**
     * Creates a new instance from the specified argument.
     *
     * @param locale The locale to use for formatting numbers and times.
     */
    public DefaultLogFormatter(String formatString, Locale locale) {
        super(locale);
        this.formatString = formatString;
    }

    /**
     * Called to format a message from the specified info.
     *
     * @param info The info where to format the message from.
     *
     * @return The formatted message.
     */
    @Override
    public final String format(LogInfo info) {
        return String.format(formatString,
                             formatMessageNumber(info.getMessageNumber()),
                             formatThreadID(info.getThreadID()),
                             formatThreadName(info.getThreadName()),
                             formatLevel(info.getLevel()),
                             formatTime(info.getTime()),
                             formatMessage(info.getMessage()));
    }

    /**
     * Called to format the sequence number of the message.
     *
     * @param messageNumber The sequence number of the message.
     *
     * @return The formatted number. This implementation returns
     *         {@code Integer.toString(messageNumber)}.
     */
    protected String formatMessageNumber(int messageNumber) {
        return Integer.toString(messageNumber);
    }

    /**
     * Called to format the thread ID.
     *
     * @param threadID The ID of the thread.
     *
     * @return the formatted ID. This implementation returns
     *         {@code Long.toString(threadID)}.
     */
    protected String formatThreadID(long threadID) {
        return Long.toString(threadID);
    }

    /**
     * Called to format the thread name.
     *
     * @param threadName The name of the thread.
     *
     * @return The formatted name. This implementation returns {@code threadName}.
     */
    protected String formatThreadName(String threadName) {
        return threadName;
    }

    /**
     * Called to format the level of the message.
     *
     * @param level The level of the message.
     *
     * @return The formatted level. This implementation returns
     *         {@code Integer.toString(messageNumber)}.
     */
    protected String formatLevel(int level) {
        return Integer.toString(level);
    }

    /**
     * Called to format the time of the message.
     *
     * @param time The time of the message.
     *
     * @return The formatted time. This implementation returns
     *         {@code DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", getLocale()).format(time)}.
     */
    protected String formatTime(LocalDateTime time) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", getLocale());
        return formatter.format(time);
    }

    /**
     * Called to format the message.
     *
     * @param message The message.
     *
     * @return nThe formatted message. This implementation returns {@code message}.
     */
    protected String formatMessage(String message) {
        return message;
    }
}
