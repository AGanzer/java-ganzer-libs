package de.ganzer.swing.logging;

import de.ganzer.core.logging.*;

import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A log target that writes into a {@link JTextComponent}.
 * <p>
 * If the component given at construction is of type {@link JEditorPane},
 * the messages are formatted to HTML if its content type contains
 * "text/html"; otherwise, the messages are written as plain text.
 *
 * @see #isFormatHTML()
 * @see #getErrorColor()
 * @see #getWarningColor()
 * @see #isErrorLevel(int)
 * @see #isWarningLevel(int)
 * @see #write(int, String)
 *
 * @since 5.4.0
 */
public class TextComponentLogTarget extends FormattedLogTarget {
    private final JTextComponent component;
    private final JEditorPane pane;

    private boolean formatHTML = true;

    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param component The component where to write the messages into.
     *
     * @throws NullPointerException {@code pane} is {@code null}.
     *
     * @see #TextComponentLogTarget(int, JTextComponent, LogFormatter, LogFilter, int)
     */
    public TextComponentLogTarget(int level, JTextComponent component) {
        this(level, component, null, null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param component The component where to write the messages into.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @throws NullPointerException {@code pane} is {@code null}.
     *
     * @see #TextComponentLogTarget(int, JTextComponent, LogFormatter, LogFilter, int)
     */
    public TextComponentLogTarget(int level, JTextComponent component, LogFilter filter) {
        this(level, component, null, filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param component The component where to write the messages into.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code pane} is {@code null}.
     *
     * @see #TextComponentLogTarget(int, JTextComponent, LogFormatter, LogFilter, int)
     */
    public TextComponentLogTarget(int level, JTextComponent component, int messageWaitTimeout) {
        this(level, component, null, null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter}.
     *
     * @param level The level of the log messages to write.
     * @param component The component where to write the messages into.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code pane} is {@code null}.
     *
     * @see #TextComponentLogTarget(int, JTextComponent, LogFormatter, LogFilter, int)
     */
    public TextComponentLogTarget(int level, JTextComponent component, LogFilter filter, int messageWaitTimeout) {
        this(level, component, null, filter, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified argument with a formatter of
     * type {@link DefaultLogFormatter}, a filter of an instance of
     * {@link DefaultLogFilter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param component The component where to write the messages into.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     *
     * @throws NullPointerException {@code pane} is {@code null}.
     *
     * @see #TextComponentLogTarget(int, JTextComponent, LogFormatter, LogFilter, int)
     */
    public TextComponentLogTarget(int level, JTextComponent component, LogFormatter formatter) {
        this(level, component, formatter, null, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and a message wait timeout of 0.
     *
     * @param level The level of the log messages to write.
     * @param component The component where to write the messages into.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param filter the filter to use to determine whether a message is written.
     *         If this is {@code null}, an instance of {@link DefaultLogFilter} is
     *         used.
     *
     * @throws NullPointerException {@code pane} is {@code null}.
     *
     * @see #TextComponentLogTarget(int, JTextComponent, LogFormatter, LogFilter, int)
     */
    public TextComponentLogTarget(int level, JTextComponent component, LogFormatter formatter, LogFilter filter) {
        this(level, component, formatter, filter, 0);
    }

    /**
     * Creates a new instance from the specified arguments with a formatter of
     * type {@link DefaultLogFormatter} and filter of an instance of
     * {@link DefaultLogFilter}.
     *
     * @param level The level of the log messages to write.
     * @param component The component where to write the messages into.
     * @param formatter The formatter to use. If this is {@code null}, an instance
     *         of {@link DefaultLogFormatter} is used.
     * @param messageWaitTimeout The timeout in milliseconds to wait until
     *         messages are joined to a single message. See {@link #write(LogInfo[])}
     *         for a more detailed explanation. If this is less than 1,
     *         incoming messages are not joined.
     *
     * @throws NullPointerException {@code pane} is {@code null}.
     *
     * @see #TextComponentLogTarget(int, JTextComponent, LogFormatter, LogFilter, int)
     */
    public TextComponentLogTarget(int level, JTextComponent component, LogFormatter formatter, int messageWaitTimeout) {
        this(level, component, formatter, null, messageWaitTimeout);
    }

    /**
     * Creates a new instance from the specified arguments.
     *
     * @param level The level of the log messages to write.
     * @param component The component where to write the messages into.
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
     * @throws NullPointerException {@code pane} is {@code null}.
     */
    public TextComponentLogTarget(int level, JTextComponent component, LogFormatter formatter, LogFilter filter, int messageWaitTimeout) {
        super(level, formatter, filter, messageWaitTimeout);
        Objects.requireNonNull(component, "pane must not be null.");

        this.component = component;
        this.pane = component instanceof JEditorPane ? (JEditorPane) component : null;
    }

    /**
     * Gets the component that was set at construction.
     *
     * @return The component.
     */
    public JTextComponent getComponent() {
        return component;
    }

    /**
     * Gets a value indicating whether {@link #write(int, String)} formats
     * the message to HTML if the component supports HTML.
     *
     * @return {@code true} to format each message to HTML. This is {@code true}
     *         by default.
     *
     * @see #getWarningColor()
     * @see #getErrorColor()
     */
    public boolean isFormatHTML() {
        return formatHTML;
    }

    /**
     * Sets a value indicating whether {@link #write(int, String)} formats
     * the message to HTML if the component supports HTML.
     * <p>
     * If this is set to {@code false}, the formatter should format the messages
     * to HTML if HTML is supported. Otherwise, the masses will not be displayed
     * correctly within the component.
     *
     * @param formatHTML {@code true} to format each message to HTML.
     *
     * @see #getWarningColor()
     * @see #getErrorColor()
     */
    public void setFormatHTML(boolean formatHTML) {
        this.formatHTML = formatHTML;
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
     */
    @Override
    protected void write(LogInfo[] info) {
        for (LogInfo logInfo : info)
            write(logInfo.getLevel(), getFormatter().format(logInfo));
    }

    /**
     * Called by {@link #write(LogInfo[])} to write the messages that are not
     * discarded by a filter into the physical target.
     * <p>
     * If the component given at construction is of type {@link JEditorPane},
     * the messages are formatted to HTML if its content type contains
     * "text/html"; otherwise, the messages are written as plain text.
     * <p>
     * This implementation calls {@link #isWarningLevel(int)} and
     * {@link #isErrorLevel(int)} to query whether the message should be
     * written as error or warning.
     *
     * @param level The log level of the message to log. This is given as
     *         information and should not be inserted into {@code messsage}.
     *         The level of joined messages is {@link Integer#MIN_VALUE}.
     * @param message The message to write. This is already formatted and must
     *         simply be written into the target as is.
     *
     * @see #getErrorColor()
     * @see #getWarningColor()
     */
    @Override
    protected void write(int level, String message) {
        if (pane != null && pane.getContentType().contains("text/html"))
            writeHTML(level, message + "<br>");
        else
            writePlain(level, message + "\n");
    }

    /**
     * Called to get the color used for warning messages.
     * <p>
     * The returned color is inserted into a style of the format
     * "style='color:%s;'" and must be interpretable.
     * <p>
     * This is called only if {@link #isFormatHTML()} returns {@code true} and
     * if the component given at construction is of type {@link JEditorPane}
     * and its content type contains "text/html".
     *
     * @return The color to use. This implementation returns "orange".
     */
    protected String getWarningColor() {
        return "orange";
    }

    /**
     * Called to get the color used for error messages.
     * <p>
     * The returned color is inserted into a style of the format
     * "style='color:%s;'" and must be interpretable.
     * <p>
     * This is called only if {@link #isFormatHTML()} returns {@code true} and
     * if the component given at construction is of type {@link JEditorPane}
     * and its content type contains "text/html".
     *
     * @return The color to use. This implementation returns "red".
     */
    protected String getErrorColor() {
        return "red";
    }

    /**
     * Called to get the information whether a log level indicates a warning.
     * <p>
     * Inheritors should override this to implement its own warning level
     * detection.
     * <p>
     * This is called only if {@link #isFormatHTML()} returns {@code true} and
     * if the component given at construction is of type {@link JEditorPane}
     * and its content type contains "text/html".
     *
     * @param level The level to query.
     *
     * @return {@code true} if {@code level} indicates a warning. This
     *         implementation returns {@code level == 1}.
     */
    protected boolean isWarningLevel(int level) {
        return level == 1;
    }

    /**
     * Called to get the information whether a log level indicates an error.
     * <p>
     * Inheritors should override this to implement its own error level
     * detection.
     * <p>
     * This is called only if {@link #isFormatHTML()} returns {@code true} and
     * if the component given at construction is of type {@link JEditorPane}
     * and its content type contains "text/html".
     *
     * @param level The level to query.
     *
     * @return {@code true} if {@code level} indicates an error. This
     *         implementation returns {@code level == 0}.
     */
    protected boolean isErrorLevel(int level) {
        return level == 0;
    }

    private void writeHTML(int level, String message) {
        if (formatHTML) {
            message = message.replace("\n", "<br>");

            if (isErrorLevel(level))
                message = String.format("<span style='color:%s;'>%s</span>", getErrorColor(), message);
            else if (isWarningLevel(level))
                message = String.format("<span style='color:%s;'>%s</span>", getWarningColor(), message);
        }

        var kit = (HTMLEditorKit) pane.getEditorKit();
        var doc = (HTMLDocument) pane.getDocument();

        try {
            boolean scrollToEnd = pane.getCaretPosition() ==  pane.getDocument().getLength();

            kit.insertHTML(doc, doc.getLength(), message, 0, 0, null);

            if (scrollToEnd)
                pane.setCaretPosition(pane.getDocument().getLength());
        } catch (Exception ex) {
            // Ignore.
        }
    }

    private void writePlain(int ignored, String message) {
        var doc = component.getDocument();

        try {
            boolean scrollToEnd = component.getCaretPosition() ==  component.getDocument().getLength();

            doc.insertString(doc.getLength(), message + "\n", null);

            if (scrollToEnd)
                component.setCaretPosition(component.getDocument().getLength());
        } catch (Exception ex) {
            // Ignore.
        }
    }
}
