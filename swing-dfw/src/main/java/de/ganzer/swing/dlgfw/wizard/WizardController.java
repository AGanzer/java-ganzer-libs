package de.ganzer.swing.dlgfw.wizard;

import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.dlgfw.internals.SwingDialogsMessages;

import java.util.Stack;

class WizardController<Data> {
    private final Stack<WizardPage<Data>> prevPages = new Stack<>();
    private final WizardModel<Data> model;
    private final Data data;

    private final GAction nextAction ;
    private final GAction prevAction;
    private final GAction cancelAction;
    private final GAction helpAction;

    public WizardController(WizardModel<Data> model, Data data) {
        this.model = model;
        this.data = data;

        nextAction = new GAction(SwingDialogsMessages.get("wizard.button.next"))
                .onAction(e -> nextPage());
        prevAction = new GAction(SwingDialogsMessages.get("wizard.button.prev"))
                .onAction(e -> prevPage());
        cancelAction = new GAction(SwingDialogsMessages.get("wizard.button.cancel"));
        helpAction = new GAction(SwingDialogsMessages.get("wizard.button.help"));
    }

    private void nextPage() {
        if (model.getCurrentPage() == null)
            throw new IllegalStateException("No current page available");

        var currPage = model.getCurrentPage();

        if (!currPage.validateInput())
            return;

        currPage.updateData(data);

        var nextPage = currPage.getNextPage(data);

        if (!model.setCurrentPage(nextPage))
            return;

        nextPage.initControls(data);
        prevPages.push(currPage);

        prevAction.setEnabled(true);
        nextAction.setName(model.getCurrentPage().getNextPage(data) != null
                                   ? SwingDialogsMessages.get("wizard.button.next")
                                   : SwingDialogsMessages.get("wizard.button.finish"));
    }

    private void prevPage() {
        if (prevPages.isEmpty())
            return;

        if (!model.setCurrentPage(prevPages.peek()))
            return;

        prevPages.pop();
        prevAction.setEnabled(!prevPages.isEmpty());
        nextAction.setName(SwingDialogsMessages.get("wizard.button.next"));
    }

    public GAction getNextAction() {
        return nextAction;
    }

    public GAction getPrevAction() {
        return prevAction;
    }

    public GAction getCancelAction() {
        return cancelAction;
    }

    public GAction getHelpAction() {
        return helpAction;
    }
}
