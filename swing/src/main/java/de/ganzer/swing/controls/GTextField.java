package de.ganzer.swing.controls;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Extends {@link JTextField} with the ability to control undo/redo operations
 * via the keyboard.
 */
@SuppressWarnings("unused")
public class GTextField extends JTextField {
    private final UndoManager undoManager = new UndoManager();

    /**
     * Constructs a new <code>TextField</code>.  A default model is created,
     * the initial string is <code>null</code>,
     * and the number of columns is set to 0.
     */
    public GTextField() {
        initUndoRedo(this, undoManager);
    }

    /**
     * Constructs a new <code>TextField</code> initialized with the
     * specified text. A default model is created and the number of
     * columns is 0.
     *
     * @param text the text to be displayed, or <code>null</code>
     */
    public GTextField(String text) {
        super(text);
        initUndoRedo(this, undoManager);
    }

    /**
     * Constructs a new empty <code>TextField</code> with the specified
     * number of columns.
     * A default model is created and the initial string is set to
     * <code>null</code>.
     *
     * @param columns the number of columns to use to calculate
     *         the preferred width; if columns is set to zero, the
     *         preferred width will be whatever naturally results from
     *         the component implementation
     */
    public GTextField(int columns) {
        super(columns);
        initUndoRedo(this, undoManager);
    }

    /**
     * Constructs a new <code>TextField</code> initialized with the
     * specified text and columns.  A default model is created.
     *
     * @param text the text to be displayed, or <code>null</code>
     * @param columns the number of columns to use to calculate
     *         the preferred width; if columns is set to zero, the
     *         preferred width will be whatever naturally results from
     *         the component implementation
     */
    public GTextField(String text, int columns) {
        super(text, columns);
        initUndoRedo(this, undoManager);
    }

    /**
     * Constructs a new <code>JTextField</code> that uses the given text
     * storage model and the given number of columns.
     * This is the constructor through which the other constructors feed.
     * If the document is <code>null</code>, a default model is created.
     *
     * @param doc the text storage to use; if this is <code>null</code>,
     *         a default will be provided by calling the
     *         <code>createDefaultModel</code> method
     * @param text the initial string to display, or <code>null</code>
     * @param columns the number of columns to use to calculate
     *         the preferred width &gt;= 0; if <code>columns</code>
     *         is set to zero, the preferred width will be whatever
     *         naturally results from the component implementation
     *
     * @throws IllegalArgumentException if <code>columns</code> &lt; 0
     */
    public GTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        initUndoRedo(this, undoManager);
    }

    /**
     * Gets the undo manager of this instance.
     *
     * @return The undo manager.
     */
    public UndoManager getUndoManager() {
        return undoManager;
    }

    static void initUndoRedo(JTextComponent component, UndoManager undoManager) {
        component.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));

        int shortcutKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        component.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcutKey), "Undo");
        component.getActionMap().put("Undo", new AbstractAction("Undo") {
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });

        component.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, shortcutKey), "Redo");
        component.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcutKey | InputEvent.SHIFT_DOWN_MASK), "Redo");
        component.getActionMap().put("Redo", new AbstractAction("Redo") {
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });
    }
}
