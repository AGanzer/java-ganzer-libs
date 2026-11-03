package de.ganzer.swing.dlgfw.wizard;

import de.ganzer.swing.dlgfw.ModifiableDataPanel;

import java.awt.Component;

public interface WizardPage<Data> extends ModifiableDataPanel<Data> {
    Object getPageId();
    Component getComponent();
    WizardPage<Data> getNextPage(Data data);
}
