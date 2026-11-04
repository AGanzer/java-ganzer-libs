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
    private final PropertyChangeListener pageChangedListener =
            e -> ((CardLayout) getLayout()).show(this, e.getNewValue().toString());
    private final WizardPageAddedListener pageAddedListener =
            e ->add(e.getComponent(), e.getPageId().toString());

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

        controller = new WizardController();
        setModel(model);
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
        var currentModel = getModel();

        if (currentModel != null) {
            if (currentModel == model)
                return;

            for (var page : currentModel.getPages())
                remove(currentModel.getComponent(page));

            currentModel.removePageChangedListener(pageChangedListener);

            if (currentModel instanceof AbstractWizardModel am)
                am.removePageAddedListener(pageAddedListener);
        }

        controller.setModel(model != null ? model : new DefaultWizardModel());

        currentModel = controller.getModel();
        currentModel.addPageChangedListener(pageChangedListener);

        if (currentModel instanceof AbstractWizardModel am)
            am.addPageAddedListener(pageAddedListener);

        for (var page : currentModel.getPages())
            add(currentModel.getComponent(page), page.toString());

        if (currentModel.getCurrentPage() != null)
            ((CardLayout) getLayout()).show(this, currentModel.getCurrentPage().toString());
    }
}
