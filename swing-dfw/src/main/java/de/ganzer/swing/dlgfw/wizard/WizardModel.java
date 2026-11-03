package de.ganzer.swing.dlgfw.wizard;

import java.beans.*;

public interface WizardModel<Data> {
    void addPage(WizardPage<Data> page);
    WizardPage<Data> getCurrentPage();
    boolean setCurrentPage(WizardPage<Data> page);
    void addPageChangingListener(VetoableChangeListener listener);
    void removePageChangingListener(VetoableChangeListener listener);
    void addPageChangedListener(PropertyChangeListener listener);
    void removePageChangedListener(PropertyChangeListener listener);
}
