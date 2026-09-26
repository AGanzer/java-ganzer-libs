package de.ganzer.core.dv;

/**
 * An interface to a view that can be used with a {@link Document}.
 *
 * @param <D> The type of the document the view shall be bound to.
 *
 * @since 5.6.0
 */
public interface DocumentView<D extends Document> {
    /**
     * Gets the template that has created the view.
     *
     * @return The template that has created the view.
     */
    DocumentViewTemplate<D, ? extends DocumentView<D>> getTemplate();

    /**
     * Gets the bound document.
     *
     * @return The bound document or {@code null} if the document is not bound
     *          yet.
     *
     * @see #setDocument(D)
     */
    D getDocument();

    /**
     * Invoked to bind the given document to the view.
     *
     * @param document The document to bind.
     */
    void setDocument(D document);

    /**
     * Invoked to close the view without any further action.
     * <p>
     * The view's document is already closed when this method is invoked.
     *
     * @see Document#isClosed()
     */
    void forceClose();
}
