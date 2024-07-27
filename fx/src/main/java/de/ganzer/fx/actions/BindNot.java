package de.ganzer.fx.actions;

import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;

/**
 * Provides named constants to indicate the properties of an action that shall
 * not be bound to a control.
 *
 * @see Action
 * @see Action#bindTo(MenuItem, int)
 * @see Action#bindTo(ButtonBase, ImageSize, int)
 */
public final class BindNot {
    /**
     * Don't bind the command text.
     */
    public static final int COMMAND_TEXT = 0x01;

    /**
     * Don't bind the tooltip text.
     */
    public static final int TOOLTIP_TEXT = 0x02;

    /**
     * Don't bind the accelerator.
     */
    public static final int ACCELERATOR = 0x04;

    /**
     * Don't bind the enabled status.
     */
    public static final int DISABLED = 0x08;

    /**
     * Don't bind the selection status.
     */
    public static final int SELECTED = 0x10;

    /**
     * Don't bind the visibility.
     */
    public static final int VISIBLE = 0x20;

    /**
     * Don't bind the image. For Buttons, this is identical to using
     * {@link ImageSize#NONE}.
     */
    public static final int IMAGE = 0x40;

    /**
     * Don't bind the action handler (for menu buttons).
     */
    public static final int ACTION = 0x80;
}
