package de.ganzer.dv;

/**
 * This interface is used to provide additional functionality to the DVManager.
 */
public interface DVManagerSupport {
    /**
     * Gets the view that currently has the focus.
     *
     * @return the active view or {@code null} if no view is active.
     */
    View<?> getActiveView();
}
