package de.ganzer.swing.controls;

import javax.swing.event.EventListenerList;
import java.beans.*;
import java.util.*;

/**
 * An abstract implementation of {@link WizardModel} that provides property
 * change events and a basic page management.
 *
 * @param <P> The page object that is associated with a given page identifier.
 *
 * @see #addPage(Object, P)
 * @see DefaultWizardModel
 *
 * @since 6.3.0
 */
public abstract class AbstractWizardModel<P> implements WizardModel {
    private final Map<Object, P> wizardPages = new HashMap<>();
    private final Collection<Object> unmodifiableWizardPages = Collections.unmodifiableSet(wizardPages.keySet());
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private final VetoableChangeSupport vetoes = new VetoableChangeSupport(this);
    private final EventListenerList events = new EventListenerList();

    private Object currentPage;

    /**
     * Returns the identifiers of all pages.
     *
     * @return A collection of all added pages. The order of the pages in the
     *         returned collection is not necessarily the order the pages where
     *         added.
     */
    @Override
    public Collection<Object> getPages() {
        return unmodifiableWizardPages;
    }

    /**
     * Returns the identifier of the current page.
     *
     * @return The current page. This should not return {@code null}.
     */
    @Override
    public Object getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the currently active page.
     *
     * @param pageId The identifier of the page to set as current page.
     *
     * @return {@code true} if the page was set successfully, {@code false}
     *         otherwise.
     */
    @Override
    public boolean setCurrentPage(Object pageId) {
        if (!wizardPages.containsKey(pageId))
            throw new IllegalArgumentException("page is not registered.");

        if (currentPage == pageId)
            return false;

        try {
            firePageChanging(currentPage, pageId);
        } catch (PropertyVetoException e) {
            return false;
        }

        var oldPage = currentPage;
        currentPage = pageId;

        firePageChanged(oldPage, currentPage);

        return true;
    }

    /**
     * Adds a listener of page changing events.
     * <p>
     * This event is fired before the pages are changed. It is vetoable and can
     * be used to prevent the page change.
     *
     * @param listener The listener to add.
     */
    @Override
    public void addPageChangingListener(VetoableChangeListener listener) {
        vetoes.addVetoableChangeListener("activePage", listener);
    }

    /**
     * Removes a listener of page changing events.
     *
     * @param listener The listener to remove.
     */
    @Override
    public void removePageChangingListener(VetoableChangeListener listener) {
        vetoes.removeVetoableChangeListener("activePage", listener);
    }

    /**
     * Gets all installed page changing listeners.
     *
     * @return The installed page changing listeners.
     */
    @Override
    public VetoableChangeListener[] getPageChangingListeners() {
        return vetoes.getVetoableChangeListeners("activePage");
    }

    /**
     * Adds a listener of page changed events.
     * <p>
     * This event is fired after the pages have changed.
     *
     * @param listener The listener to add.
     */
    @Override
    public void addPageChangedListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener("activePage", listener);
    }

    /**
     * Removes a listener of page changed events.
     *
     * @param listener The listener to remove.
     */
    @Override
    public void removePageChangedListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener("activePage", listener);
    }

    /**
     * Gets all installed page changed listeners.
     *
     * @return The installed page changed listeners.
     */
    @Override
    public PropertyChangeListener[] getPageChangedListeners() {
        return changes.getPropertyChangeListeners("activePage");
    }

    /**
     * Adds a listener of page added events.
     *
     * @param listener The listener to add.
     */
    @Override
    public void addPageAddedListener(WizardPageAddedListener listener) {
        events.add(WizardPageAddedListener.class, listener);
    }

    /**
     * Removes a listener of page added events.
     *
     * @param listener The listener to remove.
     */
    @Override
    public void removePageAddedListener(WizardPageAddedListener listener) {
        events.remove(WizardPageAddedListener.class, listener);
    }

    /**
     * Returns the listeners of page added events.
     *
     * @return The listeners of page added events.
     */
    @Override
    public WizardPageAddedListener[] getPageAddedListeners() {
        return events.getListeners(WizardPageAddedListener.class);
    }

    /**
     * Adds a page to the available pages.
     * <p>
     * If the given page is the first added page, it is set as the currently
     * active page.
     *
     * @param pageId The identifier of the page to add.
     * @param page The page that represents the page.
     *
     * @throws NullPointerException if {@code pageId} is {@code null}.
     * @throws IllegalArgumentException if {@code page} is already registered.
     */
    protected void addPage(Object pageId, P page) {
        Objects.requireNonNull(pageId, "pageId must not be null.");
        Objects.requireNonNull(page, "page must not be null.");

        if (wizardPages.containsKey(pageId))
            throw new IllegalArgumentException("page is already registered.");

        wizardPages.put(pageId, page);

        if (wizardPages.size() == 1)
            setCurrentPage(pageId);

        firePageAdded(pageId);
    }

    /**
     * Gets the page that is associated with the given page identifier.
     *
     * @param pageId The identifier of the page to get.
     *
     * @return The page that is associated with the given page identifier.
     *
     * @see #addPage(Object, P)
     */
    protected P getPage(Object pageId) {
        return wizardPages.get(pageId);
    }

    /**
     * Fires the page changing event.
     *
     * @param oldPage The old page.
     * @param newPage The new page.
     *
     * @throws PropertyVetoException if the change is vetoed.
     */
    protected void firePageChanging(Object oldPage, Object newPage) throws PropertyVetoException {
        vetoes.fireVetoableChange("activePage", oldPage, newPage);
    }

    /**
     * Fires the page changed event.
     *
     * @param oldPage The old page.
     * @param newPage The new page.
     */
    protected void firePageChanged(Object oldPage, Object newPage) {
        changes.firePropertyChange("activePage", oldPage, newPage);
    }

    /**
     * Fires the page added event.
     *
     * @param pageId The added page identifier.
     */
    protected void firePageAdded(Object pageId) {
        var listeners = getPageAddedListeners();
        var evt = new WizardPageAddedEvent(this, pageId);

        for (WizardPageAddedListener listener : listeners)
            listener.onPageAdded(evt);
    }
}
