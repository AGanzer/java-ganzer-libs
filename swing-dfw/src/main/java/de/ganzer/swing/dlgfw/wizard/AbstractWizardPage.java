package de.ganzer.swing.dlgfw.wizard;

import java.awt.Component;

public abstract class AbstractWizardPage<Data> implements WizardPage<Data> {
    private final Object pageId;
    private final Component component;

    protected AbstractWizardPage(Object pageId, Component component) {
        this.pageId = pageId;
        this.component = component;
    }

    @Override
    public Object getPageId() {
        return pageId;
    }

    @Override
    public Component getComponent() {
        return component;
    }
}
