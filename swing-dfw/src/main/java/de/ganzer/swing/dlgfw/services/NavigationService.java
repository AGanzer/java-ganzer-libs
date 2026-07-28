package de.ganzer.swing.dlgfw.services;

import java.awt.Component;

/**
 * A service to navigate through the UI.
 */
public interface NavigationService {
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
