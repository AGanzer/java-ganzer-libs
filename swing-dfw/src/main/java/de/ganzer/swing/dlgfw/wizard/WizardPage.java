package de.ganzer.swing.dlgfw.wizard;

import de.ganzer.swing.dlgfw.ModifiableDataPanel;

import java.awt.Component;

/**
 * A page that can be inserted into a {@link WizardModel}.
 *
 * @param <Data> The type of the data to work.
 */
public interface WizardPage<Data> extends ModifiableDataPanel<Data> {
    /**
     * The unique ID of the page.
     *
     * @return The unique ID of the page. This must not be {@code null}.
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
