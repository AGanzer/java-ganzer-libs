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

    public static void updateIdleActions() {
        var tracer = EditorTracer.getInstance();

        undoAction.setEnabled(tracer.canUndo());
        redoAction.setEnabled(tracer.canRedo());
        cutAction.setEnabled(tracer.canCut());
        copyAction.setEnabled(tracer.canCopy());
        pasteAction.setEnabled(tracer.canPaste());
        deleteAction.setEnabled(tracer.canDelete());
    }

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
        var windowToggleActions = new GAction[10];

        for (int i = 0; i < windowToggleActions.length; i++) {
            windowToggleActions[i] = new GAction()
                    .visible(false)
                    .enabled(false)
                    .onAction(e -> DVManager.activateDocument((Document) (((GAction) e.getSource()).getTag())));
        }

        allActions = new GActionGroup().addAll(
                fileActions = new GActionGroup("File").addAll(
                        newAction = new GAction("New...")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, defaultModifier))
                                .smallIcon(SVGProvider.get("document_new", 16))
                                .largeIcon(SVGProvider.get("document_new", 32))
                                .onAction(e -> DVManager.createDocument(null)),
                        openAction = new GAction("Open...")
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
                                .accelerator(quitAccel)
                                .onAction(e -> SwingDVApp.exit())
                ),
                editActions = new GActionGroup("Edit").addAll(
                        undoAction = new GAction("Undo")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, defaultModifier))
                                .smallIcon(SVGProvider.get("undo", 16))
                                .largeIcon(SVGProvider.get("undo", 32))
                                .enabled(false)
                                .onAction(e -> {
                                }),
                        redoAction = new GAction("Redo")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, defaultModifier))
                                .smallIcon(SVGProvider.get("redo", 16))
                                .largeIcon(SVGProvider.get("redo", 32))
                                .enabled(false)
                                .onAction(e -> {
                                }),
                        new GSeparatorAction(),
                        cutAction = new GAction("Cut")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, defaultModifier))
                                .smallIcon(SVGProvider.get("cut", 16))
                                .largeIcon(SVGProvider.get("cut", 32))
                                .enabled(false)
                                .onAction(e -> {
                                }),
                        copyAction = new GAction("Copy")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, defaultModifier))
                                .smallIcon(SVGProvider.get("copy", 16))
                                .largeIcon(SVGProvider.get("copy", 32))
                                .enabled(false)
                                .onAction(e -> {
                                }),
                        pasteAction = new GAction("Paste")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, defaultModifier))
                                .smallIcon(SVGProvider.get("paste", 16))
                                .largeIcon(SVGProvider.get("paste", 32))
                                .enabled(false)
                                .onAction(e -> {
                                }),
                        deleteAction = new GAction("Delete")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0))
                                .smallIcon(SVGProvider.get("delete", 16))
                                .largeIcon(SVGProvider.get("delete", 32))
                                .enabled(false)
                                .onAction(e -> {
                                })
                ),
                windowActions = new GActionGroup("Window").addAll(
                        closeWindowAction = new GAction("Close")
                                .accelerator(closeAccel)
                                .smallIcon(SVGProvider.get("window_close", 16))
                                .largeIcon(SVGProvider.get("window_close", 32))
                                .enabled(false)
                                .onAction(e -> {
                                }),
                        closeAllWindowsAction = new GAction("Close All")
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
                                .onAction(e -> {
                                })
                ),
                helpActions = new GActionGroup("Help").addAll(
                        helpAction = new GAction("Help")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0))
                                .smallIcon(SVGProvider.get("help", 16))
                                .largeIcon(SVGProvider.get("help", 32))
                                .onAction(e -> {
                                }),
                        new GSeparatorAction(),
                        aboutAction = new GAction("About")
                                .smallIcon(SVGProvider.get("about", 16))
                                .largeIcon(SVGProvider.get("about", 32))
                                .onAction(e -> {
                                })
                )
        );

        DVManager.addPropertyChangeListener(DVManager.ACTIVE_DOCUMENT_PROPERTY, e -> {
            var doc = (e.getNewValue() instanceof Document d) ? d : null;

            saveAction.setEnabled(doc != null && !doc.isReadOnly());
            saveAsAction.setEnabled(doc != null && doc.isSaveAsSupported());
            saveAllAction.setEnabled(DVManager.getOpenDocuments().stream().anyMatch(Document::isModified));

            System.out.println("Active document changed: " + (doc != null ? doc.getName() : "no active one"));
        });

        DVManager.addPropertyChangeListener(DVManager.ACTIVE_VIEW_PROPERTY, e -> {
            var view = (e.getNewValue() instanceof View<?> v) ? v : null;
            System.out.println("Active document changed: " + (view != null ? view.getTitle() : "no active one"));
        });

        DVManager.addPropertyChangeListener(DVManager.OPEN_DOCUMENTS_PROPERTY, e -> {
            int index = 0;

            for (var doc : DVManager.getOpenDocuments()) {
                windowToggleActions[index++]
                        .visible(true)
                        .enabled(true)
                        .name(index + ": " + doc.getName())
                        .tag(doc)
                        .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0 + index, InputEvent.ALT_DOWN_MASK));
            }

            for (int i = index; i < windowToggleActions.length; i++) {
                windowToggleActions[i].visible(false).enabled(false);
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
