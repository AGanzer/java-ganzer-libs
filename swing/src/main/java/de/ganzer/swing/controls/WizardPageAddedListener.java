package de.ganzer.swing.controls;

import java.util.EventListener;

/**
 * The listener that listens to page added events of an {@link AbstractWizardModel}.
 *
 * @since 6.3.0
 */
public interface WizardPageAddedListener extends EventListener {
    /**
     * Called when a page is added to the wizard model.
     *
     * @param event The event that contains information about the added page.
     */
    void onPageAdded(WizardPageAddedEvent event);
}
