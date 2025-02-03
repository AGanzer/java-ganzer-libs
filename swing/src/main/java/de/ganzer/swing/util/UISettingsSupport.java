package de.ganzer.swing.util;

/**
 * An interface that can be implemented by frames and dialogs that supports
 * restorable UI settings.
 */
@SuppressWarnings("unused")
public interface UISettingsSupport {
    /**
     * Called to restore the internal settings of the implementer from
     * previously saved settings.
     *
     * @param settings The settings wehre to restore the internal settings from.
     */
    void restoreUISettings(UISettings settings);

    /**
     * Called to write the internal settings of the implementor into the given
     * settings.
     *
     * @param settings The settings where to write the internal settings into.
     */
    void saveUISettings(UISettings settings);
}
