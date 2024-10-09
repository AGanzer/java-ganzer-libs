package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.*;

@SuppressWarnings("unused")
public class GCheckBoxMenuItem extends JCheckBoxMenuItem {
    /**
     * {@inheritDoc}
     */
    public GCheckBoxMenuItem() {
    }

    /**
     * {@inheritDoc}
     */
    public GCheckBoxMenuItem(Icon icon) {
        super(icon);
    }

    /**
     * {@inheritDoc}
     */
    public GCheckBoxMenuItem(String text) {
        super(text);
    }

    /**
     * {@inheritDoc}
     */
    public GCheckBoxMenuItem(Action a) {
        super(a);
    }

    /**
     * {@inheritDoc}
     */
    public GCheckBoxMenuItem(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * {@inheritDoc}
     */
    public GCheckBoxMenuItem(String text, boolean b) {
        super(text, b);
    }

    /**
     * {@inheritDoc}
     */
    public GCheckBoxMenuItem(String text, Icon icon, boolean b) {
        super(text, icon, b);
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
