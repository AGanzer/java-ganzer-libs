package de.ganzer.gv;

/**
 * The FocusOrigin enumeration defines the start point from where to search
 * for selectable controls.
 *
 * @see View#focusNext
 */
public enum FocusOrigin {
    /**
     * Select the next selectable view near the currently active view. If no
     * view is currently active, this value is equal to {@link #FIRST} if the
     * {@code forward} argument is {@code true} and it is equal to {@link #LAST}
     * if the {@code forward} argument is {@code false}.
     */
    CURRENT,
    /**
     * Select the first selectable view.
     */
    FIRST,
    /**
     * Select the last selectable view.
     */
    LAST
}
