package de.ganzer.core.util;

/**
 * An interface that can be implemented by classes that supports restorable
 * settings.
 */
@SuppressWarnings("unused")
public interface SettingsSupport {
    /**
     * Called to restore the internal settings of the implementer from
     * previously saved settings.
     *
     * @param settings The settings wehre to restore the internal settings from.
     */
    void restoreSettings(Settings settings);

    /**
     * Called to write the internal settings of the implementor into the given
     * settings.
     *
     * @param settings The settings where to write the internal settings into.
     */
    void saveSettings(Settings settings);
}
