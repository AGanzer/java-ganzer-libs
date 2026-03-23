package de.ganzer.core.logging;

/**
 * This interface is used for formatting messages that are written into a
 * {@link FormattedLogTarget} instance.
 * <p>
 * Logged messages can be formatted before they are written into the physical
 * target. An instance of each implementing class can be used to format the
 * message.
 *
 * @since 5.4.0
 */
public interface LogFormatter {
    /**
     * Called to format a message from the specified info.
     *
     * @param info The info where to format the message from.
     *
     * @return The formatted message.
     */
    String format(LogInfo info);
}
