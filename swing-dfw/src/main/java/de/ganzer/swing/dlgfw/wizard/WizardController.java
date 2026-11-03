package de.ganzer.swing.dlgfw.wizard;

import de.ganzer.swing.actions.GAction;

public class WizardController {
    private final GAction nextAction ;
    private final GAction prevAction;
    private final GAction finishAction;
    private final GAction cancelAction;
    private final GAction helpAction;

    public WizardController() {
        nextAction = new GAction();
        prevAction = new GAction();
        finishAction = new GAction();
        cancelAction = new GAction();
        helpAction = new GAction();
    }

    public GAction getNextAction() {
        return nextAction;
    }

    public GAction getPrevAction() {
        return prevAction;
    }

    public GAction getFinishAction() {
        return finishAction;
    }

    public GAction getCancelAction() {
        return cancelAction;
    }

    public GAction getHelpAction() {
        return helpAction;
    }
}
