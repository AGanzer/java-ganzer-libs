package de.ganzer.dv.internals;

import java.util.ResourceBundle;

public class DVMessages {
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("de.ganzer.dv.messages");
    }

    public static String get(String key) {
        return getInstance().getMessage(key);
    }

    public static String get(String key, Object... args) {
        return String.format(getInstance().getMessage(key), args);
    }

    private static DVMessages instance;
    private final ResourceBundle bundle;

    private DVMessages() {
        this.bundle = getBundle();
    }

    private String getMessage(String id) {
        return bundle.getString(id);
    }

    private static DVMessages getInstance() {
        if (instance == null)
            instance = new DVMessages();

        return instance;
    }
}
