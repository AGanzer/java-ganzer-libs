package de.ganzer.swing.internals;

import java.util.ResourceBundle;

public class SwingMessages {
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("de.ganzer.swing.messages");
    }

    public static String get(String key) {
        return getInstance().getMessage(key);
    }

    public static String get(String key, Object... args) {
        return String.format(getInstance().getMessage(key), args);
    }

    private static SwingMessages instance;
    private final ResourceBundle bundle;

    private SwingMessages() {
        this.bundle = getBundle();
    }

    private String getMessage(String id) {
        return bundle.getString(id);
    }

    private static SwingMessages getInstance() {
        if (instance == null)
            instance = new SwingMessages();

        return instance;
    }
}
