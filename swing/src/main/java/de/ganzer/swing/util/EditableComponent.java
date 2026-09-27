package de.ganzer.swing.util;

/**
 * An interface to a control that supports the basic editable features.
 *
 * @since 6.0.0
 */
public interface EditableComponent {
    /**
     * Gets a value that indicates whether the editor is able to undo the
     * last undoable action.
     *
     * @return {@code true} if an action can be undone.
     */
    boolean canUndo();

    /**
     * Undoes the last undoable action.
     */
    void undo();

    /**
     * Gets a value that indicates whether the editor is able to redo an
     * undone action.
     *
     * @return {@code true} if an action can be redone.
     */
    boolean canRedo();

    /**
     * Redoes the last undone action.
     */
    void redo();

    /**
     * Indicates whether a "Cut" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    boolean canCut();

    /**
     * Performs a "Cut" action.
     */
    void cut();

    /**
     * Indicates whether a "Copy" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    boolean canCopy();

    /**
     * Performs a "Copy" action.
     */
    void copy();

    /**
     * Indicates whether a "Paste" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    boolean canPaste();

    /**
     * Performs a "Paste" action.
     */
    void paste();

    /**
     * Indicates whether a "Delete" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    boolean canDelete();

    /**
     * Performs a "Delete" action.
     */
    void delete();
}
