package com.example.uitests.swingdv;

import com.example.uitests.swing.SVGProvider;
import de.ganzer.core.OS;
import de.ganzer.dv.*;
import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.actions.GActionGroup;
import de.ganzer.swing.actions.GSeparatorAction;

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

    public static final GAction optionsAction;

    public static final GAction helpAction;
    public static final GAction aboutAction;

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
                                        DVNavigationService.getInstance().showError(ex.getLocalizedMessage(), ex);
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
                                        DVNavigationService.getInstance().showError(ex.getLocalizedMessage(), ex);
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
                                        DVNavigationService.getInstance().showError(ex.getLocalizedMessage(), ex);
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
                                        DVNavigationService.getInstance().showError(ex.getLocalizedMessage(), ex);
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
                                })
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
        });

        DVManager.addPropertyChangeListener(DVManager.ACTIVE_VIEW_PROPERTY, e -> {
        });
    }
}
