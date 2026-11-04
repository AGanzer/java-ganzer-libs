package de.ganzer.swing.dlgfw.wizard;

import java.beans.*;
import java.util.Collection;

/**
 * The model that is used by a {@link WizardPane}.
 *
 * @param <Data> The type of the data that is passed between the pages.
 */
public interface WizardModel<Data> {
    /**
     * Adds a page to the available pages.
     *
     * @param page The page to add. If this is the first added page, it is set
     *         as the currently active page.
     *
     * @throws NullPointerException if {@code page} is {@code null}.
     * @throws IllegalArgumentException if {@code page} is already registered.
     */
    void addPage(WizardPage<Data> page);

    /**
     * Returns all pages.
     *
     * @return A collection of all added pages. The order of the pages in the
     *         returned collection is not necessarily the order the pages where
     *         added.
     */
    Collection<WizardPage<Data>> getPages();

    /**
     * Returns the current page.
     *
     * @return The current page or {@code null} of no page is currently visible.
     */
    WizardPage<Data> getCurrentPage();

    /**
     * Gets the next page to show after the current page.
     *
     * @param data The data to work with.
     *
     * @return The next page to show or {@code null} if no further page is
     *         available.
     */
    WizardPage<Data> getNextPage(Data data);

    /**
     * Sets the currently active page.
     *
     * @param page The page to set as active.
     *
     * @return {@code true} if the page was set successfully, {@code false}
     *         otherwise.
     */
    boolean setCurrentPage(WizardPage<Data> page);

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
}
