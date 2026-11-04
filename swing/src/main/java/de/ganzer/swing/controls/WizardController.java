package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.internals.SwingMessages;

import java.util.Objects;
import java.util.Stack;

public class WizardController {
    private final Stack<Object> prevPages = new Stack<>();
    private final GAction nextAction ;
    private final GAction prevAction;

    private WizardModel model;

    /**
     * Creates a new instance.
     *
     * @see #WizardController(WizardModel)
     * @see #setModel(WizardModel)
     */
    public WizardController() {
        this(null);
    }

    /**
     * Creates a new instance.
     *
     * @param model The model to set or {@code null} to set it later.
     *
     * @see #setModel(WizardModel)
     */
    public WizardController(WizardModel model) {
        this.model = model;

        nextAction = new GAction(SwingMessages.get("wizard.button.next"))
                .onAction(e -> nextPage());
        prevAction = new GAction(SwingMessages.get("wizard.button.prev"))
                .onAction(e -> prevPage());
    }

    /**
     * Gets the action that is fired when the next button is pressed.
     * <p>
     * The action itself is handled and managed by this controller itself. It
     * is provided to create a button or menu item.
     *
     * @return The action that is fired when the next button is pressed.
     */
    public GAction getNextAction() {
        return nextAction;
    }

    /**
     * Gets the action that is fired when the previous button is pressed.
     * <p>
     * The action itself is handled and managed by this controller itself. It
     * is provided to create a button or menu item.
     *
     * @return The action that is fired when the previous button is pressed.
     */
    public GAction getPrevAction() {
        return prevAction;
    }

    /**
     * Gets the model that is used by this component.
     *
     * @return The model that is used by this component.
     */
    public WizardModel getModel() {
        return model;
    }

    /**
     * Sets the model that is used by this component.
     * <p>
     * Changing the model does reset the actions and the current page.
     *
     * @param model The model that is used by this component.
     *
     * @throws NullPointerException if {@code model} is {@code null}.
     */
    public void setModel(WizardModel model) {
        Objects.requireNonNull(model, "model must not be null.");

        if (this.model == model)
            return;

        this.model = model;

        prevPages.clear();
        prevAction.setEnabled(false);
        nextAction.setName(SwingMessages.get("wizard.button.next"));
    }

    private void nextPage() {
        if (model == null)
            throw new IllegalStateException("there is no model set.");

        var currPage = model.getCurrentPage();

        var nextPage = model.getNextPage();

        if (!model.setCurrentPage(nextPage))
            return;

        prevPages.push(currPage);

        prevAction.setEnabled(true);
        nextAction.setName(model.getNextPage() != null
                                   ? SwingMessages.get("wizard.button.next")
                                   : SwingMessages.get("wizard.button.finish"));
    }

    private void prevPage() {
        if (model == null)
            throw new IllegalStateException("there is no model set.");

        if (prevPages.isEmpty())
            return;

        if (!model.setCurrentPage(prevPages.peek()))
            return;

        prevPages.pop();
        prevAction.setEnabled(!prevPages.isEmpty());
        nextAction.setName(SwingMessages.get("wizard.button.next"));
    }
}
