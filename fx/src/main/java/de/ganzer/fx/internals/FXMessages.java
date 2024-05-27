package de.ganzer.fx.internals;

import java.util.ResourceBundle;

public class FXMessages {
    private static FXMessages _instance;
    private final ResourceBundle _bundle;

    private FXMessages() {
        _bundle = ResourceBundle.getBundle("de.ganzer.fx.messages");
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
            _instance = new FXMessages();

        return _instance.getMessage(id);
    }
}
