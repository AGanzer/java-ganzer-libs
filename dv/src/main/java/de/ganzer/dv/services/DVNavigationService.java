package de.ganzer.dv.services;

import de.ganzer.core.Services;
import de.ganzer.dv.DocumentTemplate;

import java.util.Collection;
import java.util.List;

/**
 * The navigation service used by the document-view framework to access the user.
 * <p>
 * A service implementation of this type has to be registered by
 * {@link Services#register(Class, Object)}.
 * <pre>{@code
 * Services.register(DVNavigationService.class, new MyDVNavigationServiceImpl());
 * }</pre>
 *
 * @since 6.0.0
 */
public interface DVNavigationService {
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
     * @return The list of locations to open or {@code null} or an empty list
     *          if the user has canceled.
     */
    Collection<String> queryLocationsToOpen(List<String> filters, String initialFilter);

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
    Boolean querySave(String name);

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
    String querySaveLocation(String initialLocation, String filter);

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     * @param cause The exception that caused the error or {@code null} if no
     *        exception is available.
     */
    void showError(String message, Throwable cause);

    /**
     * Invoked to choose a document template.
     *
     * @param templates The available templates to choose from.
     *
     * @return The chosen document template or {@code null} if the user has
     *         canceled.
     */
    DocumentTemplate<?> chooseDocumentTemplate(List<DocumentTemplate<?>> templates);
}
