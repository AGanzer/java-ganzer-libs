package de.ganzer.swing.dlgfw.wizard;

import java.beans.*;
import java.util.*;

/**
 * An abstract implementation of the {@link WizardModel} that provides property
 * change events and a basic page management.
 *
 * @param <Data> The type of the data that is passed between the pages.
 */
public abstract class AbstractWizardModel<Data> implements WizardModel<Data> {
    private final Map<Object, WizardPage<Data>> wizardPages = new HashMap<>();
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private final VetoableChangeSupport vetoes = new VetoableChangeSupport(this);

    private WizardPage<Data> currentPage;

    /**
     * Adds a page to the available pages.
     *
     * @param page The page to add. If this is the first added page, it is set
     *         as the currently active page.
     *
     * @throws NullPointerException if {@code page} is {@code null}.
     * @throws IllegalArgumentException if {@code page} is already registered.
     */
    @Override
    public void addPage(WizardPage<Data> page) {
        Objects.requireNonNull(page, "page must not be null.");

        if (wizardPages.containsKey(page.getPageId()))
            throw new IllegalArgumentException("page is already registered.");

        wizardPages.put(page.getPageId(), page);

        if (wizardPages.size() == 1)
            currentPage = page;
    }

    /**
     * Returns all pages.
     *
     * @return A collection of all added pages. The order of the pages in the
     *         returned collection is not necessarily the order the pages where
     *         added.
     */
    @Override
    public Collection<WizardPage<Data>> getPages() {
        return wizardPages.values();
    }

    /**
     * Returns the current page.
     *
     * @return The current page or {@code null} of no page is currently visible.
     */
    @Override
    public WizardPage<Data> getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the currently active page.
     *
     * @param page The page to set as active.
     *
     * @return {@code true} if the page was set successfully, {@code false}
     *         otherwise.
     */
    @Override
    public boolean setCurrentPage(WizardPage<Data> page) {
        Objects.requireNonNull(page, "page must not be null.");

        if (!wizardPages.containsKey(page.getPageId()))
            throw new IllegalArgumentException("page is not registered.");

        if (currentPage == page)
            return false;

        try {
            vetoes.fireVetoableChange("activePage", currentPage, page);
        } catch (PropertyVetoException e) {
            return false;
        }

        var oldPage = currentPage;
        currentPage = page;

        changes.firePropertyChange("activePage", oldPage, currentPage);

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
}
