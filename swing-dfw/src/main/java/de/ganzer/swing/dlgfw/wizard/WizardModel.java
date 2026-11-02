package de.ganzer.swing.dlgfw.wizard;

import de.ganzer.swing.actions.GAction;

import java.util.ArrayList;
import java.util.List;

public class WizardModel {
    private final List<WizardPanelDescriptor> panelDescriptors = new ArrayList<>();
    private final GAction nextAction ;
    private final GAction prevAction;
    private final GAction finishAction;
    private final GAction cancelAction;
    private final GAction helpAction;

    public WizardModel() {
        nextAction = new GAction();
        prevAction = new GAction();
        finishAction = new GAction();
        cancelAction = new GAction();
        helpAction = new GAction();
    }

    public void registerPanel(WizardPanelDescriptor descriptor) {
        panelDescriptors.add(descriptor);
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
