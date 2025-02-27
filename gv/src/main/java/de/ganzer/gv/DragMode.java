package de.ganzer.gv;

import de.ganzer.gv.core.Position;

/**
 * Defines the kinds of dragging and moving.
 *
 * @see View#dragByMouse
 */
public enum DragMode {
    /**
     * The left edge of the view is dragged by the mouse.
     */
    LEFT,
    /**
     * The right edge of the view is dragged by the mouse.
     */
    RIGHT,
    /**
     * The top edge of the view is dragged by the mouse.
     */
    TOP,
    /**
     * The bottom edge of the view is dragged by the mouse.
     */
    BOTTOM,
    /**
     * The top left edge of the view is dragged by the mouse.
     */
    TOP_LEFT,
    /**
     * The top right edge of the view is dragged by the mouse.
     */
    TOP_RIGHT,
    /**
     * The bottom left edge of the view is dragged by the mouse.
     */
    BOTTOM_LEFT,
    /**
     * The bottom right edge of the view is dragged by the mouse.
     */
    BOTTOM_RIGHT,
    /**
     * The view is moved by the mouse.
     */
    MOVE
}
