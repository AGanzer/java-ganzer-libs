package de.ganzer.swing.internals;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class SwingMessages {
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("de.ganzer.swing.messages");
    }

    public static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("de.ganzer.swing.messages", locale);
    }

    public static String get(String key) {
        return getInstance().getMessage(key);
    }

    public static String get(String key, Object... args) {
        return String.format(getInstance().getMessage(key), args);
    }

    private static SwingMessages instance;
    private final Locale locale;
    private final ResourceBundle bundle;

    private SwingMessages(Locale locale) {
        this.locale = locale;
        this.bundle = getBundle(locale);
    }

    private String getMessage(String id) {
        return bundle.getString(id);
    }

    private static SwingMessages getInstance() {
        Locale current = Locale.getDefault();

        if (instance == null || !Objects.equals(current, instance.locale))
            instance = new SwingMessages(current);

        return instance;
    }
}
