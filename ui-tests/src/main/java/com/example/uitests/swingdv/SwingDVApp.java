package com.example.uitests.swingdv;

import com.example.uitests.swing.SVGProvider;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import de.ganzer.core.OS;
import de.ganzer.swing.util.UISettings;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class SwingDVApp {
    public static final String NAME = "SwingDV";
    public static final String TITLE = "Swing Document View";
    public static final String VERSION = "1.0.0";
    public static final UISettings uiSettings = new UISettings(NAME, null);

    private static MainWindow mainWindow;

    public static void main(String[] args) {
        if (OS.isMac()) {
            System.setProperty("apple.awt.application.name", TITLE);
            System.setProperty("apple.laf.useScreenMenuBar", "true");

            Taskbar.getTaskbar().setIconImage(SVGProvider.get("hamburger", 64).getImage());
        }

        Toolkit.getDefaultToolkit()
                .getSystemEventQueue()
                .push(new ExceptionHandlingEventQueue());

        loadSettings();
        setupLaF();

        SwingUtilities.invokeLater(() -> {
            mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }

    public static void exit() {
        mainWindow.dispatchEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSING));
    }

    public static void saveSettings() {
        try {
            SwingDVApp.uiSettings.write(mainWindow.getClass().getSimpleName(), mainWindow);
            SwingDVApp.uiSettings.save();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private static void loadSettings() {
        try {
            uiSettings.load();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private static void setupLaF() {
        if (OS.isMac())
            FlatMacLightLaf.setup();
        else
            FlatIntelliJLaf.setup();

        try {
            String value = uiSettings.read("lookAndFeel", "");

            if (!value.isEmpty())
                UIManager.setLookAndFeel(value);
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
            ex.printStackTrace(System.err);
        }

        UIManager.put("TitlePane.menuBarEmbedded", false);
        FlatLaf.updateUI();
    }

    private static class ExceptionHandlingEventQueue extends EventQueue {
        @Override
        protected void dispatchEvent(AWTEvent event) {
            try {
                super.dispatchEvent(event);
            } catch (Throwable t) {
                handleException(t);
            }
        }

        private void handleException(Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
