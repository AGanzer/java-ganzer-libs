package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.*;

/**
 * This is created by a {@link GAction} when a button shall be created.
 * <p>
 * The only difference to {@link JButton} is, that {@code GButton} recognizes
 * the visibility of an action, hides the image if this should not be visible
 * and uses the small image only if required.
 */
@SuppressWarnings("unused")
public class GButton extends JButton {
    private boolean hideImage;
    private boolean useSmallImage;

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
    public GButton(Action a, boolean hideImage, boolean useSmallImage) {
        super(a);
        this.hideImage = hideImage;
        this.useSmallImage = useSmallImage;

        if (a != null) {
            if (hideImage)
                setIcon(null);
            else if (useSmallImage)
                setIcon((Icon) a.getValue(Action.SMALL_ICON));
        }
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

        if (action != null) {
            if (hideImage)
                setIcon(null);
            else if (useSmallImage)
                setIcon((Icon) action.getValue(Action.SMALL_ICON));
        }
    }

    @Override
    protected void actionPropertyChanged(Action action, String propertyName) {
        if (GAction.VISIBILITY_KEY.equals(propertyName))
            configureVisibilityFromAction(action);
        else {
            super.actionPropertyChanged(action, propertyName);

            if (hideImage)
                setIcon(null);
            else if (useSmallImage)
                setIcon((Icon) action.getValue(Action.SMALL_ICON));
        }
    }

    private void configureVisibilityFromAction(Action action) {
        if (action == null)
            return;

        Boolean visible = (Boolean) action.getValue(GAction.VISIBILITY_KEY);
        setVisible(visible == null || visible);
    }
}
