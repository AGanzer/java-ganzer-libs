package de.ganzer.swing.dialogs;

import javax.swing.*;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

/**
 * A dialog that enables closing it by the Esc key.
 *
 * @see #createRootPane()
 */
public class EscapableDialog extends JDialog {
    private boolean escaped = true;

    /**
     * {@inheritDoc}
     */
    public EscapableDialog() {
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Frame owner) {
        super(owner);
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Frame owner, String title) {
        super(owner, title);
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Frame owner, String title, GraphicsConfiguration gc) {
        super(owner, title, false, gc);
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Dialog owner) {
        super(owner);
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Dialog owner, String title) {
        super(owner, title);
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Dialog owner, String title, GraphicsConfiguration gc) {
        super(owner, title, false, gc);
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Window owner) {
        super(owner);
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Window owner, ModalityType modalityType) {
        super(owner, modalityType);
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Window owner, String title) {
        super(owner, title);
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);
    }

    /**
     * {@inheritDoc}
     */
    public EscapableDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
    }

    /**
     * Gets a value that indicates whether the dialog is closed by the Esc key
     * or by the frames Close button or Close menu or by a Cancel button.
     *
     * @return {@code false} if the dialog is closed by an OK button and
     *         {@code true} if the dialog is closed in any other way.
     */
    @SuppressWarnings("unused")
    public boolean isEscaped() {
        return escaped;
    }

    /**
     * Sends a {@link WindowEvent#WINDOW_CLOSING} event to this dialog.
     *
     * @param escaped Indicates whether the dialog is closed by the Esc key.
     */
    public void closeDialog(boolean escaped) {
        this.escaped = escaped;
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Calls {@link JDialog#createRootPane()} and adds an Esc key to the action
     * map of the root pane to dispatch a {@link WindowEvent#WINDOW_CLOSING}
     * event.
     *
     * @return The created root pane.
     */
    @Override
    protected JRootPane createRootPane() {
        final var ACTION_MAP_KEY = "EscKeyClick-de.ganzer.swing";

        var root = super.createRootPane();

        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                ACTION_MAP_KEY);
        root.getActionMap().put(ACTION_MAP_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeDialog(true);
            }
        });

        return root;
    }
}
