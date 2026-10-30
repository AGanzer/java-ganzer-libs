package com.example.uitests.swingdv;

import de.ganzer.core.util.UserSettings;
import de.ganzer.swing.util.UISettings;

import java.io.IOException;

public final class LocalSettings {
    public static final UserSettings user = new UserSettings(SwingDVApp.NAME, null, true);
    public static final UISettings ui = new UISettings(SwingDVApp.NAME, null, true);

    public static void save() {
        try {
            user.save();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        try {
            ui.save();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    static {
        try {
            user.load();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        try {
            ui.load();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
