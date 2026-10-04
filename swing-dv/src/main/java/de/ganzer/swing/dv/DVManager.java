package de.ganzer.swing.dv;

import de.ganzer.core.OS;
import de.ganzer.core.Services;
import de.ganzer.dv.BasicDVManager;
import de.ganzer.dv.DVSaveException;
import de.ganzer.dv.Document;
import de.ganzer.dv.View;
import de.ganzer.dv.services.DVNavigationService;
import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.actions.GActionGroup;
import de.ganzer.swing.actions.GToggleActionGroup;
import de.ganzer.swing.util.EditorTracer;

import javax.swing.KeyStroke;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

/**
 * A document manager for Swing-based GUI applications.
 * <p>
 * The manager needs some support to perform certain operations that depend
 * on the used UI framework. This support should be installed once at
 * application startup
 *
 * @see #registerSupport(DVManagerSupport)
 * @see DVManagerSupport
 * @see BasicDVManager
 *
 * @since 6.0.0
 */
public final class DVManager extends BasicDVManager {
    private static final GAction[] windowToggleActions = new GAction[20];

    /**
     * The action where the recently opened documents are inserted into.
     */
    public static final GActionGroup recentDocsActions = new GActionGroup("Recent Documents")
                                .enabled(false);
    /**
     * The action that saves a document.
     */
    public static final GAction saveAction = new GAction("Save")
            .shortDescription("Save the active document")
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> {
                try {
                    saveActiveDocument();
                } catch (DVSaveException ex) {
                    ((DVNavigationService) Services.get(DVNavigationService.class)).showError(ex.getLocalizedMessage(), ex);
                }
            });
    /**
     * The action that saves all documents.
     */
    public static final GAction saveAllAction = new GAction("Save All")
            .shortDescription("Save all documents")
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK))
            .enabled(false)
            .onAction(e -> {
                try {
                    saveAllDocuments();
                } catch (DVSaveException ex) {
                    ((DVNavigationService) Services.get(DVNavigationService.class)).showError(ex.getLocalizedMessage(), ex);
                }
            });
    /**
     * The action that saves the active document with a new name.
     */
    public static final GAction saveAsAction = new GAction("Save As...")
            .shortDescription("Save the active document with a new name")
            .enabled(false)
            .onAction(e -> {
                try {
                    saveActiveDocumentAs();
                } catch (DVSaveException ex) {
                    ((DVNavigationService) Services.get(DVNavigationService.class)).showError(ex.getLocalizedMessage(), ex);
                }
            });
    /**
     * The action that undoes the last action.
     */
    public static final GAction undoAction = new GAction("Undo")
            .shortDescription("Undo the last action")
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> {
                var doc = getActiveDocument();
                if (doc != null) {
                    doc.undo();
                }
            });
    /**
     * The action that redoes the last undone action.
     */
    public static final GAction redoAction = new GAction("Redo")
            .shortDescription("Redo the last undone action")
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> {
                var doc = getActiveDocument();
                if (doc != null) {
                    doc.redo();
                }
            });
    /**
     * The action that cuts the selected text.
     */
    public static final GAction cutAction = new GAction("Cut")
            .shortDescription("Cut the selected text")
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> EditorTracer.getInstance().cut());
    /**
     * The action that copies the selected text.
     */
    public static final GAction copyAction = new GAction("Copy")
            .shortDescription("Copy the selected text")
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> EditorTracer.getInstance().copy());
    /**
     * The action that pastes the text from the clipboard.
     */
    public static final GAction pasteAction = new GAction("Paste")
            .shortDescription("Paste the text from the clipboard")
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()))
            .enabled(false)
            .onAction(e -> EditorTracer.getInstance().paste());
    /**
     * The action that deletes the selected text.
     */
    public static final GAction deleteAction = new GAction("Delete")
            .shortDescription("Delete the selected text")
            .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0))
            .enabled(false)
            .onAction(e -> EditorTracer.getInstance().delete());
    /**
     * The action that closes the current window.
     */
    public static final GAction closeWindowAction = new GAction("Close")
            .shortDescription("Close the current window")
            .accelerator(OS.isMac()
                                 ? KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.META_DOWN_MASK)
                                 : OS.isWindows()
                    ? KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_DOWN_MASK)
                    : KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK))
            .enabled(false)
            .onAction(e -> {
                var view = getSupport().getActiveMDISubView();

                if (view != null && view.getTemplate().isClosable() && view.getDocument().canCloseView(view)) {
                    view.getDocument().removeView(view);
                    view.forceClose();
                }
            });
    /**
     * The action that closes all windows.
     */
    public static final GAction closeAllWindowsAction = new GAction("Close All")
            .shortDescription("Close all windows")
            .enabled(false)
            .onAction(e -> {
                var views = getSupport().getAllMDISubViews();

                for (var view : views) {
                    if (!view.getTemplate().isClosable())
                        continue;

                    if (!view.getDocument().canCloseView(view))
                        break;

                    view.getDocument().removeView(view);
                    view.forceClose();
                }
            });
    /**
     * The action that contains the open windows actions.
     */
    public static final GToggleActionGroup chooseWindowActions = new GToggleActionGroup();

    /**
     * Updates the edit actions.
     * <p>
     * This should be called once each time when the application enters the idle
     * mode.
     */
    @SuppressWarnings("DataFlowIssue")
    public static void updateEditActions() {
        var tracer = EditorTracer.getInstance();
        var doc = getActiveDocument();

        undoAction.setEnabled(doc != null && doc.canUndo());
        redoAction.setEnabled(doc != null && doc.canRedo());
        cutAction.setEnabled(tracer.canCut());
        copyAction.setEnabled(tracer.canCopy());
        pasteAction.setEnabled(tracer.canPaste());
        deleteAction.setEnabled(tracer.canDelete());

        undoAction.name(undoAction.isEnabled() ? "Undo " + doc.getUndoTitle() : "Undo")
                .shortDescription(undoAction.isEnabled() ? "Undo " + doc.getUndoTitle() : "Undo the last action\"");
        redoAction.name(redoAction.isEnabled() ? "Redo " + doc.getRedoTitle() : "Redo")
                .shortDescription(redoAction.isEnabled() ? "Redo " + doc.getRedoTitle() : "Redo the last undone action");
    }

    private static DVManagerSupport support;

    /**
     * Sets the support for the BasicDVManager.
     * <p>
     * The manager needs some support to perform certain operations that depend
     * on the used UI framework. This support should be installed once at
     * application startup.
     *
     * @param support The support to set.
     */
    public static void registerSupport(DVManagerSupport support) {
        DVManager.support = support;
    }

    /**
     * Checks if the support for the {@link DVManager} is registered.
     *
     * @return {@code true} if the support is registered, {@code false}
     *          otherwise.
     */
    public static boolean isSupportRegistered() {
        return support != null;
    }

    /**
     * Gets the registered support.
     *
     * @return The registered support.
     *
     * @throws IllegalStateException if no support is registered.
     *
     * @see #registerSupport(DVManagerSupport)
     * @see #isSupportRegistered()
     */
    public static DVManagerSupport getSupport() {
        if (support == null)
            throw new IllegalStateException("DVManagerSupport is not registered.");

        return support;
    }

    private static void updateSaveActions() {
        var doc = getActiveDocument();
        saveAction.setEnabled(doc != null && doc.isModified());
        saveAsAction.setEnabled(doc != null && doc.isSaveAsSupported());
        saveAllAction.setEnabled(getOpenDocuments().stream().anyMatch(Document::isModified));
    }

    private static final PropertyChangeListener documentNameListener = evt ->
            Arrays.stream(windowToggleActions)
                    .filter(a -> a.getTag().equals(evt.getSource()))
                    .findFirst()
                    .ifPresent(action -> action.setName(evt.getNewValue().toString()));
    private static final PropertyChangeListener documentModifiedListener = evt -> updateSaveActions();

    static {
        for (int i = 0; i < windowToggleActions.length; i++) {
            windowToggleActions[i] = new GAction()
                    .visible(false)
                    .enabled(false)
                    .selectable(true)
                    .onAction(e -> DVManager.activateDocument((Document) (((GAction) e.getSource()).getTag())));

            if (i < 9)
                windowToggleActions[i].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, InputEvent.ALT_DOWN_MASK));
            else if (i == 9)
                windowToggleActions[i].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.ALT_DOWN_MASK));
        }

        chooseWindowActions.addAll(windowToggleActions);

        addPropertyChangeListener(ACTIVE_DOCUMENT_PROPERTY, e -> {
            var docOld = (e.getOldValue() instanceof Document d) ? d : null;
            var docNew = (e.getNewValue() instanceof Document d) ? d : null;

            if (docOld != null) {
                docOld.removePropertyChangeListener(Document.NAME_PROPERTY, documentNameListener);
                docOld.removePropertyChangeListener(Document.MODIFIED_PROPERTY, documentModifiedListener);
            }

            if (docNew != null) {
                docNew.addPropertyChangeListener(Document.NAME_PROPERTY, documentNameListener);
                docNew.addPropertyChangeListener(Document.MODIFIED_PROPERTY, documentModifiedListener);

                for (GAction windowToggleAction : windowToggleActions) {
                    if (windowToggleAction.getTag() == docNew) {
                        windowToggleAction.setSelected(true);
                        break;
                    }
                }
            }

            saveAction.setEnabled(docNew != null && docNew.isModified());
            saveAsAction.setEnabled(docNew != null && docNew.isSaveAsSupported());
            saveAllAction.setEnabled(getOpenDocuments().stream().anyMatch(Document::isModified));

            System.out.println("Active document changed: " + (docNew != null ? docNew.getName() : "no active one"));
        });

        addPropertyChangeListener(ACTIVE_VIEW_PROPERTY, e -> {
            var view = (e.getNewValue() instanceof View<?> v) ? v : null;
            System.out.println("Active view changed: " + (view != null ? view.getTitle() : "no active one"));
        });

        addPropertyChangeListener(OPEN_DOCUMENTS_PROPERTY, e -> {
            int index = 0;

            for (var doc : getOpenDocuments()) {
                windowToggleActions[index++]
                        .visible(true)
                        .enabled(true)
                        .name(doc.getName())
                        .tag(doc)
                        .selected(doc == getActiveDocument());
            }

            for (int i = index; i < windowToggleActions.length; i++) {
                windowToggleActions[i].visible(false).enabled(false).selected(false);
            }

            var docOld = (e.getOldValue() instanceof Document d) ? d : null;
            var docNew = (e.getNewValue() instanceof Document d) ? d : null;

            if (docOld != null)
                System.out.println("Document closed: " + docOld.getName());
            else if (docNew != null)
                System.out.println("Document opened: " + docNew.getName());
            else
                System.err.println("Documents changed but with no document.");
        });
    }
}
