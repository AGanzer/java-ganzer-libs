package de.ganzer.swing.util;

/**
 * An interface to a control that supports the basic editable features.
 * <p>
 * This interface provides default implementations for all of its methods to
 * prevent that an implementor does not have to provide empty implementations
 * for the unused features.
 *
 * @since 6.0.0
 */
public interface EditableComponent {
    /**
     * Gets a value that indicates whether the editor is able to undo the
     * last undoable action.
     *
     * @return {@code true} if an action can be undone. This implementation does
     *         always return {@code false}.
     */
    default boolean canUndo() {
        return false;
    }

    /**
     * Undoes the last undoable action.
     * <p>
     * This implementation does nothing.
     */
    default void undo() {
    }

    /**
     * Gets a value that indicates whether the editor is able to redo an
     * undone action.
     *
     * @return {@code true} if an action can be redone. This implementation does
     *         always return {@code false}.
     */
    default boolean canRedo() {
        return false;
    }

    /**
     * Redoes the last undone action.
     * <p>
     * This implementation does nothing.
     */
    default void redo() {
    }

    /**
     * Indicates whether a "Cut" action is currently supported.
     *
     * @return {@code true} if the action is currently supported. This
     *         implementation does always return {@code false}.
     */
    default boolean canCut() {
        return false;
    }

    /**
     * Performs a "Cut" action.
     * <p>
     * This implementation does nothing.
     */
    default void cut() {
    }

    /**
     * Indicates whether a "Copy" action is currently supported.
     *
     * @return {@code true} if the action is currently supported. This
     *         implementation does always return {@code false}.
     */
    default boolean canCopy() {
        return false;
    }

    /**
     * Performs a "Copy" action.
     * <p>
     * This implementation does nothing.
     */
    default void copy() {
    }

    /**
     * Indicates whether a "Paste" action is currently supported.
     *
     * @return {@code true} if the action is currently supported. This
     *         implementation does always return {@code false}.
     */
    default boolean canPaste() {
        return false;
    }

    /**
     * Performs a "Paste" action.
     * <p>
     * This implementation does nothing.
     */
    default void paste() {
    }

    /**
     * Indicates whether a "Delete" action is currently supported.
     *
     * @return {@code true} if the action is currently supported. This
     *         implementation does always return {@code false}.
     */
    default boolean canDelete() {
        return false;
    }

    /**
     * Performs a "Delete" action.
     * <p>
     * This implementation does nothing.
     */
    default void delete() {
    }
}
