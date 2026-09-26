package com.example.uitests.swingdv;

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

    static {
        allActions = new GActionGroup().addAll(
                fileActions = new GActionGroup("File").addAll(
                        newActionGroup = new GActionGroup("New"),
                        openAction = new GAction("Open..."),
                        new GSeparatorAction(),
                        saveAction = new GAction("Save"),
                        saveAllAction = new GAction("Save All"),
                        saveAsAction = new GAction("Save As..."),
                        new GSeparatorAction(),
                        exitAction = new GAction("Exit")
                ),
                editActions = new GActionGroup("Edit").addAll(
                        undoAction = new GAction("Undo"),
                        redoAction = new GAction("Redo"),
                        new GSeparatorAction(),
                        cutAction = new GAction("Cut"),
                        copyAction = new GAction("Copy"),
                        pasteAction = new GAction("Paste"),
                        deleteAction = new GAction("Delete")
                ),
                windowActions = new GActionGroup("Window"),
                settingsActions = new GActionGroup("Settings"),
                helpActions = new GActionGroup("Help")
        );
    }
}
