package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.beans.PropertyChangeListener;

/**
 * A pane that displays several pages like a wizard.
 */
public class WizardPane extends JPanel {
    private final WizardController controller;
    private final PropertyChangeListener pageChangedListener = e -> {
        ((CardLayout) getLayout()).show(this, e.getNewValue().toString());
    };

    /**
     * Creates a new wizard pane with no data.
     */
    public WizardPane() {
        this(null);
    }

    /**
     * Creates a new wizard pane with the given data and model.
     *
     * @param model The model to set.
     */
    public WizardPane(WizardModel model) {
        super(new CardLayout());

        controller = new WizardController(model != null ? model : new DefaultWizardModel());

        model = controller.getModel();

        for (var page : model.getPages())
            add(model.getComponent(page), page.toString());

        if (model.getCurrentPage() != null)
            ((CardLayout) getLayout()).show(this, model.getCurrentPage().toString());

        model.addPageChangedListener(pageChangedListener);
    }

    /**
     * Gets the model that is used by this component.
     *
     * @return The model that is used by this component.
     */
    public WizardModel getModel() {
        return controller.getModel();
    }

    /**
     * Sets the model that is used by this component.
     *
     * @param model The model that is used by this component.
     */
    public void setModel(WizardModel model) {
        if (getModel() == model)
            return;

        for (var page : getModel().getPages())
            remove(model.getComponent(page));

        getModel().removePageChangedListener(pageChangedListener);
        controller.setModel(model != null ? model : new DefaultWizardModel());

        model = controller.getModel();
        model.addPageChangedListener(pageChangedListener);

        for (var page : model.getPages())
            add(model.getComponent(page), page.toString());

        if (model.getCurrentPage() != null)
            ((CardLayout) getLayout()).show(this, model.getCurrentPage().toString());
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
