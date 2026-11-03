package de.ganzer.swing.dlgfw.wizard;

import java.beans.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultWizardModel<Data> implements WizardModel<Data> {
    private final Map<Object, WizardPage<Data>> wizardPages = new HashMap<>();
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private final VetoableChangeSupport vetoes = new VetoableChangeSupport(this);

    private WizardPage<Data> currentPage;

    @Override
    public void addPage(WizardPage<Data> page) {
        Objects.requireNonNull(page, "page must not be null.");

        if (wizardPages.containsKey(page.getPageId()))
            throw new IllegalArgumentException("page is already registered.");

        wizardPages.put(page.getPageId(), page);

        if (wizardPages.size() == 1)
            currentPage = page;
    }

    @Override
    public WizardPage<Data> getCurrentPage() {
        return currentPage;
    }

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

    @Override
    public void addPageChangingListener(VetoableChangeListener listener) {
        vetoes.addVetoableChangeListener("activePage", listener);
    }

    @Override
    public void removePageChangingListener(VetoableChangeListener listener) {
        vetoes.removeVetoableChangeListener("activePage", listener);
    }

    @Override
    public void addPageChangedListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener("activePage", listener);
    }

    @Override
    public void removePageChangedListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener("activePage", listener);
    }
}
