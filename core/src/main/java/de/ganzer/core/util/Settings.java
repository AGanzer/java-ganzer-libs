package de.ganzer.core.util;

import java.util.Properties;

/**
 * Utility for reading and writing settings.
 * <p>
 * This implementation extends the {@link Properties} class with various methods
 * that make it easier to write, read and validate property values. To ensure
 * that all settings are written correctly as strings, use only the several
 * {@code write} methods or the inherited {@link #setProperty} method, because
 * the {@code set} method stores values as objects and not as string, so that
 * they cannot be restored on reading it rom a settings file.
 */
public class Settings extends Properties {
    /**
     * Gets the setting with the specified key.
     *
     * @param key The key of the setting to get.
     * @param defaultValue The value to return if {@code key} is not present.
     *
     * @return The set value or {@code defaultValue}.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public String read(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Gets the setting with the specified key.
     *
     * @param key The key of the setting to get.
     * @param defaultValue The value to return if {@code key} is not present.
     *
     * @return The set value or {@code defaultValue}.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public int read(String key, int defaultValue) {
        String value = getProperty(key);

        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Gets the setting with the specified key.
     *
     * @param key The key of the setting to get.
     * @param defaultValue The value to return if {@code key} is not present.
     *
     * @return The set value or {@code defaultValue}.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public long read(String key, long defaultValue) {
        String value = getProperty(key);

        try {
            return value != null ? Long.parseLong(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Gets the setting with the specified key.
     *
     * @param key The key of the setting to get.
     * @param defaultValue The value to return if {@code key} is not present.
     *
     * @return The set value or {@code defaultValue}.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public float read(String key, float defaultValue) {
        String value = getProperty(key);

        try {
            return value != null ? Float.parseFloat(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Gets the setting with the specified key.
     *
     * @param key The key of the setting to get.
     * @param defaultValue The value to return if {@code key} is not present.
     *
     * @return The set value or {@code defaultValue}.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public double read(String key, double defaultValue) {
        String value = getProperty(key);

        try {
            return value != null ? Double.parseDouble(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Gets the setting with the specified key.
     *
     * @param key The key of the setting to get.
     * @param defaultValue The value to return if {@code key} is not present.
     *
     * @return The set value or {@code defaultValue}.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public boolean read(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    /**
     * Gets the setting with the specified key.
     *
     * @param key The key of the setting to get.
     * @param defaultValue The value to return if {@code key} is not present.
     *
     * @return The set value or {@code defaultValue}.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public <T extends Enum<T>> T read(String key, T defaultValue) {
        String value = getProperty(key);

        try {
            return value != null ? Enum.valueOf(defaultValue.getDeclaringClass(), value) : defaultValue;
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    /**
     * Sets the value of the property with the specified key.
     *
     * @param key The key of the property to set.
     * @param value the value to set.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public void write (String key, String value) {
        setProperty(key, value);
    }

    /**
     * Sets the value of the property with the specified key.
     *
     * @param key The key of the property to set.
     * @param value the value to set.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public void write(String key, long value) {
        setProperty(key, Long.toString(value));
    }

    /**
     * Sets the value of the property with the specified key.
     *
     * @param key The key of the property to set.
     * @param value the value to set.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public void write(String key, double value) {
        setProperty(key, Double.toString(value));
    }

    /**
     * Sets the value of the property with the specified key.
     *
     * @param key The key of the property to set.
     * @param value the value to set.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public void write(String key, boolean value) {
        setProperty(key, Boolean.toString(value));
    }

    /**
     * Sets the value of the property with the specified key.
     *
     * @param key The key of the property to set.
     * @param value the value to set.
     *
     * @throws NullPointerException {@code key} returns {@code null}.
     */
    public <T extends Enum<T>> void write(String key, T value) {
        setProperty(key, value.name());
    }
}
