package com.example.uitests.swingdv.doc;

import de.ganzer.dv.Model;
import de.ganzer.dv.Undoable;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoableEdit;

/**
 * Encapsulates an undoable action from a {@link Document}.
 * <p>
 * Panels or windows that contain a {@link JTextComponent} derivation can use
 * this class to enable undoable actions that depend on the editor component
 * for a {@link Model}:
 *
 * <pre>{@code
 * editor = new JTextArea();
 * editor.getDocument().addUndoableEditListener(
 *         e -> getPresenter().getModel().addUndoable(new SwingDocumentUndoable(e.getEdit())));
 * }</pre>
 */
public class SwingDocumentUndoable implements Undoable {
    private final UndoableEdit edit;

    /**
     * Creates a new instance.
     *
     * @param edit The undoable edit to encapsulate.
     */
    public SwingDocumentUndoable(UndoableEdit edit) {
        this.edit = edit;
    }

    /**
     * Gets the title of the action. This can be used to display the action
     * within an Undo/Redo menu item.
     *
     * @return The title or {@code null} if no title is available.
     */
    @Override
    public String getTitle() {
        return edit.getPresentationName();
    }

    /**
     * This executes the action that can be later made undone. This can be used
     * to execute the action if it is not already executed the first time.
     */
    @Override
    public void execute() {
        edit.redo();
    }

    /**
     * Undoes the action.
     */
    @Override
    public void undo() {
        edit.undo();
    }
}
