package de.ganzer.core.dv;

import java.util.Collection;
import java.util.List;

/**
 * The navigation service used by the document-view framework to access the user.
 *
 * @since 6.0.0
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
    public abstract Collection<String> queryLocationsToOpen(List<String> filters, String initialFilter);

    /**
     * Invoked to query the user whether the document with the given name should
     * be saved.
     *
     * @param name The name of the document that contains unsaved modified data.
     *
     * @return {@code true} if the document can be saved, {@code false} not to
     *         save and {@code null} if the user wants to cancel to ongoing
     *         operation.
     */
    public abstract Boolean querySave(String name);

    /**
     * Invoked to query a location where to save new data.
     * <p>
     * A "location" is implementation defined. Usually, this is a path to a file
     * or a URL to an internet address.
     * <p>
     * A filter is also implementation defined, but usually, it defines a file
     * filter that can be shown in a "Save" dialog.
     *
     * @param initialLocation The initial location to show in a dialog.
     * @param filter the filter to filer to possible results or {@code null}
     *         if no filter is provided.
     *
     * @return The chosen location or {@code null} if the user has canceled.
     */
    public abstract String querySaveLocation(String initialLocation, String filter);

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     * @param cause The exception that caused the error or {@code null} if no
     *        exception is available.
     */
    public abstract void showError(String message, Throwable cause);
}
