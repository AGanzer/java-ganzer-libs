package de.ganzer.swing.dlgfw.wizard;

import de.ganzer.swing.actions.GAction;
import de.ganzer.swing.dlgfw.internals.SwingDialogsMessages;

import java.util.Objects;
import java.util.Stack;

/**
 * The controller that is used by {@link WizardPane}.
 * <p>
 * This controller manages the "Next" and "Previous" actions and the given
 * model. It can also be used to implement a custom wizard panel or dialog.
 *
 * @param <Data> The type of the data that is passed between the pages.
 */
public class WizardController<Data> {
    private final Stack<WizardPage<Data>> prevPages = new Stack<>();

    private WizardModel<Data> model;
    private Data data;

    private final GAction nextAction ;
    private final GAction prevAction;

    /**
     * Creates a new instance.
     *
     * @param model The model to set.
     * @param data The data to set or {@code null} to set the data later.
     *
     * @throws NullPointerException if {@code model} is {@code null}.
     *
     * @see #setData(Data)
     * @see #setModel(WizardModel)
     */
    public WizardController(WizardModel<Data> model, Data data) {
        Objects.requireNonNull(model, "model must not be null.");

        this.model = model;
        this.data = data;

        if (model.getCurrentPage() != null && data != null)
            model.getCurrentPage().initControls(data);

        nextAction = new GAction(SwingDialogsMessages.get("wizard.button.next"))
                .onAction(e -> nextPage());
        prevAction = new GAction(SwingDialogsMessages.get("wizard.button.prev"))
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
    public WizardModel<Data> getModel() {
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
    public void setModel(WizardModel<Data> model) {
        Objects.requireNonNull(model, "model must not be null.");

        if (this.model == model)
            return;

        this.model = model;

        if (model.getCurrentPage() != null && data != null)
            model.getCurrentPage().initControls(data);

        prevPages.clear();
        prevAction.setEnabled(false);
        nextAction.setName(SwingDialogsMessages.get("wizard.button.next"));
    }

    /**
     * Gets the data that is passed between the pages.
     *
     * @return The data that is passed between the pages or {@code null} if no
     *          data is set.
     */
    public Data getData() {
        return data;
    }

    /**
     * Sets the data that is passed between the pages.
     *
     * @param data The data that is passed between the pages.
     */
    public void setData(Data data) {
        if (this.data == data)
            return;

        this.data = data;

        if (model.getCurrentPage() != null && data != null)
            model.getCurrentPage().initControls(data);
    }

    private void nextPage() {
        var currPage = model.getCurrentPage();

        if (currPage != null) {
            if (!currPage.validateInput())
                return;

            currPage.updateData(data);
        }

        var nextPage = model.getNextPage(data);

        if (!model.setCurrentPage(nextPage))
            return;

        nextPage.initControls(data);
        prevPages.push(currPage);

        prevAction.setEnabled(true);
        nextAction.setName(model.getNextPage(data) != null
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
}
