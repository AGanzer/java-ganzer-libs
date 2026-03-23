package com.example.uitests.swing;

import de.ganzer.swing.util.UISettings;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class SwingTestApp {
    private static final UISettings uiSettings = new UISettings("swing-ui-tests", "0.0.1");

    public static UISettings getUiSettings() {
        return uiSettings;
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> throwable.printStackTrace(System.err));

        CommandLineParser.parse(args);

        try {
            uiSettings.load();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        Toolkit.getDefaultToolkit()
                .getSystemEventQueue()
                .push(new ExceptionHandlingEventQueue());

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

    public static class ExceptionHandlingEventQueue extends EventQueue {
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
