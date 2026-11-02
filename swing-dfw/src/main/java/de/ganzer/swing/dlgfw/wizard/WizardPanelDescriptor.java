package de.ganzer.swing.dlgfw.wizard;

import java.awt.Component;

public class WizardPanelDescriptor {
    private final Object panelId;
    private final Component component;

    public WizardPanelDescriptor(Object panelId, Component component) {
        this.panelId = panelId;
        this.component = component;
    }

    public Object getPanelId() {
        return panelId;
    }

    public Component getComponent() {
        return component;
    }
}
