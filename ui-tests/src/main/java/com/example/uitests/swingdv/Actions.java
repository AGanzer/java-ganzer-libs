package com.example.uitests.swingdv;

import com.example.uitests.swing.SVGProvider;
import com.example.uitests.swingdv.services.NavigationService;
import de.ganzer.core.OS;
import de.ganzer.dv.*;
import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.actions.GActionGroup;
import de.ganzer.swing.actions.GSeparatorAction;
import de.ganzer.swing.actions.GToggleActionGroup;
import de.ganzer.swing.util.EditorTracer;

import javax.swing.KeyStroke;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class Actions {
    public static final GActionGroup allActions;
    public static final GActionGroup fileActions;
    public static final GActionGroup editActions;
    public static final GActionGroup windowActions;
    public static final GActionGroup settingsActions;
    public static final GActionGroup helpActions;

    public static final GAction newAction;
    public static final GAction openAction;
    public static final GActionGroup recentDocsActions;
    public static final GAction saveAction;
    public static final GAction saveAllAction;
    public static final GAction saveAsAction;
    public static final GAction exitAction;

    public static final GAction undoAction;
    public static final GAction redoAction;
    public static final GAction cutAction;
    public static final GAction copyAction;
    public static final GAction pasteAction;
    public static final GAction deleteAction;

    public static final GAction closeWindowAction;
    public static final GAction closeAllWindowsAction;
    public static final GToggleActionGroup chooseWindowActions;

    public static final GAction optionsAction;

    public static final GAction helpAction;
    public static final GAction aboutAction;

    @SuppressWarnings("DataFlowIssue")
    public static void updateEditActions() {
        var tracer = EditorTracer.getInstance();
        var doc = DVManager.getActiveDocument();

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

    public static void updateSaveActions() {
        var doc = DVManager.getActiveDocument();
        saveAction.setEnabled(doc != null && doc.isModified());
        saveAsAction.setEnabled(doc != null && doc.isSaveAsSupported());
        saveAllAction.setEnabled(DVManager.getOpenDocuments().stream().anyMatch(Document::isModified));
    }

    private static final GAction[] windowToggleActions = new GAction[20];

    private static final PropertyChangeListener documentNameListener = evt ->
            Arrays.stream(windowToggleActions)
                    .filter(a -> a.getTag().equals(evt.getSource()))
                    .findFirst()
                    .ifPresent(action -> action.setName(evt.getNewValue().toString()));
    private static final PropertyChangeListener documentModifiedListener = evt -> updateSaveActions();

    static {
        var defaultModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        var quitAccel = OS.isMac()
                ? KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.META_DOWN_MASK)
                : KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK);
        var closeAccel = OS.isMac()
                ? KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.META_DOWN_MASK)
                : OS.isWindows()
                    ? KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_DOWN_MASK)
                    : KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK);

        for (int i = 0; i < windowToggleActions.length; i++) {
            windowToggleActions[i] = new GAction()
                    .visible(false)
                    .enabled(false)
                    .onAction(e -> DVManager.activateDocument((Document) (((GAction) e.getSource()).getTag())));

            if (i < 9)
                windowToggleActions[i].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + i, InputEvent.ALT_DOWN_MASK));
            else if (i == 9)
                windowToggleActions[i].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.ALT_DOWN_MASK));
        }

        allActions = new GActionGroup().addAll(
                fileActions = new GActionGroup("File").addAll(
                        newAction = new GAction("New...")
                                .shortDescription("Create a new document")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, defaultModifier))
                                .smallIcon(SVGProvider.get("document_new", 16))
                                .largeIcon(SVGProvider.get("document_new", 32))
                                .onAction(e -> DVManager.createDocument(null)),
                        openAction = new GAction("Open...")
                                .shortDescription("Open an existing document")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, defaultModifier))
                                .smallIcon(SVGProvider.get("document_open", 16))
                                .largeIcon(SVGProvider.get("document_open", 32))
                                .onAction(e -> {
                                    try {
                                        DVManager.openDocuments(null, false);
                                    } catch (DVLoadException ex) {
                                        NavigationService.getInstance().showError(ex.getLocalizedMessage(), ex);
                                    }
                                }),
                        new GSeparatorAction(),
                        recentDocsActions = new GActionGroup("Recent Documents")
                                .enabled(false),
                        new GSeparatorAction(),
                        saveAction = new GAction("Save")
                                .shortDescription("Save the active document")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, defaultModifier))
                                .smallIcon(SVGProvider.get("save", 16))
                                .largeIcon(SVGProvider.get("save", 32))
                                .enabled(false)
                                .onAction(e -> {
                                    try {
                                        DVManager.saveActiveDocument();
                                    } catch (DVSaveException ex) {
                                        NavigationService.getInstance().showError(ex.getLocalizedMessage(), ex);
                                    }
                                }),
                        saveAllAction = new GAction("Save All")
                                .shortDescription("Save all documents")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, defaultModifier | InputEvent.SHIFT_DOWN_MASK))
                                .smallIcon(SVGProvider.get("save_all", 16))
                                .largeIcon(SVGProvider.get("save_all", 32))
                                .enabled(false)
                                .onAction(e -> {
                                    try {
                                        DVManager.saveAllDocuments();
                                    } catch (DVSaveException ex) {
                                        NavigationService.getInstance().showError(ex.getLocalizedMessage(), ex);
                                    }
                                }),
                        saveAsAction = new GAction("Save As...")
                                .shortDescription("Save the active document with a new name")
                                .smallIcon(SVGProvider.get("save_as", 16))
                                .largeIcon(SVGProvider.get("save_as", 32))
                                .enabled(false)
                                .onAction(e -> {
                                    try {
                                        DVManager.saveActiveDocumentAs();
                                    } catch (DVSaveException ex) {
                                        NavigationService.getInstance().showError(ex.getLocalizedMessage(), ex);
                                    }
                                }),
                        new GSeparatorAction(),
                        exitAction = new GAction(OS.isMac() ? "Quit" : "Exit")
                                .shortDescription("Exit the application")
                                .accelerator(quitAccel)
                                .onAction(e -> SwingDVApp.exit())
                ),
                editActions = new GActionGroup("Edit").addAll(
                        undoAction = new GAction("Undo")
                                .shortDescription("Undo the last action")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, defaultModifier))
                                .smallIcon(SVGProvider.get("undo", 16))
                                .largeIcon(SVGProvider.get("undo", 32))
                                .enabled(false)
                                .onAction(e -> {
                                    var doc = DVManager.getActiveDocument();
                                    if (doc != null) {
                                        doc.undo();
                                    }
                                }),
                        redoAction = new GAction("Redo")
                                .shortDescription("Redo the last undone action")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, defaultModifier))
                                .smallIcon(SVGProvider.get("redo", 16))
                                .largeIcon(SVGProvider.get("redo", 32))
                                .enabled(false)
                                .onAction(e -> {
                                    var doc = DVManager.getActiveDocument();
                                    if (doc != null) {
                                        doc.redo();
                                    }
                                }),
                        new GSeparatorAction(),
                        cutAction = new GAction("Cut")
                                .shortDescription("Cut the selected text")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, defaultModifier))
                                .smallIcon(SVGProvider.get("cut", 16))
                                .largeIcon(SVGProvider.get("cut", 32))
                                .enabled(false)
                                .onAction(e -> EditorTracer.getInstance().cut()),
                        copyAction = new GAction("Copy")
                                .shortDescription("Copy the selected text")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, defaultModifier))
                                .smallIcon(SVGProvider.get("copy", 16))
                                .largeIcon(SVGProvider.get("copy", 32))
                                .enabled(false)
                                .onAction(e -> EditorTracer.getInstance().copy()),
                        pasteAction = new GAction("Paste")
                                .shortDescription("Paste the text from the clipboard")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, defaultModifier))
                                .smallIcon(SVGProvider.get("paste", 16))
                                .largeIcon(SVGProvider.get("paste", 32))
                                .enabled(false)
                                .onAction(e -> EditorTracer.getInstance().paste()),
                        deleteAction = new GAction("Delete")
                                .shortDescription("Delete the selected text")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0))
                                .smallIcon(SVGProvider.get("delete", 16))
                                .largeIcon(SVGProvider.get("delete", 32))
                                .enabled(false)
                                .onAction(e -> EditorTracer.getInstance().delete())
                ),
                windowActions = new GActionGroup("Window").addAll(
                        closeWindowAction = new GAction("Close")
                                .shortDescription("Close the current window")
                                .accelerator(closeAccel)
                                .smallIcon(SVGProvider.get("window_close", 16))
                                .largeIcon(SVGProvider.get("window_close", 32))
                                .enabled(false)
                                .onAction(e -> {
                                }),
                        closeAllWindowsAction = new GAction("Close All")
                                .shortDescription("Close all windows")
                                .smallIcon(SVGProvider.get("windows_close", 16))
                                .largeIcon(SVGProvider.get("windows_close", 32))
                                .enabled(false)
                                .onAction(e -> {
                                }),
                        new GSeparatorAction(),
                        chooseWindowActions = new GToggleActionGroup().addAll(
                                windowToggleActions
                        )
                ),
                settingsActions = new GActionGroup("Settings").addAll(
                        optionsAction = new GAction("Options")
                                .shortDescription("Open options dialog")
                                .onAction(e -> {
                                })
                ),
                helpActions = new GActionGroup("Help").addAll(
                        helpAction = new GAction("Help")
                                .shortDescription("Show help")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0))
                                .smallIcon(SVGProvider.get("help", 16))
                                .largeIcon(SVGProvider.get("help", 32))
                                .onAction(e -> {
                                }),
                        new GSeparatorAction(),
                        aboutAction = new GAction("About")
                                .shortDescription("Show about dialog")
                                .smallIcon(SVGProvider.get("about", 16))
                                .largeIcon(SVGProvider.get("about", 32))
                                .onAction(e -> {
                                })
                )
        );

        DVManager.addPropertyChangeListener(DVManager.ACTIVE_DOCUMENT_PROPERTY, e -> {
            var docOld = (e.getOldValue() instanceof Document d) ? d : null;
            var docNew = (e.getNewValue() instanceof Document d) ? d : null;

            if (docOld != null) {
                docOld.removePropertyChangeListener(Document.NAME_PROPERTY, documentNameListener);
                docOld.removePropertyChangeListener(Document.MODIFIED_PROPERTY, documentModifiedListener);
            }

            if (docNew != null) {
                docNew.addPropertyChangeListener(Document.NAME_PROPERTY, documentNameListener);
                docNew.addPropertyChangeListener(Document.MODIFIED_PROPERTY, documentModifiedListener);
            }

            saveAction.setEnabled(docNew != null && docNew.isModified());
            saveAsAction.setEnabled(docNew != null && docNew.isSaveAsSupported());
            saveAllAction.setEnabled(DVManager.getOpenDocuments().stream().anyMatch(Document::isModified));

            System.out.println("Active document changed: " + (docNew != null ? docNew.getName() : "no active one"));
        });

        DVManager.addPropertyChangeListener(DVManager.ACTIVE_VIEW_PROPERTY, e -> {
            var view = (e.getNewValue() instanceof View<?> v) ? v : null;
            System.out.println("Active view changed: " + (view != null ? view.getTitle() : "no active one"));
        });

        DVManager.addPropertyChangeListener(DVManager.OPEN_DOCUMENTS_PROPERTY, e -> {
            int index = 0;

            for (var doc : DVManager.getOpenDocuments()) {
                windowToggleActions[index++]
                        .visible(true)
                        .enabled(true)
                        .name(doc.getName())
                        .tag(doc)
                        .selected(doc == DVManager.getActiveDocument());
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
