package de.ganzer.fx;

import java.util.ResourceBundle;

// TODO: This class is not used and should be removed.
public class FxMessages {
    private static FxMessages _instance;
    private final ResourceBundle _bundle;

    private FxMessages() {
        _bundle = ResourceBundle.getBundle("de.ganzer.fx.messages");
    }

    private String getMessage(String id) {
        return _bundle.getString(id);
    }

    public static String get(String id) {
        if (_instance == null)
            _instance = new FxMessages();

        return _instance.getMessage(id);
    }
}
