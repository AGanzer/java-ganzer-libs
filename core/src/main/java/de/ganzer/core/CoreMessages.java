package de.ganzer.core;

import java.util.ResourceBundle;

/**
 * Enables access to localized messages from the de.ganzer.core library.
 * <p>
 * This is an internally used class and should not be used by client code.
 */
public class CoreMessages {
    private static CoreMessages _instance;
    private final ResourceBundle _bundle;

    private CoreMessages() {
        _bundle = ResourceBundle.getBundle("de.ganzer.core.messages");
    }

    private String getMessage(String id) {
        return _bundle.getString(id);
    }

    /**
     * Gets a localized message from the de.ganzer.core library.
     *
     * @param id The ID of the message to get.
     * @return The localized message with the specified ID.
     */
    public static String get(String id) {
        if (_instance == null)
            _instance = new CoreMessages();

        return _instance.getMessage(id);
    }
}
