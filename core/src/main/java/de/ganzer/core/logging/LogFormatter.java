package de.ganzer.core.logging;

import java.util.Locale;

/**
 * This class defines an abstract base for formatting messages that are written
 * a {@link LogTarget} instance.
 * <p>
 * Logged messages are formatted before they are written into the physical
 * target. An instance of this class is used to format the message.
 */
public abstract class LogFormatter {
    private final Locale locale;

    /**
     * Creates a new instance with the default locale.
     */
    protected LogFormatter() {
        this.locale = Locale.getDefault();
    }

    /**
     * Creates a new instance from the specified argument.
     *
     * @param locale The locale to use for formatting numbers and times.
     */
    protected LogFormatter(Locale locale) {
        this.locale = locale;
    }

    /**
     * Gets the locale to use for formatting numbers and times.
     *
     * @return The locale to use for formatting numbers and times.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Called to format a message from the specified info.
     *
     * @param info The info where to format the message from.
     *
     * @return The formatted message.
         */
    public abstract String format(LogInfo info);
}
