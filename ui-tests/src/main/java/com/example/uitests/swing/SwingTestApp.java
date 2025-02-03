package com.example.uitests.swing;

import de.ganzer.swing.util.UISettings;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SwingTestApp {
    private static final UISettings uiSettings = new UISettings("ui-tests", "0.0.1");

    public static UISettings getUiSettings() {
        return uiSettings;
    }

    public static void main(String[] args) {
        uiSettings.load();

        var frame = new MainFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                uiSettings.save();
            }
        });

        frame.setVisible(true);
    }
}
