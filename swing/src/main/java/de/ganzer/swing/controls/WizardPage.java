package de.ganzer.swing.controls;

import java.awt.Component;

/**
 * The interface to a page that is displayed in a {@link WizardPane} by using
 * an instance of {@link DefaultWizardModel}.
 */
public interface WizardPage {
    /**
     * The unique identifier of the page.
     *
     * @return The unique identifier of the page. This must not be {@code null}.
     */
    Object getPageId();

    /**
     * The component to display in a {@link WizardPane}.
     *
     * @return The component to display in the wizard. This must not be
     *         {@code null}.
     */
    Component getComponent();
}
