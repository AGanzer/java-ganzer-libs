package de.ganzer.swing.util;

import de.ganzer.core.OS;

import javax.swing.Action;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;

/**
 * A utility class that enables tracing of focusable components to enable
 * editableComponent actions globally.
 *
 * @see #install()
 *
 * @since 6.0.0
 */
public final class EditorTracer {
    private static final JTextComponentWrapper wrapper = new JTextComponentWrapper();

    private static EditableComponent editableComponent;

    /**
     * Enables the trace of editableComponent controls. This should be called once at the
     * start of a Swing-based application.
     */
    public static void install() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", e -> {
            editableComponent = getEditable(e.getNewValue());

            if (editableComponent == null)
                editableComponent = getEditable(KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner());
        });
    }

    /**
     * Gets a value that indicates whether the current editor is able to undo
     * the last undoable action.
     *
     * @return {@code true} if an action can be undone.
     */
    public static boolean canUndo() {
        return editableComponent != null && editableComponent.canUndo();
    }

    /**
     * Undoes the last undoable action.
     */
    public static void undo() {
        if (canUndo())
            editableComponent.undo();
    }

    /**
     * Gets a value that indicates whether the current editor is able to redo an
     * undone action.
     *
     * @return {@code true} if an action can be redone.
     */
    public static boolean canRedo() {
        return editableComponent != null && editableComponent.canRedo();
    }

    /**
     * Redoes the last undone action.
     */
    public static void redo() {
        if (canRedo())
            editableComponent.redo();
    }

    /**
     * Indicates whether a "Cut" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    public static boolean canCut() {
        return editableComponent != null && editableComponent.canCut();
    }

    /**
     * Performs a "Cut" action.
     */
    public static void cut() {
        if (canCut())
            editableComponent.cut();
    }

    /**
     * Indicates whether a "Copy" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    public static boolean canCopy() {
        return editableComponent != null && editableComponent.canCopy();
    }

    /**
     * Performs a "Copy" action.
     */
    public static void copy() {
        if (canCopy())
            editableComponent.copy();
    }

    /**
     * Indicates whether a "Paste" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    public static boolean canPaste() {
        return editableComponent != null && editableComponent.canPaste();
    }

    /**
     * Performs a "Paste" action.
     */
    public static void paste() {
        if (canPaste())
            editableComponent.paste();
    }

    /**
     * Indicates whether a "Delete" action is currently supported.
     *
     * @return {@code true} if the action is currently supported.
     */
    public static boolean canDelete() {
        return editableComponent != null && editableComponent.canDelete();
    }

    /**
     * Performs a "Delete" action.
     */
    public static void delete() {
        if (canDelete())
            editableComponent.delete();
    }

    private static EditableComponent getEditable(Object control) {
        if (control instanceof EditableComponent e)
            return e;

        if (control instanceof JTextComponent c) {
            wrapper.setComponent(c);
            return wrapper;
        }

        return null;
    }

    private static class JTextComponentWrapper implements EditableComponent {
        private JTextComponent component;

        public void setComponent(JTextComponent component) {
            this.component = component;
        }

        @Override
        public boolean canUndo() {
            Action undo = component.getActionMap().get("undo");
            return undo != null && undo.isEnabled();
        }

        @Override
        public void undo() {
            Action undo = component.getActionMap().get("undo");
            undo.actionPerformed(new ActionEvent(component, ActionEvent.ACTION_PERFORMED, null));
        }

        @Override
        public boolean canRedo() {
            Action redo = component. getActionMap().get("redo");
            return redo != null && redo.isEnabled();
        }

        @Override
        public void redo() {
            Action redo = component.getActionMap().get("redo");
            redo.actionPerformed(new ActionEvent(component, ActionEvent.ACTION_PERFORMED, null));
        }

        @Override
        public boolean canCut() {
            return component.isEditable() && component.getSelectedText() != null;
        }

        @Override
        public void cut() {
            component.cut();
        }

        @Override
        public boolean canCopy() {
            return component.getSelectedText() != null;
        }

        @Override
        public void copy() {
            component.copy();
        }

        @Override
        public boolean canPaste() {
            if (!component.isEditable())
                return false;

            try {
                var cb = Toolkit.getDefaultToolkit().getSystemClipboard();
                return cb.isDataFlavorAvailable(DataFlavor.stringFlavor);
            } catch (IllegalStateException ex) {
                return false;
            }
        }

        @Override
        public void paste() {
            component.paste();
        }

        @Override
        public boolean canDelete() {
            if (OS.isMac()) {
                return component.isEditable()
                        && (component.getSelectedText() != null || component.getSelectionStart() > 0);
            } else {
                return component.isEditable()
                        && (component.getSelectedText() != null || component.getSelectionStart() < component.getText().length());
            }
        }

        @Override
        public void delete() {
            Action deleteAction = OS.isMac()
                    ? component.getActionMap().get(DefaultEditorKit.deletePrevCharAction)
                    : component.getActionMap().get(DefaultEditorKit.deleteNextCharAction);

            if (deleteAction != null && deleteAction.isEnabled())
                deleteAction.actionPerformed(new ActionEvent(component, ActionEvent.ACTION_PERFORMED, null));
        }
    }
}
