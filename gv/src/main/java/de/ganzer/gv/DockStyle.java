package de.ganzer.gv;

/**
 * The DockStyle enumeration defines the possible docking styles of a view.
 */
public enum DockStyle {
    /**
     * The view is not docked.
     */
    NONE,
    /**
     * The view is docked on the left edge of the parent view.
     */
    LEFT,
    /**
     * The view is docked on the right edge of the parent view.
     */
    RIGHT,
    /**
     * The view is docked on the top edge of the parent view.
     */
    TOP,
    /**
     * The view is docked on the bottom edge of the parent view.
     */
    BOTTOM,
    /**
     * The view is docked on all edges of the parent view.
     */
    FILL
}
