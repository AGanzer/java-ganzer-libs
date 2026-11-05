package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.beans.PropertyChangeListener;

/**
 * A pane that displays several pages like a wizard.
 *
 * @since 6.3.0
 */
public class WizardPane extends JPanel {
    private final WizardController controller;
    private final PropertyChangeListener pageChangedListener;
    private final WizardPageAddedListener pageAddedListener;

    /**
     * Creates a new wizard pane with an instance of {@link DefaultWizardModel}.
     */
    public WizardPane() {
        this(null);
    }

    /**
     * Creates a new wizard pane with the given model.
     *
     * @param model The model to set or {@code null} to set an instance of
     *        {@link DefaultWizardModel}.
     */
    public WizardPane(WizardModel model) {
        super(new CardLayout());

        controller = new WizardController();
        pageChangedListener = e -> ((CardLayout) getLayout()).show(this, e.getNewValue().toString());
        pageAddedListener = e ->add(controller.getModel().getComponent(e.getPageId()), e.getPageId().toString());

        setModel(model);
    }

    /**
     * Gets the action that is fired when the "Next" button is pressed.
     * <p>
     * The action itself is handled and managed by this component itself. It
     * is provided to create a button or menu item.
     *
     * @return The action that is fired when the "Next" button is pressed.
     */
    public GAction getNextAction() {
        return controller.getNextAction();
    }

    /**
     * Gets the action that is fired when the "Previous" button is pressed.
     * <p>
     * The action itself is handled and managed by this component itself. It
     * is provided to create a button or menu item.
     *
     * @return The action that is fired when the "Previous" button is pressed.
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
     * @param model The model to set or {@code null} to set an instance of
     *        {@link DefaultWizardModel}.
     */
    public void setModel(WizardModel model) {
        var currentModel = getModel();

        if (currentModel == model)
            return;

        if (currentModel != null) {
            for (var page : currentModel.getPages())
                remove(currentModel.getComponent(page));

            currentModel.removePageChangedListener(pageChangedListener);
            currentModel.removePageAddedListener(pageAddedListener);
        }

        controller.setModel(model != null ? model : new DefaultWizardModel());

        currentModel = controller.getModel();
        currentModel.addPageChangedListener(pageChangedListener);
        currentModel.addPageAddedListener(pageAddedListener);

        for (var page : currentModel.getPages())
            add(currentModel.getComponent(page), page.toString());

        if (currentModel.getCurrentPage() != null)
            ((CardLayout) getLayout()).show(this, currentModel.getCurrentPage().toString());
    }
}
