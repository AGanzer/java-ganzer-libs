package de.ganzer.core.internals;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class CoreMessages {
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("de.ganzer.core.messages");
    }

    public static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("de.ganzer.core.messages", locale);
    }

    public static String get(String key) {
        return getInstance().getMessage(key);
    }

    public static String get(String key, Object... args) {
        return String.format(getInstance().getMessage(key), args);
    }

    private static CoreMessages instance;
    private final Locale locale;
    private final ResourceBundle bundle;

    private CoreMessages(Locale locale) {
        this.locale = locale;
        this.bundle = getBundle(locale);
    }

    private String getMessage(String id) {
        return bundle.getString(id);
    }

    private static CoreMessages getInstance() {
        Locale current = Locale.getDefault();

        if (instance == null || !Objects.equals(current, instance.locale))
            instance = new CoreMessages(current);

        return instance;
    }
}
