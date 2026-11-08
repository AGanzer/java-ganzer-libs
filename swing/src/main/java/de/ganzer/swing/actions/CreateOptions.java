package de.ganzer.swing.actions;

import javax.swing.*;

/**
 * Options for creating a button by an implementation of {@link GActionItemBuilder}.
 *
 * @see GActionItemBuilder#createButton(int)
 * @see GActionItemBuilder#addButtons(JToolBar, int)
 */
@SuppressWarnings("unused")
public final class CreateOptions {
    /**
     * The buttons are created with default settings.
     */
    public static final int DEFAULT = 0x00;

    /**
     * The created buttons are focusable.
     *
     * @deprecated This is ignored since 6.5.0. Buttons are focusable by default
     *             now.
     */
    @Deprecated(since = "6.5.0", forRemoval = true)
    public static final int FOCUSABLE = 0x00;

    /**
     * The text of the buttons are not hidden.
     *<p>
     * Normally, the text of a button is visible only if no image is set. This
     * option forces a visible text even if an image is set. If no image is set,
     * the button's text ist always visible.
     */
    public static final int SHOW_TEXT = 0x02;

    /**
     * The image of the buttons are not visible.
     */
    public static final int HIDE_IMAGE = 0x04;

    /**
     * The images of the buttons are not above the texts but leads them.
     */
    public static final int IMAGE_LEADING = 0x08;

    /**
     * The created buttons have no border.
     * <p>
     * This sets the paint border property of a button and might not have any
     * effect. See {@link AbstractButton#setBorderPainted(boolean)} for more
     * details.
     *
     * @deprecated since 6.5.0. Use {@link #TOOL_BUTTON} instead.
     */
    @Deprecated(since = "6.5.0", forRemoval = true)
    public static final int NO_BORDER = 0x10;

    /**
     * The created buttons are like toolbar buttons.
     * <p>
     * Tool-buttons does not have a border and are not focusable. They also
     * reacts on a mouse hover to repaint its status.
     * <p>
     * If a button is created to put it into a {@link JToolBar}, this option
     * is not needed to be set. It is useful to create a toolbar-like button
     * that shall be inserted into a panel other than a {@link JToolBar}.
     * <p>
     * This Options deprecates {@link #NO_BORDER}.
     *
     * @since 6.5.0
     */
    public static final int TOOL_BUTTON = 0x10;

    /**
     * The small image shall be used even if there is a large image available.
     */
    public static final int SMALL_IMAGE = 0x20;

    /**
     * The short name shall be used if available.
     */
    public static final int SHORT_NAME = 0x40;

    /**
     * Gets a value that indicates whether the specified {@code value} is set
     * in the specified {@code options}.
     * <p>
     * This is a shortcut for {@code (options & value) != 0}.
     *
     * @param options The options that shall be checked.
     * @param value The value that shall be queried.
     *
     * @return {@code true} if {@code value} is set in {@code options};
     *         otherwise, {@code false}.
     */
    public static boolean isSet(int options, int value) {
        return (options & value) != 0;
    }
}
