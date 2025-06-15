package de.ganzer.swing.controls;

import javax.swing.AbstractAction;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Extends {@link JTextArea} with the ability to control undo/redo operations
 * via the keyboard.
 */
@SuppressWarnings("unused")
public class GTextArea extends JTextArea {
    private final UndoManager undoManager = new UndoManager();

    /**
     * Constructs a new TextArea.  A default model is set, the initial string
     * is null, and rows/columns are set to 0.
     */
    public GTextArea() {
        GTextField.initUndoRedo(this, undoManager);
    }

    /**
     * Constructs a new TextArea with the specified text displayed.
     * A default model is created and rows/columns are set to 0.
     *
     * @param text the text to be displayed, or null
     */
    public GTextArea(String text) {
        super(text);
        GTextField.initUndoRedo(this, undoManager);
    }

    /**
     * Constructs a new empty TextArea with the specified number of
     * rows and columns.  A default model is created, and the initial
     * string is null.
     *
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     *
     * @throws IllegalArgumentException if the rows or columns
     *         arguments are negative.
     */
    public GTextArea(int rows, int columns) {
        super(rows, columns);
        GTextField.initUndoRedo(this, undoManager);
    }

    /**
     * Constructs a new TextArea with the specified text and number
     * of rows and columns.  A default model is created.
     *
     * @param text the text to be displayed, or null
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     *
     * @throws IllegalArgumentException if the rows or columns arguments are
     *          negative.
     */
    public GTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
        GTextField.initUndoRedo(this, undoManager);
    }

    /**
     * Constructs a new JTextArea with the given document model, and defaults
     * for all of the other arguments (null, 0, 0).
     *
     * @param doc the model to use
     */
    public GTextArea(Document doc) {
        super(doc);
        GTextField.initUndoRedo(this, undoManager);
    }

    /**
     * Constructs a new JTextArea with the specified number of rows
     * and columns, and the given model.  All of the constructors
     * feed through this constructor.
     *
     * @param doc the model to use, or create a default one if null
     * @param text the text to be displayed, null if none
     * @param rows the number of rows &gt;= 0
     * @param columns the number of columns &gt;= 0
     *
     * @throws IllegalArgumentException if the rows or columns
     *         arguments are negative.
     */
    public GTextArea(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
        GTextField.initUndoRedo(this, undoManager);
    }

    /**
     * Gets the undo manager of this instance.
     *
     * @return The undo manager.
     */
    public UndoManager getUndoManager() {
        return undoManager;
    }
}
