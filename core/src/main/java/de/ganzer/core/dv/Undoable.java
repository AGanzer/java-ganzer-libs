package de.ganzer.core.dv;

/**
 * An interface to an undoable action.
 *
 * @since 6.0.0
 */
public interface Undoable {
    /**
     * Gets the title of the action. This can be used to display the action
     * within an Undo/Redo menu item.
     *
     * @return The title or {@code null} if no title is available.
     */
    String getTitle();

    /**
     * This executes the action that can be later made undone. This can be used
     * to execute the action if it is not already executed the first time.
     */
    void execute();

    /**
     * Undoes the action.
     */
    void undo();

    /**
     * Redoes the action. This implementation calls {@link #execute()}.
     */
    default void redo() {
        execute();
    }
}
