package de.ganzer.fx.internals;

import java.util.ResourceBundle;

public class FXMessages {
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("de.ganzer.fx.messages");
    }

    public static String get(String key) {
        return getInstance().getMessage(key);
    }

    public static String get(String key, Object... args) {
        return String.format(getInstance().getMessage(key), args);
    }

    private static FXMessages instance;
    private final ResourceBundle bundle;

    private FXMessages() {
        this.bundle = getBundle();
    }

    private String getMessage(String id) {
        return bundle.getString(id);
    }

    private static FXMessages getInstance() {
        if (instance == null)
            instance = new FXMessages();

        return instance;
    }
}
