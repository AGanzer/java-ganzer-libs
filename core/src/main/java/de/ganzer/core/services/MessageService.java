package de.ganzer.core.services;

import de.ganzer.core.internals.CoreMessages;

/**
 * Defines the service to use to display a simple message box.
 */
public interface MessageService {
    /**
     * The type of the message to display.
     */
    enum Type {
        /**
         * Not specified.
         */
        NONE,
        /**
         * An information message.
         */
        INFORMATION,
        /**
         * A Question.
         */
        CONFIRMATION,
        /**
         * A warning message.
         */
        WARNING,
        /**
         * An error message.
         */
        ERROR
    }

    /**
     * The OK button. This can be used to compare the result of an invocation
     * of a {@link #show} method.
     */
    String OK_BUTTON = CoreMessages.get("ok");
    /**
     * The Cancel button. This can be used to compare the result of an invocation
     * of a {@link #show} method.
     */
    String CANCEL_BUTTON = CoreMessages.get("cancel");
    /**
     * The Yes button. This can be used to compare the result of an invocation
     * of a {@link #show} method.
     */
    String YES_BUTTON = CoreMessages.get("yes");
    /**
     * The No button. This can be used to compare the result of an invocation
     * of a {@link #show} method.
     */
    String NO_BUTTON = CoreMessages.get("no");
    /**
     * The Abort button. This can be used to compare the result of an invocation
     * of a {@link #show} method.
     */
    String ABORT_BUTTON = CoreMessages.get("abort");
    /**
     * The Retry button. This can be used to compare the result of an invocation
     * of a {@link #show} method.
     */
    String RETRY_BUTTON = CoreMessages.get("retry");
    /**
     * The Ignore button. This can be used to compare the result of an invocation
     * of a {@link #show} method.
     */
    String IGNORE_BUTTON = CoreMessages.get("ignore");

    /**
     * The OK button. This is a shortcut for using the methods that requires
     * buttons.
     */
    String[] BUTTONS_OK = new String[] { OK_BUTTON };
    /**
     * The OK and Cancel buttons. This is a shortcut for using the methods that
     * requires buttons.
     */
    String[] BUTTONS_OK_CANCEL = new String[] { OK_BUTTON, CANCEL_BUTTON };
    /**
     * The Yes and No buttons. This is a shortcut for using the methods that
     * requires buttons.
     */
    String[] BUTTONS_YES_NO = new String[] { YES_BUTTON, NO_BUTTON };
    /**
     * The Yes, No and Cancel buttons. This is a shortcut for using the methods
     * that requires buttons.
     */
    String[] BUTTONS_YES_NO_CANCEL = new String[] { YES_BUTTON, NO_BUTTON, CANCEL_BUTTON };
    /**
     * The Abort, Retry and Ignore buttons. This is a shortcut for using the
     * methods that requires buttons.
     */
    String[] BUTTONS_ABORT_RETRY_IGNORE = new String[] { ABORT_BUTTON, RETRY_BUTTON, IGNORE_BUTTON };

    /**
     * Shows a message box of type {@link Type#INFORMATION} a single OK button
     * and a default title.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param message The message to display.
     */
    default void show(Object parent, String message) {
        show(parent, message, null, Type.INFORMATION, BUTTONS_OK, null);
    }

    /**
     * Shows a message box with a single OK button and a default title.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param message The message to display.
     * @param type The type of the icon to display.
     */
    default void show(Object parent, String message, Type type) {
        show(parent, message, null, type, BUTTONS_OK, null);
    }

    /**
     * Shows a message box of type {@link Type#CONFIRMATION} and a default title
     * from the specified arguments.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param message The message to display.
     * @param buttons The buttons to display.
     *
     * @return The button the user has clicked to close the message box or
     *         {@code null} if the message box is closed without a button.
     */
    default String show(Object parent, String message, String[] buttons) {
        return show(parent, message, null, Type.CONFIRMATION, buttons, null);
    }

    /**
     * Shows a message box of type {@link Type#CONFIRMATION} and a default title
     * from the specified arguments.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param message The message to display.
     * @param buttons The buttons to display.
     * @param initialButton The initially selected button or {@code null} to
     *        select the first button initially.
     *
     * @return The button the user has clicked to close the message box or
     *         {@code null} if the message box is closed without a button.
     */
    default String show(Object parent, String message, String[] buttons, String initialButton) {
        return show(parent, message, null, Type.CONFIRMATION, buttons, initialButton);
    }

    /**
     * Shows a message box with a default title from the specified arguments.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param message The message to display.
     * @param type The type of the icon to display.
     * @param buttons The buttons to display.
     * @param initialButton The initially selected button or {@code null} to
     *        select the first button initially.
     *
     * @return The button the user has clicked to close the message box or
     *         {@code null} if the message box is closed without a button.
     */
    default String show(Object parent, String message, Type type, String[] buttons, String initialButton) {
        return show(parent, message, null, type, buttons, initialButton);
    }

    /**
     * Shows a message box of type {@link Type#INFORMATION} with a single OK
     * button.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param message The message to display.
     * @param title The title of the message box or {@code null} to display a
     *        default or no title.
     */
    default void show(Object parent, String message, String title) {
        show(parent, message, title, Type.INFORMATION, BUTTONS_OK, null);
    }

    /**
     * Shows a message box with a single OK button from the specified arguments.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param message The message to display.
     * @param title The title of the message box or {@code null} to display a
     *        default or no title.
     * @param type The type of the icon to display.
     */
    default void show(Object parent, String message, String title, Type type) {
        show(parent, message, title, type, BUTTONS_OK, null);
    }

    /**
     * Shows a message box of type {@link Type#CONFIRMATION} from the specified
     * arguments.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param message The message to display.
     * @param title The title of the message box or {@code null} to display a
     *        default or no title.
     * @param buttons The buttons to display.
     *
     * @return The button the user has clicked to close the message box or
     *         {@code null} if the message box is closed without a button.
     */
    default String show(Object parent, String message, String title, String[] buttons) {
        return show(parent, message, title, Type.CONFIRMATION, buttons, null);
    }

    /**
     * Shows a message box of type {@link Type#CONFIRMATION} from the specified
     * arguments.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param message The message to display.
     * @param title The title of the message box or {@code null} to display a
     *        default or no title.
     * @param buttons The buttons to display.
     * @param initialButton The initially selected button or {@code null} to
     *        select the first button initially.
     *
     * @return The button the user has clicked to close the message box or
     *         {@code null} if the message box is closed without a button.
     */
    default String show(Object parent, String message, String title, String[] buttons, String initialButton) {
        return show(parent, message, title, Type.CONFIRMATION, buttons, initialButton);
    }

    /**
     * Shows a message box from the specified arguments.
     *
     * @param parent The parent that owns the message box or {@code null} to use
     *        the currently focused window. The box is displayed relatively to
     *        this parent.
     * @param message The message to display.
     * @param title The title of the message box or {@code null} to display a
     *        default or no title.
     * @param type The type of the icon to display.
     * @param buttons The buttons to display.
     * @param initialButton The initially selected button or {@code null} to
     *        select the first button initially.
     *
     * @return The button the user has clicked to close the message box or
     *         {@code null} if the message box is closed without a button.
     */
    String show(Object parent, String message, String title, Type type, String[] buttons, String initialButton);
}
