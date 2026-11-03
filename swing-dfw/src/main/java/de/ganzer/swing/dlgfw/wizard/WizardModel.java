package de.ganzer.swing.dlgfw.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WizardModel {
    private final List<WizardPanelDescriptor> panelDescriptors = new ArrayList<>();

    private WizardPanelDescriptor currentDescriptor;

    public void registerPanel(WizardPanelDescriptor descriptor) {
        Objects.requireNonNull(descriptor, "descriptor must not be null.");

        if (panelDescriptors.contains(descriptor))
            throw new IllegalArgumentException("Panel descriptor already registered.");

        panelDescriptors.add(descriptor);

        if (panelDescriptors.size() == 1)
            currentDescriptor = descriptor;
    }

    public WizardPanelDescriptor getCurrentDescriptor() {
        return currentDescriptor;
    }

    public void setCurrentDescriptor(WizardPanelDescriptor descriptor) {
        Objects.requireNonNull(descriptor, "descriptor must not be null.");

        if (!panelDescriptors.contains(descriptor))
            throw new IllegalArgumentException("Panel descriptor not registered.");

        this.currentDescriptor = descriptor;
    }
}
