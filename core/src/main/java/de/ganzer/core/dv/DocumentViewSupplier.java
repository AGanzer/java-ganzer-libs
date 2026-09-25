package de.ganzer.core.dv;

/**
 * Used by {@link DocumentViewTemplate} to create a view.
 *
 * @param <D> The type of the document.
 * @param <V> The type of the view.
 *
 * @since 5.6.0
 */
public interface DocumentViewSupplier<D extends Document, V extends DocumentView<D>> {
    /**
     * Creates the view from the specified information.
     *
     * @param info The information for creating the view.
     *
     * @return The created view.
     */
    V createView(DocumentViewCreationInfo<D, V> info);
}
