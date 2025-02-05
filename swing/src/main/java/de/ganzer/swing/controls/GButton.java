package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.*;

/**
 * This is created by a {@link GAction} when a button shall be created.
 * <p>
 * The only difference to {@link JButton} is, that {@code GButton} recognizes
 * the visibility of an action.
 */
@SuppressWarnings("unused")
public class GButton extends JButton {
    /**
     * {@inheritDoc}
     */
    public GButton() {
    }

    /**
     * {@inheritDoc}
     */
    public GButton(Icon icon) {
        super(icon);
    }

    /**
     * {@inheritDoc}
     */
    public GButton(String text) {
        super(text);
    }

    /**
     * {@inheritDoc}
     */
    public GButton(Action a) {
        super(a);
    }

    /**
     * {@inheritDoc}
     */
    public GButton(String text, Icon icon) {
        super(text, icon);
    }

    @Override
    protected void configurePropertiesFromAction(Action action) {
        super.configurePropertiesFromAction(action);
        configureVisibilityFromAction(action);
    }

    @Override
    protected void actionPropertyChanged(Action action, String propertyName) {
        if (GAction.VISIBILITY_KEY.equals(propertyName))
            configureVisibilityFromAction(action);
        else
            super.actionPropertyChanged(action, propertyName);
    }

    private void configureVisibilityFromAction(Action action) {
        if (action == null)
            return;

        Boolean visible = (Boolean) action.getValue(GAction.VISIBILITY_KEY);
        setVisible(visible == null || visible);
    }
}
