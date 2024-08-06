package de.ganzer.swing.internals;

import java.util.ResourceBundle;

public class SwingMessages {
    private static SwingMessages _instance;
    private final ResourceBundle _bundle;

    private SwingMessages() {
        _bundle = ResourceBundle.getBundle("de.ganzer.swing.messages");
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
            _instance = new SwingMessages();

        return _instance.getMessage(id);
    }
}
