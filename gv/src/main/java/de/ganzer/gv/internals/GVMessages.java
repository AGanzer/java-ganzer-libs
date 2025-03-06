package de.ganzer.gv.internals;

import java.util.ResourceBundle;

public class GVMessages {
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("de.ganzer.gv.messages");
    }

    public static String get(String key) {
        return getInstance().getMessage(key);
    }

    public static String get(String key, Object... args) {
        return String.format(getInstance().getMessage(key), args);
    }

    private static GVMessages instance;
    private final ResourceBundle bundle;

    private GVMessages() {
        this.bundle = getBundle();
    }

    private String getMessage(String id) {
        return bundle.getString(id);
    }

    private static GVMessages getInstance() {
        if (instance == null)
            instance = new GVMessages();

        return instance;
    }
}
