package de.ganzer.swing.dlgfw.services;

/**
 * A service to get application-specific information.
 *
 * @since 6.0.0
 */
public interface ApplicationService {
    /**
     * Gets the display name of the application for use in dialog titles.
     *
     * @return The application's display name.
     */
    String getAppDisplayName();
}
