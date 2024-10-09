package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.*;

@SuppressWarnings("unused")
public class GMenuItem extends JMenuItem {
    /**
     * {@inheritDoc}
     */
    public GMenuItem() {
    }

    /**
     * {@inheritDoc}
     */
    public GMenuItem(Icon icon) {
        super(icon);
    }

    /**
     * {@inheritDoc}
     */
    public GMenuItem(String text) {
        super(text);
    }

    /**
     * {@inheritDoc}
     */
    public GMenuItem(Action a) {
        super(a);
    }

    /**
     * {@inheritDoc}
     */
    public GMenuItem(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * {@inheritDoc}
     */
    public GMenuItem(String text, int mnemonic) {
        super(text, mnemonic);
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
