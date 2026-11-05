package de.ganzer.swing.controls;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Collection;

/**
 * The model that is used by a {@link WizardPane} and {@link WizardController}.
 * <p>
 * This model works with objects that identifies a wizard page. The concrete
 * component behind the page is implementation defined.
 *
 * @see AbstractWizardModel
 * @see DefaultWizardModel
 *
 * @since 6.3.0
 */
public interface WizardModel {
    /**
     * Returns the identifiers of all pages.
     *
     * @return A collection of all added pages. The order of the pages in the
     *         returned collection is not necessarily the order the pages where
     *         added.
     */
    Collection<Object> getPages();

    /**
     * Gets the identifier of the next page to show after the given page.
     *
     * @param pageId The identifier of the page to get the next page for.
     *
     * @return The next page to show or {@code null} if no further page is
     *         available.
     */
    Object getNextPage(Object pageId);

    /**
     * Invoked to validate the data the user input into the given page.
     *
     * @param pageId The page to validate.
     *
     * @return {@code true} if the page is valid, {@code false} otherwise.
     */
    boolean validatePage(Object pageId);

    /**
     * Invoked when the given page is going to be left.
     * <p>
     * Implementors should update the wizard's data with the user's input.
     *
     * @param pageId The page to leave.
     */
    void pageLeaving(Object pageId);

    /**
     * Invoked when the given page is entered.
     * <p>
     * Implementors should update the page's controls with the wizard's data.
     *
     * @param pageId The page to enter.
     */
    void pageEntered(Object pageId);

    /**
     * Returns the identifier of the current page.
     *
     * @return The current page. This should not return {@code null}.
     */
    Object getCurrentPage();

    /**
     * Sets the currently active page.
     *
     * @param pageId The identifier of the page to set as current page.
     *
     * @return {@code true} if the page was set successfully, {@code false}
     *         otherwise.
     */
    boolean setCurrentPage(Object pageId);

    /**
     * Returns the Swing component for the given page.
     *
     * @param pageId The identifier of the page where to get the component for.
     *
     * @return The Swing component for the given page. This should not return
     *         {@code null}.
     */
    Component getComponent(Object pageId);

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

    /**
     * Adds a listener of page added events.
     *
     * @param listener The listener to add.
     */
    void addPageAddedListener(WizardPageAddedListener listener);

    /**
     * Removes a listener of page added events.
     *
     * @param listener The listener to remove.
     */
    void removePageAddedListener(WizardPageAddedListener listener);

    /**
     * Returns the listeners of page added events.
     *
     * @return The listeners of page added events.
     */
    WizardPageAddedListener[] getPageAddedListeners();
}
