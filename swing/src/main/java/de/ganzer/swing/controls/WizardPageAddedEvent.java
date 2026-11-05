package de.ganzer.swing.controls;

import java.util.EventObject;

/**
 * the event that is fired by {@link AbstractWizardModel} when a new page is
 * added.
 *
 * @since 6.3.0
 */
public class WizardPageAddedEvent extends EventObject {
    private final Object pageId;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @param pageId The added page identifier.
     *
     * @throws IllegalArgumentException if source is null
     */
    public WizardPageAddedEvent(Object source, Object pageId) {
        super(source);
        this.pageId = pageId;
    }

    /**
     * Gets the page identifier.
     *
     * @return The added page identifier.
     */
    public Object getPageId() {
        return pageId;
    }
}
