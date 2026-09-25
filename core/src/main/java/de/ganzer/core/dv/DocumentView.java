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
     * Gets the bound document.
     *
     * @return The bound document or {@code null} if the document is not bound
     *          yet.
     *
     * @see #setDocument(D)
     */
    D getDocument();

    /**
     * Called to bind the given document to the view.
     *
     * @param document The document to bind.
     */
    void setDocument(D document);

    /**
     * Gets the view's title.
     *
     * @return The title or {@code null} if the title is not set.
     *
     * @see #setTitle(String)
     */
    String getTitle();

    /**
     * Sets the title of the view.
     *
     * @param title The title to set.
     */
    void setTitle(String title);

    /**
     * Gets the view's title tool tip.
     * <p>
     * This default implementation is provided because not every view needs a
     * tool tip for its title, but only views that may be subviews within a
     * tabbed client of a main window.
     *
     * @return The tool tip or {@code null} if the tool tip is not set. This
     *          implementation does always return {@code null}.
     *
     * @see #setTitleToolTip(String)
     */
    default String getTitleToolTip() {
        return null;
    }

    /**
     * Sets the title tool tip of the view.
     * <p>
     * This default implementation is provided because not every view needs a
     * tool tip for its title, but only views that may be subviews within a
     * tabbed client of a main window.
     * <p>
     * This implementation does nothing.
     *
     * @param toolTip The tool tip to set.
     */
    default void setTitleToolTip(String toolTip) {
    }

    /**
     * Invoked to set the description text of an "Undo" action.
     * <p>
     * This default implementation is provided because not every view has a menu
     * or button for undoable actions.
     * <p>
     * This implementation does nothing.
     *
     * @param description The description to set or {@code null} if no undoable
     *         action is available.
     */
    default void setUndoableDescription(String description) {
    }

    /**
     * Invoked to set the description text of a "Redo" action.
     * <p>
     * This default implementation is provided because not every view has a menu
     * or button for redoable actions.
     * <p>
     * This implementation does nothing.
     *
     * @param description The description to set or {@code null} if no redoable
     *         action is available.
     */
    default void setRedoableDescription(String description) {
    }

    /**
     * Indicates whether a "Cut" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    default boolean canCut() {
        return false;
    }

    /**
     * Indicates whether a "Copy" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    default boolean canCopy() {
        return false;
    }

    /**
     * Indicates whether a "Paste" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    default boolean canPaste() {
        return false;
    }

    /**
     * Indicates whether a "Delete" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    default boolean canDelete() {
        return false;
    }

    /**
     * Performs a "Cut" action.
     */
    default void cut() {
    }

    /**
     * Performs a "Copy" action.
     */
    default void copy() {
    }

    /**
     * Performs a "Paste" action.
     */
    default void paste() {
    }

    /**
     * Performs a "Delete" action.
     */
    default void delete() {
    }
}
