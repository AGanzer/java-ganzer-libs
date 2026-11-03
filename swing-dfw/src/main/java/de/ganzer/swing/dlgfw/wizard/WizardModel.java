package de.ganzer.swing.dlgfw.wizard;

import java.beans.*;
import java.util.*;

public class WizardModel<Data> {
    private final Map<Object, WizardPage<Data>> wizardPages = new HashMap<>();
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private final VetoableChangeSupport vetoes = new VetoableChangeSupport(this);

    private WizardPage<Data> currentPage;

    public void addPage(WizardPage<Data> page) {
        Objects.requireNonNull(page, "page must not be null.");

        if (wizardPages.containsKey(page.getPageId()))
            throw new IllegalArgumentException("page is already registered.");

        wizardPages.put(page.getPageId(), page);

        if (wizardPages.size() == 1)
            currentPage = page;
    }

    public WizardPage<Data> getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(WizardPage<Data> page) {
        Objects.requireNonNull(page, "page must not be null.");

        if (!wizardPages.containsKey(page.getPageId()))
            throw new IllegalArgumentException("page is not registered.");

        if (currentPage == page)
            return;

        try {
            vetoes.fireVetoableChange("activePage", currentPage, page);
        } catch (PropertyVetoException e) {
            return;
        }

        var oldPage = currentPage;
        currentPage = page;

        changes.firePropertyChange("activePage", oldPage, currentPage);
    }

    public void addPageChangingListener(VetoableChangeListener listener) {
        vetoes.addVetoableChangeListener("activePage", listener);
    }

    public void removePageChangingListener(VetoableChangeListener listener) {
        vetoes.removeVetoableChangeListener("activePage", listener);
    }

    public void addPageChangedListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener("activePage", listener);
    }

    public void removePageChangedListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener("activePage", listener);
    }
}
