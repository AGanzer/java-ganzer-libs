package de.ganzer.swing.dlgfw.wizard;

import de.ganzer.swing.actions.GAction;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.beans.PropertyChangeListener;

/**
 * A pane that displays several pages like a wizard.
 *
 * @param <Data> The type of the data that is passed between the pages.
 */
public class WizardPane<Data> extends JPanel {
    private final WizardController<Data> controller;
    private final PropertyChangeListener pageChangedListener = e -> {};

    /**
     * Creates a new wizard pane with no data.
     */
    public WizardPane() {
        this(null, null);
    }

    /**
     * Creates a new wizard pane with the given data.
     *
     * @param data The data that is passed between the pages.
     */
    public WizardPane(Data data) {
        this(null, data);
    }

    /**
     * Creates a new wizard pane with the given model.
     *
     * @param model The model to set.
     */
    public WizardPane(WizardModel<Data> model) {
        this(model, null);
    }

    /**
     * Creates a new wizard pane with the given data and model.
     *
     * @param model The model to set.
     * @param data The data that is passed between the pages.
     */
    public WizardPane(WizardModel<Data> model, Data data) {
        super(new CardLayout());

        controller = new WizardController<>(model != null ? model : new DefaultWizardModel<>(), data);

        for (var page : getModel().getPages())
            add(page.getComponent(), page.getPageId().toString());

        getModel().addPageChangedListener(pageChangedListener);
    }

    /**
     * Gets the data that is passed between the pages.
     *
     * @return The data that is passed between the pages or {@code null} if no
     *          data is set.
     */
    public Data getData() {
        return controller.getData();
    }

    /**
     * Sets the data that is passed between the pages.
     *
     * @param data The data that is passed between the pages.
     */
    public void setData(Data data) {
        controller.setData(data);
    }

    /**
     * Gets the model that is used by this component.
     *
     * @return The model that is used by this component.
     */
    public WizardModel<Data> getModel() {
        return controller.getModel();
    }

    /**
     * Sets the model that is used by this component.
     *
     * @param model The model that is used by this component.
     */
    public void setModel(WizardModel<Data> model) {
        if (getModel() == model)
            return;

        for (var page : getModel().getPages())
            remove(page.getComponent());

        getModel().removePageChangedListener(pageChangedListener);
        controller.setModel(model);
        getModel().addPageChangedListener(pageChangedListener);

        for (var page : getModel().getPages())
            add(page.getComponent(), page.getPageId().toString());

        if (getModel().getCurrentPage() != null)
            ((CardLayout) getLayout()).show(this, getModel().getCurrentPage().getPageId().toString());
    }

    /**
     * Gets the action that is fired when the next button is pressed.
     * <p>
     * The action itself is handled and managed by this component itself. It
     * is provided to create a button or menu item.
     *
     * @return The action that is fired when the next button is pressed.
     */
    public GAction getNextAction() {
        return controller.getNextAction();
    }

    /**
     * Gets the action that is fired when the previous button is pressed.
     * <p>
     * The action itself is handled and managed by this component itself. It
     * is provided to create a button or menu item.
     *
     * @return The action that is fired when the previous button is pressed.
     */
    public GAction getPrevAction() {
        return controller.getPrevAction();
    }
}
