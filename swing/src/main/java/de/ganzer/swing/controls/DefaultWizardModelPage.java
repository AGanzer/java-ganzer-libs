package de.ganzer.swing.controls;

import java.awt.Component;

/**
 * The interface to wizard pages taht are used with instances of the
 * {@link DefaultWizardModel}.
 *
 * @since 6.3.0
 */
public interface DefaultWizardModelPage {
    /**
     * Gets the component that is used to display the page.
     *
     * @return The component that is used to display the page.
     */
    Component getComponent();

    /**
     * Invoked to update the page's controls with the wizard's data.
     */
    void updatePageControls();

    /**
     * Invoked to update the wizard's data with the page's controls.
     */
    void updateWizardData();

    /**
     * Invoked to validate the input of the page's controls.
     *
     * @return {@code true} if the input is valid, {@code false} otherwise.
     */
    boolean validateInput();
}
