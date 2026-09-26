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

    /**
     * Invoked to get one or more locations that shall be opened as documents.
     * <p>
     * A "location" is implementation defined. Usually, this is a path to a file
     * or a URL to an internet address.
     * <p>
     * The filters are also implementation defined, but usually, they define a
     * set of file filters that can be shown in an "Open" dialog.
     *
     * @param filters The filters to filter the possible results or {@code null}
     *         if no filter is provided.
     * @param initialFilter The initial filter to set or {@code null} if no
     *         initial filter is provided.
     *
     * @return The list of locations to open or {@code null} of an empty list
     *          if the user has canceled.
     */
    public abstract Collection<String> getLocationsToOpen(List<String> filters, String initialFilter);
}
