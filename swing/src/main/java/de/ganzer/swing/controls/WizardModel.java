package de.ganzer.swing.controls;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Collection;

/**
 * The model that is used by a {@link WizardPane}.
 */
public interface WizardModel {
    /**
     * Returns all pages.
     *
     * @return A collection of all added pages. The order of the pages in the
     *         returned collection is not necessarily the order the pages where
     *         added.
     */
    Collection<Object> getPages();

    /**
     * Gets the next page to show after the current page.
     *
     * @return The next page to show or {@code null} if no further page is
     *         available If {@link #getCurrentPage()} returns {@code null},
     *         the first available page is returned.
     */
    Object getNextPage();

    /**
     * Returns the current page.
     *
     * @return The current page or {@code null} of no page is currently set.
     */
    Object getCurrentPage();

    /**
     * Sets the currently active page.
     *
     * @param page The page to set as current page.
     *
     * @return {@code true} if the page was set successfully, {@code false}
     *         otherwise.
     */
    boolean setCurrentPage(Object page);

    /**
     * Returns the Swing component for the given page.
     *
     * @param page The page where to get the component for.
     *
     * @return The Swing component for the given page. This should not return
     *         {@code null}.
     */
    Component getComponent(Object page);

    /**
     * Adds a listener of page changing events.
     * <p>
     * This event is fired before the pages are changed. It is vetoable and can
     * be used to prevent the page change.
     *
     * @param listener The listener to add.
     */
    void addPageChangingListener(VetoableChangeListener listener);

    /**
     * Removes a listener of page changing events.
     *
     * @param listener The listener to remove.
     */
    void removePageChangingListener(VetoableChangeListener listener);

    /**
     * Gets all installed page changing listeners.
     *
     * @return The installed page changing listeners.
     */
    VetoableChangeListener[] getPageChangingListeners();

    /**
     * Adds a listener of page changed events.
     * <p>
     * This event is fired after the pages have changed.
     *
     * @param listener The listener to add.
     */
    void addPageChangedListener(PropertyChangeListener listener);

    /**
     * Removes a listener of page changed events.
     *
     * @param listener The listener to remove.
     */
    void removePageChangedListener(PropertyChangeListener listener);

    /**
     * Gets all installed page changed listeners.
     *
     * @return The installed page changed listeners.
     */
    PropertyChangeListener[] getPageChangedListeners();
}
