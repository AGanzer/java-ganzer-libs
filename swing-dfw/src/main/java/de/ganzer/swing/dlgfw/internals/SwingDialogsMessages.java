package de.ganzer.swing.dlgfw.internals;

import java.util.ResourceBundle;

public class SwingDialogsMessages {
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("de.ganzer.swing.dlgfw");
    }

    public static String get(String key) {
        return getInstance().getMessage(key);
    }

    public static String get(String key, Object... args) {
        return String.format(getInstance().getMessage(key), args);
    }

    private static SwingDialogsMessages instance;
    private final ResourceBundle bundle;

    private SwingDialogsMessages() {
        this.bundle = getBundle();
    }

    private String getMessage(String id) {
        return bundle.getString(id);
    }

    private static SwingDialogsMessages getInstance() {
        if (instance == null)
            instance = new SwingDialogsMessages();

        return instance;
    }
}
