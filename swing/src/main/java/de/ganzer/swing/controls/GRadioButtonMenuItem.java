package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.*;

/**
 * This is created by a {@link GAction} when a menu item shall be created.
 * <p>
 * The only difference to {@link JRadioButtonMenuItem} is, that
 * {@code GRadioButtonMenuItem} recognizes the visibility of an action.
 */
@SuppressWarnings("unused")
public class GRadioButtonMenuItem extends JRadioButtonMenuItem {
    /**
     * {@inheritDoc}
     */
    public GRadioButtonMenuItem() {
    }

    /**
     * {@inheritDoc}
     */
    public GRadioButtonMenuItem(Icon icon) {
        super(icon);
    }

    /**
     * {@inheritDoc}
     */
    public GRadioButtonMenuItem(String text) {
        super(text);
    }

    /**
     * {@inheritDoc}
     */
    public GRadioButtonMenuItem(Action a) {
        super(a);
    }

    /**
     * {@inheritDoc}
     */
    public GRadioButtonMenuItem(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * {@inheritDoc}
     */
    public GRadioButtonMenuItem(String text, boolean selected) {
        super(text, selected);
    }

    /**
     * {@inheritDoc}
     */
    public GRadioButtonMenuItem(Icon icon, boolean selected) {
        super(icon, selected);
    }

    /**
     * {@inheritDoc}
     */
    public GRadioButtonMenuItem(String text, Icon icon, boolean selected) {
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
