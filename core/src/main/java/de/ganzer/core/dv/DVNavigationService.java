package de.ganzer.core.dv;

import java.util.Collection;
import java.util.List;

/**
 * The navigation service used by the document-view framework to access the user.
 */
public abstract class DVNavigationService {
    private static DVNavigationService instance;

    /**
     * Registers the navigation service to use.
     * <p>
     * This method should be called with an instance of the navigation service
     * once at application startup.
     *
     * @param service The service to use.
     *
     * @see #getInstance()
     */
    public static void registerService(DVNavigationService service) {
        instance = service;
    }

    /**
     * Returns the instance of the navigation service.
     *
     * @return The instance of the navigation service.
     *
     * @throws IllegalStateException If no navigation service is registered.
     *
     * @see #registerService(DVNavigationService)
     */
    public static DVNavigationService getInstance() {
        if (instance == null)
            throw new IllegalStateException("No navigation service is registered.");

        return instance;
    }

    public abstract Collection<String> getLocationsToOpen(List<String> filters, String initialFilter);
}
