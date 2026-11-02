package de.ganzer.swing.dlgfw;

import de.ganzer.core.util.Strings;
import de.ganzer.swing.dlgfw.internals.SwingDialogsMessages;

import javax.swing.Icon;
import javax.swing.UIManager;
import java.awt.BorderLayout;

/**
 * The data used with the {@link MessageDialog}.
 *
 * @since 6.3.0
 */
@SuppressWarnings("unused")
public class MessageDialogData {
    /**
     * The title of the dialog or {@code null} to show no title.
     */
    public final String title;
    /**
     * The message to display. This may be an HTML message with special
     * formatting or a plain message with default '\n' line breaks.
     */
    public final String message;
    /**
     * The icon to display or {@code null} to show no icon.
     */
    public final Icon icon;
    /**
     * The location of the buttons. This is either {@link BorderLayout#SOUTH} or
     * {@link BorderLayout#EAST}.
     */
    public final String buttonLocation;
    /**
     * The texts of the buttons to display.
     */
    public final String[] buttons;
    /**
     * The button that is pressed by the "Enter" key.
     */
    public final String defaultButton;

    /**
     * The result of the dialog. This is one of the button texts.
     */
    public String result;

    /**
     * Creates a new instance with an information title and icon and an "OK"
     * button.
     *
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     */
    public MessageDialogData(String message) {
        this(null, message, null, null, MessageButtons.OK, null, MessageType.INFORMATION);
    }

    /**
     * Creates a new instance with a default title and icon.
     *
     * @param title The title of the dialog or {@code null}, empty or blank to
     *        use the default title for {@code type}.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     */
    public MessageDialogData(String title, String message) {
        this(title, message, null, null, MessageButtons.OK, null, MessageType.INFORMATION);
    }

    /**
     * Creates a new instance with a default title and icon.
     * <p>
     * This shown buttons depends on {@code type}. If this is {@link MessageType#QUESTION},
     * a "YES" and "NO" button is shown. In all other cases a single "OK" button.
     *
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     * @param type The type of the dialog. If this is {@code null}, no title is
     *        shown.
     */
    public MessageDialogData(String message, MessageType type) {
        this(null, message, null, null, getButtons(type), null, type);
    }

    /**
     * Creates a new instance with a default title and icon.
     * <p>
     * This shown buttons depends on {@code type}. If this is {@link MessageType#QUESTION},
     * a "YES" and "NO" button is shown. In all other cases a single "OK" button.
     *
     * @param title The title of the dialog or {@code null}, empty or blank to
     *        use the default title for {@code type}.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     * @param type The type of the dialog. If this is {@code null}, no title is
     *        shown.
     */
    public MessageDialogData(String title, String message, MessageType type) {
        this(title, message, null, null, getButtons(type), null, type);
    }

    /**
     * Creates a new instance with a default title and icon.
     *
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     * @param buttons The buttons to display. If this is {@code null},
     *        {@link MessageButtons#OK} is used.
     * @param defaultButton The button that is pressed by the "Enter" key or
     *        {@code null} to make the first button the default.
     * @param type The type of the dialog. If this is {@code null}, no title is
     *        shown.
     */
    public MessageDialogData(String message, MessageButtons buttons, String defaultButton, MessageType type) {
        this(null, message, null, null, getButtons(buttons), defaultButton, type);
    }

    /**
     * Creates a new instance with a default title and icon.
     *
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     * @param buttonLocation The location of the buttons. This may be
     *        {@link BorderLayout#SOUTH} or {@link BorderLayout#EAST}. If this
     *        is {@code null}, empty or blank, {@link BorderLayout#SOUTH} is
     *        used.
     * @param buttons The buttons to display. If this is {@code null},
     *        {@link MessageButtons#OK} is used.
     * @param defaultButton The button that is pressed by the "Enter" key or
     *        {@code null} to make the first button the default.
     * @param type The type of the dialog. If this is {@code null}, no title is
     *        shown.
     */
    public MessageDialogData(String message, String buttonLocation, MessageButtons buttons, String defaultButton, MessageType type) {
        this(null, message, null, buttonLocation, getButtons(buttons), defaultButton, type);
    }

    /**
     * Creates a new instance.
     *
     * @param title The title of the dialog or {@code null}, empty or blank to
     *        use the default title for {@code type}.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     * @param icon The icon to display or {@code null} to use the default icon
     *        for {@code type}.
     * @param buttons The buttons to display. If this is {@code null},
     *        {@link MessageButtons#OK} is used.
     * @param defaultButton The button that is pressed by the "Enter" key or
     *        {@code null} to make the first button the default.
     * @param type The type of the dialog. If this is {@code null}, no title is
     *        shown if {@code title} and {@code icon} are both {@code null}.
     */
    public MessageDialogData(String title, String message, Icon icon, MessageButtons buttons, String defaultButton, MessageType type) {
        this(title, message, icon, null, getButtons(buttons), defaultButton, type);
    }

    /**
     * Creates a new instance.
     *
     * @param title The title of the dialog or {@code null}, empty or blank to
     *        use the default title for {@code type}.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     * @param icon The icon to display or {@code null} to use the default icon
     *        for {@code type}.
     * @param buttonLocation The location of the buttons. This may be
     *        {@link BorderLayout#SOUTH} or {@link BorderLayout#EAST}. If this
     *        is {@code null}, empty or blank, {@link BorderLayout#SOUTH} is
     *        used.
     * @param buttons The buttons to display. If this is {@code null},
     *        {@link MessageButtons#OK} is used.
     * @param defaultButton The button that is pressed by the "Enter" key or
     *        {@code null} to make the first button the default.
     * @param type The type of the dialog. If this is {@code null}, no title is
     *        shown if {@code title} and {@code icon} are both {@code null}.
     */
    public MessageDialogData(String title, String message, Icon icon, String buttonLocation, MessageButtons buttons, String defaultButton, MessageType type) {
        this(title, message, icon, buttonLocation, getButtons(buttons), defaultButton, type);
    }

    /**
     * Creates a new instance.
     *
     * @param title The title of the dialog or {@code null}, empty or blank to
     *        use the default title for {@code type}.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     * @param icon The icon to display or {@code null} to use the default icon
     *        for {@code type}.
     * @param buttons The texts of the buttons to display. If this is {@code null},
     *        or empty, "OK" is used.
     * @param defaultButton The button that is pressed by the "Enter" key or
     *        {@code null} to make the first button the default.
     * @param type The type of the dialog. If this is {@code null}, no title is
     *        shown if {@code title} and {@code icon} are both {@code null}.
     */
    public MessageDialogData(String title, String message, Icon icon, String[] buttons, String defaultButton, MessageType type) {
        this(title, message, icon, null, buttons, defaultButton, type);
    }

    /**
     * Creates a new instance.
     *
     * @param title The title of the dialog or {@code null}, empty or blank to
     *        use the default title for {@code type}.
     * @param message The message to display. This may be an HTML message with
     *        special formatting or a plain message with default '\n' line breaks.
     * @param icon The icon to display or {@code null} to use the default icon
     *        for {@code type}.
     * @param buttonLocation The location of the buttons. This may be
     *        {@link BorderLayout#SOUTH} or {@link BorderLayout#EAST}. If this
     *        is {@code null}, empty or blank, {@link BorderLayout#SOUTH} is
     *        used.
     * @param buttons The texts of the buttons to display. If this is {@code null},
     *        or empty, "OK" is used.
     * @param defaultButton The button that is pressed by the "Enter" key or
     *        {@code null} to make the first button the default.
     * @param type The type of the dialog. If this is {@code null}, no title is
     *        shown if {@code title} and {@code icon} are both {@code null}.
     */
    public MessageDialogData(String title, String message, Icon icon, String buttonLocation, String[] buttons, String defaultButton, MessageType type) {
        this.title = Strings.isNullOrBlank(title) ? getTitle(type) : title;
        this.message = message;
        this.icon = icon == null ? getIcon(type) : icon;
        this.buttonLocation = Strings.isNullOrBlank(buttonLocation) ? BorderLayout.SOUTH : buttonLocation;
        this.buttons = buttons == null || buttons.length == 0
                ? new String[] {SwingDialogsMessages.get("commons.buttons.ok")}
                : buttons;
        this.defaultButton = defaultButton;
    }

    /**
     * Gets the index of the button that was clicked.
     *
     * @return {@code -1} if {@link #result} is {@code null} or not a text of
     *          a button or the index of the button that was clicked.
     */
    public int getResultIndex() {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].equals(result)) {
                return i;
            }
        }

        return -1;
    }

    private static MessageButtons getButtons(MessageType type) {
        return type == MessageType.QUESTION
                ? MessageButtons.YES_NO
                : MessageButtons.OK;
    }

    private static String[] getButtons(MessageButtons buttons) {
        return buttons == null ? null : switch (buttons) {
            case OK -> new String[] {
                    SwingDialogsMessages.get("commons.buttons.ok")};
            case OK_CANCEL -> new String[] {
                    SwingDialogsMessages.get("commons.buttons.ok"),
                    SwingDialogsMessages.get("commons.buttons.cancel")};
            case YES_NO -> new String[] {
                    SwingDialogsMessages.get("commons.buttons.yes"),
                    SwingDialogsMessages.get("commons.buttons.no")};
            case YES_NO_CANCEL -> new String[] {
                    SwingDialogsMessages.get("commons.buttons.yes"),
                    SwingDialogsMessages.get("commons.buttons.no"),
                    SwingDialogsMessages.get("commons.buttons.cancel")};
            case YES_NO_CANCEL_ALL -> new String[] {
                    SwingDialogsMessages.get("commons.buttons.yes"),
                    SwingDialogsMessages.get("commons.buttons.yesToAll"),
                    SwingDialogsMessages.get("commons.buttons.no"),
                    SwingDialogsMessages.get("commons.buttons.noToAll"),
                    SwingDialogsMessages.get("commons.buttons.cancel")};
            case ABORT_RETRY_IGNORE -> new String[] {
                    SwingDialogsMessages.get("commons.buttons.abort"),
                    SwingDialogsMessages.get("commons.buttons.retry"),
                    SwingDialogsMessages.get("commons.buttons.ignore")};
            case ABORT_RETRY_IGNORE_ALL -> new String[] {
                    SwingDialogsMessages.get("commons.buttons.abort"),
                    SwingDialogsMessages.get("commons.buttons.retry"),
                    SwingDialogsMessages.get("commons.buttons.ignore"),
                    SwingDialogsMessages.get("commons.buttons.ignoreAll")};
        };
    }

    private static String getTitle(MessageType type) {
        return type == null ? null : switch (type) {
            case ERROR -> SwingDialogsMessages.get("dialogs.title.error");
            case WARNING -> SwingDialogsMessages.get("dialogs.title.warning");
            case INFORMATION -> SwingDialogsMessages.get("dialogs.title.information");
            case QUESTION -> SwingDialogsMessages.get("dialogs.title.question");
        };
    }

    private static Icon getIcon(MessageType type) {
        return type == null ? null : switch (type) {
            case ERROR -> UIManager.getIcon("OptionPane.errorIcon");
            case WARNING -> UIManager.getIcon("OptionPane.warningIcon");
            case INFORMATION -> UIManager.getIcon("OptionPane.informationIcon");
            case QUESTION -> UIManager.getIcon("OptionPane.questionIcon");
        };
    }
}
