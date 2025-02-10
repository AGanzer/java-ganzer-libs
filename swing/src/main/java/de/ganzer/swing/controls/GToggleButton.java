package de.ganzer.swing.controls;

import de.ganzer.swing.actions.GAction;

import javax.swing.*;

/**
 * This is created by a {@link GAction} when a button shall be created.
 * <p>
 * The only difference to {@link JToggleButton} is, that {@code GToggleButton}
 * recognizes the visibility of an action, hides the image if this should not
 * be visible and uses the small image only if required.
 */
@SuppressWarnings("unused")
public class GToggleButton extends JToggleButton {
    private boolean hideImage;
    private boolean useSmallImage;

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
    public GToggleButton(Action a, boolean hideImage, boolean useSmallImage) {
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
