package de.ganzer.core.internals;

import java.util.ResourceBundle;

public class CoreMessages {
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("de.ganzer.core.messages");
    }

    public static String get(String key) {
        return getInstance().getMessage(key);
    }

    public static String get(String key, Object... args) {
        return String.format(getInstance().getMessage(key), args);
    }

    private static CoreMessages instance;
    private final ResourceBundle bundle;

    private CoreMessages() {
        this.bundle = getBundle();
    }

    private String getMessage(String id) {
        return bundle.getString(id);
    }

    private static CoreMessages getInstance() {
        if (instance == null)
            instance = new CoreMessages();

        return instance;
    }
}
