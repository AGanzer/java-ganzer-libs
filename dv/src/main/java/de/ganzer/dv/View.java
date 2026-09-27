package de.ganzer.dv;

/**
 * An interface to a view that can be used with a {@link Document}.
 *
 * @param <D> The type of the document the view shall be bound to.
 *
 * @since 6.0.0
 */
public interface View<D extends Document> {
    /**
     * Gets the template that has created the view.
     *
     * @return The template that has created the view.
     */
    ViewTemplate<D, ? extends View<D>> getTemplate();

    /**
     * Gets the bound document.
     * <p>
     * Implementors should set and return the document that is given by
     * {@link ViewCreationInfo} when the view is created by a {@link ViewSupplier}.
     *
     * @return The bound document or {@code null} if the document is not bound
     *          yet.
     */
    D getDocument();

    /**
     * Invoked to bring the view to the front.
     */
    void toFront();

    /**
     * Invoked to close the view without any further action.
     * <p>
     * The view's document is already closed when this method is invoked.
     * Implementors have to ensure that no exception is thrown and the view is
     * closed.
     *
     * @see Document#isClosed()
     */
    void forceClose();
}
