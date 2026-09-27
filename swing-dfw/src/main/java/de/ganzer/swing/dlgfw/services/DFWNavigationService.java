package de.ganzer.swing.dlgfw.services;

import de.ganzer.core.Services;

import java.awt.Component;

/**
 * A service to navigate through the UI.
 * <p>
 * A service implementation of this type has to be registered by
 * {@link Services#register(Class, Object)}.
 *
 * @since 6.0.0
 */
public interface DFWNavigationService {
    /**
     * Gets a confirmation from the user.
     *
     * @param parent The parent component
     * @param question The question to ask.
     * @param title The title of the dialog to show.
     *
     * @return {@code true} if the user has confirmed, {@code false} if the user
     *          has denied, {@code null} if the user has canceled.
     */
    Boolean getConfirmation(Component parent, String question, String title);
}
