package de.ganzer.core;

import java.util.ResourceBundle;

public class CoreMessages {
    private static CoreMessages _instance;
    private final ResourceBundle _bundle;

    private CoreMessages() {
        _bundle = ResourceBundle.getBundle("de.ganzer.core.messages");
    }

    private String getMessage(String id) {
        return _bundle.getString(id);
    }

    public static String get(String id) {
        if (_instance == null)
            _instance = new CoreMessages();

        return _instance.getMessage(id);
    }
}
