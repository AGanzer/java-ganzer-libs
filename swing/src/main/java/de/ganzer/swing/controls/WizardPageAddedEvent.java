package de.ganzer.swing.controls;

import java.awt.Component;
import java.util.EventObject;

/**
 * the event that is fired by {@link AbstractWizardModel} when a new page is
 * added.
 */
public class WizardPageAddedEvent extends EventObject {
    private final Object pageId;
    private final Component component;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     *
     * @param pageId The added page identifier.
     * @param component The added component.
     *
     * @throws IllegalArgumentException if source is null
     */
    public WizardPageAddedEvent(Object source, Object pageId, Component component) {
        super(source);
        this.pageId = pageId;
        this.component = component;
    }

    /**
     * Gets the page identifier.
     *
     * @return The added page identifier.
     */
    public Object getPageId() {
        return pageId;
    }

    /**
     * Gets the component.
     *
     * @return The added component.
     */
    public Component getComponent() {
        return component;
    }
}
