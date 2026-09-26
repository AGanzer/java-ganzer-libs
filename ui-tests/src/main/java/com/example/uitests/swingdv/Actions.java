package com.example.uitests.swingdv;

import com.example.uitests.swing.SVGProvider;
import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.actions.GActionGroup;
import de.ganzer.swing.actions.GSeparatorAction;

public class Actions {
    public static final GActionGroup allActions;
    public static final GActionGroup fileActions;
    public static final GActionGroup editActions;
    public static final GActionGroup windowActions;
    public static final GActionGroup settingsActions;
    public static final GActionGroup helpActions;

    public static final GActionGroup newActionGroup;
    public static final GAction openAction;
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
        allActions = new GActionGroup().addAll(
                fileActions = new GActionGroup("File").addAll(
                        newActionGroup = new GActionGroup("New"),
                        openAction = new GAction("Open...")
                                .smallIcon(SVGProvider.get("document_open", 16))
                                .largeIcon(SVGProvider.get("document_open", 32))
                                .onAction(e -> {}),
                        new GSeparatorAction(),
                        saveAction = new GAction("Save")
                                .smallIcon(SVGProvider.get("save", 16))
                                .largeIcon(SVGProvider.get("save", 32))
                                .onAction(e -> {}),
                        new GSeparatorAction(),
                        saveAllAction = new GAction("Save All")
                                .smallIcon(SVGProvider.get("save_all", 16))
                                .largeIcon(SVGProvider.get("save_all", 32))
                                .onAction(e -> {}),
                        saveAsAction = new GAction("Save As...")
                                .smallIcon(SVGProvider.get("save_as", 16))
                                .largeIcon(SVGProvider.get("save_as", 32))
                                .onAction(e -> {}),
                        new GSeparatorAction(),
                        exitAction = new GAction("Exit")
                                .onAction(e -> {})
                ),
                editActions = new GActionGroup("Edit").addAll(
                        undoAction = new GAction("Undo")
                                .smallIcon(SVGProvider.get("undo", 16))
                                .largeIcon(SVGProvider.get("undo", 32))
                                .onAction(e -> {}),
                        redoAction = new GAction("Redo")
                                .smallIcon(SVGProvider.get("redo", 16))
                                .largeIcon(SVGProvider.get("redo", 32))
                                .onAction(e -> {}),
                        new GSeparatorAction(),
                        cutAction = new GAction("Cut")
                                .smallIcon(SVGProvider.get("cut", 16))
                                .largeIcon(SVGProvider.get("cut", 32))
                                .onAction(e -> {}),
                        copyAction = new GAction("Copy")
                                .smallIcon(SVGProvider.get("copy", 16))
                                .largeIcon(SVGProvider.get("copy", 32))
                                .onAction(e -> {}),
                        pasteAction = new GAction("Paste")
                                .smallIcon(SVGProvider.get("paste", 16))
                                .largeIcon(SVGProvider.get("paste", 32))
                                .onAction(e -> {}),
                        deleteAction = new GAction("Delete")
                                .smallIcon(SVGProvider.get("delete", 16))
                                .largeIcon(SVGProvider.get("delete", 32))
                                .onAction(e -> {})
                ),
                windowActions = new GActionGroup("Window").addAll(
                        closeWindowAction = new GAction("Close")
                                .smallIcon(SVGProvider.get("window_close", 16))
                                .largeIcon(SVGProvider.get("window_close", 32))
                                .onAction(e -> {}),
                        closeAllWindowsAction = new GAction("Close All")
                                .smallIcon(SVGProvider.get("windows_close", 16))
                                .largeIcon(SVGProvider.get("windows_close", 32))
                                .onAction(e -> {})
                ),
                settingsActions = new GActionGroup("Settings").addAll(
                        optionsAction = new GAction("Options")
                                .onAction(e -> {})
                ),
                helpActions = new GActionGroup("Help").addAll(
                        helpAction = new GAction("Help")
                                .smallIcon(SVGProvider.get("help", 16))
                                .largeIcon(SVGProvider.get("help", 32))
                                .onAction(e -> {}),
                        new GSeparatorAction(),
                        aboutAction = new GAction("About")
                                .smallIcon(SVGProvider.get("about", 16))
                                .largeIcon(SVGProvider.get("about", 32))
                                .onAction(e -> {})
                )
        );
    }
}
