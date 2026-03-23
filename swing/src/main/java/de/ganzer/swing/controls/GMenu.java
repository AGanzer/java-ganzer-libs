package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.*;

/**
 * This is created by a {@link GAction} when a menu shall be created.
 * <p>
 * The only difference to {@link JMenu} is, that {@code GMenu} recognizes
 * the visibility of an action.
 */
@SuppressWarnings("unused")
public class GMenu extends JMenu {
    /**
     * {@inheritDoc}
     */
    public GMenu() {
    }

    /**
     * {@inheritDoc}
     */
    public GMenu(String s) {
        super(s);
    }

    /**
     * {@inheritDoc}
     */
    public GMenu(Action a) {
        super(a);
    }

    /**
     * {@inheritDoc}
     */
    public GMenu(String s, boolean b) {
        super(s, b);
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
