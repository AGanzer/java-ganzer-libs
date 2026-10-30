package de.ganzer.swing.dv.internals;

import java.util.ResourceBundle;

public class SwingDVMessages {
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("de.ganzer.swing.dv.messages");
    }

    public static String get(String key) {
        return getInstance().getMessage(key);
    }

    public static String get(String key, Object... args) {
        return String.format(getInstance().getMessage(key), args);
    }

    private static SwingDVMessages instance;
    private final ResourceBundle bundle;

    private SwingDVMessages() {
        this.bundle = getBundle();
    }

    private String getMessage(String id) {
        return bundle.getString(id);
    }

    private static SwingDVMessages getInstance() {
        if (instance == null)
            instance = new SwingDVMessages();

        return instance;
    }
}
