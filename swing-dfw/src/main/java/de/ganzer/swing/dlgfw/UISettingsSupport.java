package de.ganzer.swing.dlgfw;

import de.ganzer.swing.util.UISettings;

/**
 * An interface that can be implemented by frames and dialogs that supports
 * restorable settings.
 */
public interface UISettingsSupport {
    /**
     * Called to restore the internal settings of the implementer from
     * previously saved settings.
     *
     * @param settings The settings wehre to restore the internal settings from.
     */
    void restoreSettings(UISettings settings);

    /**
     * Called to write the internal settings of the implementor into the given
     * settings.
     *
     * @param settings The settings where to write the internal settings into.
     */
    void saveSettings(UISettings settings);
}
