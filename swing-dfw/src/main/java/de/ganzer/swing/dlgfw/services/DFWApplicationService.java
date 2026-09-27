package de.ganzer.swing.dlgfw.services;

import de.ganzer.core.Services;

/**
 * A service to get application-specific information.
 * <p>
 * A service implementation of this type has to be registered by
 * {@link Services#register(Class, Object)}.
 *
 * @since 6.0.0
 */
public interface DFWApplicationService {
    /**
     * Gets the display name of the application for use in dialog titles.
     *
     * @return The application's display name.
     */
    String getAppDisplayName();
}
