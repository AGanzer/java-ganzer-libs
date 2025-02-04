package com.example.uitests.swing;

import de.ganzer.swing.util.UISettings;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class SwingTestApp {
    private static final UISettings uiSettings = new UISettings("swing-ui-tests", "0.0.1");

    public static UISettings getUiSettings() {
        return uiSettings;
    }

    public static void main(String[] args) {
        try {
            uiSettings.load();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        var frame = new MainFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ignored) {
                try {
                    uiSettings.save();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        });

        frame.setVisible(true);
    }
}
