package de.ganzer.swing.dlgfw.wizard;

import java.util.ArrayList;
import java.util.List;

public class WizardModel {
    private final List<WizardPanelDescriptor> panelDescriptors = new ArrayList<>();

    private WizardPanelDescriptor currentDescriptor;

    public WizardModel() {
    }

    public void registerPanel(WizardPanelDescriptor descriptor) {
        panelDescriptors.add(descriptor);
    }

    public WizardPanelDescriptor getCurrentDescriptor() {
        return currentDescriptor;
    }

    public void setCurrentDescriptor(WizardPanelDescriptor currentDescriptor) {
        this.currentDescriptor = currentDescriptor;
    }
}
