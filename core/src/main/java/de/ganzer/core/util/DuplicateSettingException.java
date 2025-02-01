package de.ganzer.core.util;

import java.lang.RuntimeException;

/**
 * Thrown if multiple {@link UserSettings} instances with the same file name
 * are created.
 */
public class DuplicateSettingException extends RuntimeException {
    /**
     * Creates a new instance.
     *
     * @param name The file name of the duplicate settings.
     */
    public DuplicateSettingException(String name) {
        super(String.format("A setting with the file name \"%s\" does already exist."));
    }
}
