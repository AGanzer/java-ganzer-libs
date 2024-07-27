package de.ganzer.fx.actions;

import javafx.scene.control.ButtonBase;

/**
 * Indicates the size of the image that shall be bound to a button.
 *
 * @see Action
 * @see Action#bindTo(ButtonBase, ImageSize)
 * @see Action#bindTo(ButtonBase, ImageSize, int)
 */
public enum ImageSize {
    /**
     * Don't bind any image. This is identical to using {@link BindNot#IMAGE}.
     */
    NONE,

    /**
     * Bind the small image.
     */
    SMALL,

    /**
     * Bind the medium image.
     */
    MEDIUM,

    /**
     * Bind the large image.
     */
    LARGE
}
