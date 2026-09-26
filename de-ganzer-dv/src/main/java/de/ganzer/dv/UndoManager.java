package de.ganzer.dv;

import java.util.List;
import java.util.Stack;

/**
 * The manager that manages undoable actions that implement {@link Undoable}.
 *
 * @since 6.0.0
 */
public class UndoManager {
    private final Stack<Undoable> undoStack = new Stack<>();
    private final Stack<Undoable> redoStack = new Stack<>();

    private int maxUndoableCount;

    /**
     * Creates a new manager that enables 99 undoable actions.
     */
    public UndoManager() {
        this (99);
    }

    /**
     * Creates a new instance.
     *
     * @param maxUndoableCount The maximum number of undoable actions.
     */
    public UndoManager(int maxUndoableCount) {
        this.maxUndoableCount = maxUndoableCount;
    }

    /**
     * Get the maximum number of undoable actions.
     *
     * @return The maximum number of undoable actions.
     */
    public int getMaxUndoableCount() {
        return maxUndoableCount;
    }

    /**
     * Sets the maximum number of undoable actions.
     *
     * @param maxUndoableCount The maximum number of undoable actions.
     */
    public void setMaxUndoableCount(int maxUndoableCount) {
        this.maxUndoableCount = maxUndoableCount;

        while(undoStack.size() > maxUndoableCount)
            undoStack.remove(0);
    }

    /**
     * Removes all undoable actions.
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    /**
     * Pushes an undoable action to the stack of undoable actions.
     *
     * @param undoable The action to push. This is not executed but only pushed.
     *
     * @see #add(Undoable, boolean)
     */
    public void add(Undoable undoable) {
        add(undoable, false);
    }

    /**
     * Pushes an undoable action to the stack of undoable actions.
     *
     * @param undoable The action to push. This is not executed but only pushed.
     * @param execute If this is {@code true}, {@link Undoable#execute()} is
     *         invoked.
     */
    public void add(Undoable undoable, boolean execute) {
        if (maxUndoableCount == undoStack.size())
            undoStack.remove(0);

        undoStack.push(undoable);
        redoStack.clear();

        if (execute)
            undoable.execute();
    }

    /**
     * Indicates whether an action that can be undone is available.
     *
     * @return {@code true} if an action can be undone.
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Undoes the latest undoable action.
     */
    public void undo() {
        if (!canUndo())
            return;

        Undoable undo = undoStack.pop();
        redoStack.push(undo);

        undo.undo();
    }

    /**
     * Indicates whether an action that can be redone is available.
     *
     * @return {@code true} if an action can be redone.
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Redoes the latest undoable action.
     */
    public void redo() {
        if (!canRedo())
            return;

        Undoable undo = redoStack.pop();
        undoStack.push(undo);

        undo.redo();
    }

    /**
     * Gets the title of the current undoable action.
     *
     * @return The title of tue current undoable action or {@code null} if there
     *          is no undoable action.
     */
    public String getUndoTitle() {
        return canUndo() ? undoStack.peek().getTitle() : null;
    }

    /**
     * Gets the title of the current redoable action.
     *
     * @return The title of tue current redoable action or {@code null} if there
     *          is no redoable action.
     */
    public String getRedoTitle() {
        return canRedo() ? redoStack.peek().getTitle() : null;
    }

    /**
     * Gets the titles of all available actions that can be undone.
     *
     * @return The titles of all available actions that can be undone.
     */
    public List<String> getUndoTitles() {
        return undoStack.stream().map(Undoable::getTitle).toList();
    }

    /**
     * Gets the titles of all available actions that can be redone.
     *
     * @return The titles of all available actions that can be redone.
     */
    public List<String> getRedoTitles() {
        return redoStack.stream().map(Undoable::getTitle).toList();
    }
}
