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
     * Gets the view's title.
     * <p>
     * This implementation builds the title from the given title hints of the
     * view's template and the title of the view's document. If there are more
     * than one views in the document, the view's index is appended to the title.
     *
     * @return The view's title.
     *
     * @see ViewTemplate#getReadOnlyHintFormat()
     * @see ViewTemplate#getModificationHintFormat()
     */
    default String getTitle() {
        var format = getDocument().isReadOnly()
                ? getTemplate().showReadOnlyHint() ? getTemplate().getReadOnlyHintFormat() : "%s"
                : getDocument().isModified() && getTemplate().showModificationHint() ? getTemplate().getModificationHintFormat() : "%s";
        var title = String.format(format, getDocument().getTitle());

        var views = getDocument().getViews().stream().filter(v -> v.getTemplate().hasTitleNumber()).toList();
        int index = views.size() > 1 ? views.indexOf(this) : -1;

        if (index >= 0)
            title += ":" + (index + 1);

        return title;
    }

    /**
     * Invoked by the document to update the view's title.
     *
     * @see #getTitle()
     */
    void updateTitle();

    /**
     * Invoked by the document to notify the view about changes in its data.
     * <p>
     * <b>NOTE:</b> Implementors should ensure that updating its controls within
     * this method does not cause a new change of the document to avoid infinite
     * update loops.
     *
     * @param context The context of change. This is implementation defined and
     *        may be {@code null} to indicate that the view should be updated
     *        completely.
     */
    void documentDataChanged(Object context);

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
