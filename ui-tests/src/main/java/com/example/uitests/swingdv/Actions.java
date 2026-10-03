package com.example.uitests.swingdv;

import com.example.uitests.swing.SVGProvider;
import com.example.uitests.swingdv.services.NavigationService;
import de.ganzer.core.OS;
import de.ganzer.dv.*;
import de.ganzer.dv.swing.DVManager;
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
    public static final GAction exitAction;

    public static final GAction optionsAction;

    public static final GAction helpAction;
    public static final GAction aboutAction;

    static {
        var defaultModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        var quitAccel = OS.isMac()
                ? KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.META_DOWN_MASK)
                : KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK);

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
                        DVManager.recentDocsActions,
                        new GSeparatorAction(),
                        DVManager.saveAction
                                .smallIcon(SVGProvider.get("save", 16))
                                .largeIcon(SVGProvider.get("save", 32)),
                        DVManager.saveAllAction
                                .smallIcon(SVGProvider.get("save_all", 16))
                                .largeIcon(SVGProvider.get("save_all", 32)),
                        DVManager.saveAsAction
                                .smallIcon(SVGProvider.get("save_as", 16))
                                .largeIcon(SVGProvider.get("save_as", 32)),
                        new GSeparatorAction(),
                        exitAction = new GAction(OS.isMac() ? "Quit" : "Exit")
                                .shortDescription("Exit the application")
                                .accelerator(quitAccel)
                                .onAction(e -> SwingDVApp.exit())
                ),
                editActions = new GActionGroup("Edit").addAll(
                        DVManager.undoAction
                                .smallIcon(SVGProvider.get("undo", 16))
                                .largeIcon(SVGProvider.get("undo", 32)),
                        DVManager.redoAction
                                .smallIcon(SVGProvider.get("redo", 16))
                                .largeIcon(SVGProvider.get("redo", 32)),
                        new GSeparatorAction(),
                        DVManager.cutAction
                                .smallIcon(SVGProvider.get("cut", 16))
                                .largeIcon(SVGProvider.get("cut", 32)),
                        DVManager.copyAction
                                .smallIcon(SVGProvider.get("copy", 16))
                                .largeIcon(SVGProvider.get("copy", 32)),
                        DVManager.pasteAction
                                .smallIcon(SVGProvider.get("paste", 16))
                                .largeIcon(SVGProvider.get("paste", 32)),
                        DVManager.deleteAction
                                .smallIcon(SVGProvider.get("delete", 16))
                                .largeIcon(SVGProvider.get("delete", 32))
                ),
                windowActions = new GActionGroup("Window").addAll(
                        DVManager.closeWindowAction
                                .smallIcon(SVGProvider.get("window_close", 16))
                                .largeIcon(SVGProvider.get("window_close", 32)),
                        DVManager.closeAllWindowsAction
                                .smallIcon(SVGProvider.get("windows_close", 16))
                                .largeIcon(SVGProvider.get("windows_close", 32)),
                        new GSeparatorAction(),
                        DVManager.chooseWindowActions
                ),
                settingsActions = new GActionGroup("Settings").addAll(
                        optionsAction = new GAction("Options")
                                .shortDescription("Open options-dialog")
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
                                .shortDescription("Show about-dialog")
                                .smallIcon(SVGProvider.get("about", 16))
                                .largeIcon(SVGProvider.get("about", 32))
                                .onAction(e -> {
                                })
                )
        );
    }
}
