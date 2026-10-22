package com.example.uitests.fxdv;

import de.ganzer.fx.actions.Action;
import de.ganzer.fx.actions.ActionGroup;
import de.ganzer.fx.actions.SeparatorAction;

public final class Actions {
    public static final ActionGroup fileActions = new ActionGroup("_File");
    public static final ActionGroup editActions = new ActionGroup("_Edit");
    public static final ActionGroup windowActions = new ActionGroup("_Windows");
    public static final ActionGroup extrasActions = new ActionGroup("E_xtras");
    public static final ActionGroup helpActions = new ActionGroup("_Help");

    public static final Action newAction = new Action("_New");
    public static final Action openAction = new Action("_Open");
    public static final Action saveAction = new Action("_Save");
    public static final Action saveAsAction = new Action("Save _As");
    public static final Action saveAllAction = new Action("Save A_ll");
    public static final Action exitAction = new Action("E_xit");

    public static final Action undoAction = new Action("_Undo");
    public static final Action redoAction = new Action("_Redo");
    public static final Action cutAction = new Action("_Cut");
    public static final Action copyAction = new Action("C_opy");
    public static final Action pasteAction = new Action("_Paste");
    public static final Action deleteAction = new Action("_Delete");

    public static final Action closeAction = new Action("_Close");
    public static final Action closeAllAction = new Action("Close _All");

    public static final Action settingsAction = new Action("_Settings");

    public static final Action helpAction = new Action("_Help");
    public static final Action aboutAction = new Action("_About");

    static {
        fileActions.addAll(
                newAction,
                openAction,
                new SeparatorAction(),
                saveAction,
                saveAsAction,
                saveAllAction,
                new SeparatorAction(),
                exitAction);
        editActions.addAll(
                undoAction,
                redoAction,
                new SeparatorAction(),
                cutAction,
                copyAction,
                pasteAction,
                deleteAction);
        windowActions.addAll(
                closeAction,
                closeAllAction);
        extrasActions.addAll(
                settingsAction);
        helpActions.addAll(
                helpAction,
                new SeparatorAction(),
                aboutAction);
    }

    private Actions() {
    }
}
