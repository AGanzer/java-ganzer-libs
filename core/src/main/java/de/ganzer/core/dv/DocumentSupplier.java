package de.ganzer.core.dv;

/**
 * Used by {@link DocumentTemplate} to create a new document.
 *
 * @param <D> The type of the document.
 *
 * @since 5.6.0
 */
public interface DocumentSupplier<D extends Document> {
    /**
     * Creates a document from the given information.
     *
     * @param info The information to create the document.
     *
     * @return The created document.
     */
    D createDocument(DocumentCreationInfo<D> info);
}
