package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.*;

@SuppressWarnings("unused")
public class GToggleButton extends JToggleButton {
    /**
     * {@inheritDoc}
     */
    public GToggleButton() {
    }

    /**
     * {@inheritDoc}
     */
    public GToggleButton(Icon icon) {
        super(icon);
    }

    /**
     * {@inheritDoc}
     */
    public GToggleButton(Icon icon, boolean selected) {
        super(icon, selected);
    }

    /**
     * {@inheritDoc}
     */
    public GToggleButton(String text) {
        super(text);
    }

    /**
     * {@inheritDoc}
     */
    public GToggleButton(String text, boolean selected) {
        super(text, selected);
    }

    /**
     * {@inheritDoc}
     */
    public GToggleButton(Action a) {
        super(a);
    }

    /**
     * {@inheritDoc}
     */
    public GToggleButton(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * {@inheritDoc}
     */
    public GToggleButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
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
