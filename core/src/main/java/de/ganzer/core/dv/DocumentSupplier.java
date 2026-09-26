package de.ganzer.core.dv;

import java.io.IOException;

/**
 * Used by {@link DocumentTemplate} to create a new document.
 *
 * @param <D> The type of the document.
 *
 * @since 6.0.0
 */
public interface DocumentSupplier<D extends Document> {
    /**
     * Creates a document from the given information.
     *
     * @param info The information to create the document.
     *
     * @return The created document.
     *
     * @throws IOException on any error loading the data.
     */
    D createDocument(DocumentCreationInfo<D> info) throws IOException;
}
