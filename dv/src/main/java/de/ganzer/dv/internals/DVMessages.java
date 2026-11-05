package de.ganzer.dv.internals;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class DVMessages {
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("de.ganzer.dv.messages");
    }

    public static ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle("de.ganzer.dv.messages", locale);
    }

    public static String get(String key) {
        return getInstance().getMessage(key);
    }

    public static String get(String key, Object... args) {
        return String.format(getInstance().getMessage(key), args);
    }

    private static DVMessages instance;
    private final Locale locale;
    private final ResourceBundle bundle;

    private DVMessages(Locale locale) {
        this.locale = locale;
        this.bundle = getBundle(locale);
    }

    private String getMessage(String id) {
        return bundle.getString(id);
    }

    private static DVMessages getInstance() {
        Locale current = Locale.getDefault();

        if (instance == null || !Objects.equals(current, instance.locale))
            instance = new DVMessages(current);

        return instance;
    }
}
