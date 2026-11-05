package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.internals.SwingMessages;

import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.Stack;

/**
 * The controller used by {@link WizardPane}.
 * <p>
 * This controller manages the navigation between pages of a wizard and can also
 * be used in custom wizard implementations.
 *
 * @since 6.3.0
 */
public class WizardController {
    /**
     * The "Next" and "Finish" actions are both fired by the action gotten by
     * {@link #getNextAction()}. This is the action's command if the next page
     * shall be shown. This is handled by the controller itself.
     */
    public static final String NEXT_COMMAND = "next";
    /**
     * The "Next" and "Finish" actions are both fired by the action gotten by
     * {@link #getNextAction()}. This is the action's command if the last page
     * is shown and die wizard shall be closed. This should be handled by the
     * controller itself.
     */
    public static final String FINISH_COMMAND = "finish";

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
                .command(NEXT_COMMAND)
                .onAction(this::nextPage);
        prevAction = new GAction(SwingMessages.get("wizard.button.prev"))
                .onAction(e -> prevPage());
    }

    /**
     * Gets the action that is fired when the "Next" button is pressed.
     * <p>
     * The action itself is handled and managed by this controller itself. It
     * is provided to create a button or menu item.
     *
     * @return The action that is fired when the "Next" button is pressed.
     */
    public GAction getNextAction() {
        return nextAction;
    }

    /**
     * Gets the action that is fired when the "Previous" button is pressed.
     * <p>
     * The action itself is handled and managed by this controller itself. It
     * is provided to create a button or menu item.
     *
     * @return The action that is fired when the "Previous" button is pressed.
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

    private void nextPage(ActionEvent e) {
        if (model == null || model.getCurrentPage() == null)
            throw new IllegalStateException("there is no model set or the model has no current page.");

        if (FINISH_COMMAND.equals(e.getActionCommand()))
            return;

        var currPage = model.getCurrentPage();

        if (!model.validatePage(currPage))
            return;

        model.pageLeaving(currPage);

        var nextPage = model.getNextPage(currPage);

        if (nextPage == null) {
            nextAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, FINISH_COMMAND));
            return;
        }

        if (!model.setCurrentPage(nextPage))
            return;

        prevPages.push(currPage);
        prevAction.setEnabled(true);

        model.pageEntered(currPage);

        if (model.getNextPage(currPage) != null)
            nextAction.setName(SwingMessages.get("wizard.button.next"));
        else
            nextAction.setName(SwingMessages.get("wizard.button.finish"));
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
