package com.example.uitests.swingdv;

import com.example.uitests.swing.SVGProvider;
import com.example.uitests.swingdv.doc.text.CSVDocument;
import com.example.uitests.swingdv.doc.text.CSVView;
import com.example.uitests.swingdv.doc.text.TextDocument;
import com.example.uitests.swingdv.doc.text.TextView;
import com.example.uitests.swingdv.doc.welcome.WelcomeDocument;
import com.example.uitests.swingdv.doc.welcome.WelcomeView;
import com.example.uitests.swingdv.services.NavigationService;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import de.ganzer.core.OS;
import de.ganzer.core.Services;
import de.ganzer.dv.swing.DVManager;
import de.ganzer.dv.DocumentTemplate;
import de.ganzer.dv.ViewTemplate;
import de.ganzer.dv.services.DVNavigationService;
import de.ganzer.swing.dlgfw.services.DFWNavigationService;
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
    private static DocumentTemplate<WelcomeDocument> welcomeTpl;

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
        registerServices();
        registerTemplates();
        DVManager.registerSupport(new DVMSupport());

        SwingUtilities.invokeLater(() -> {
            mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            DVManager.createDocument(null, welcomeTpl);
        });
    }

    public static MainWindow getMainWindow() {
        return mainWindow;
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
        UIManager.put("Table.intercellSpacing", new Dimension(1, 1));

        FlatLaf.updateUI();
    }

    private static void registerServices() {
        Services.register(DVNavigationService.class, NavigationService.getInstance());
        Services.register(DFWNavigationService.class, NavigationService.getInstance());
    }

    private static void registerTemplates() {
        welcomeTpl = new DocumentTemplate<>(
                "Welcome",
                s -> false,
                WelcomeDocument::new,
                "Welcome",
                null,
                DocumentTemplate.NO_NEW_NUMBER | DocumentTemplate.IS_HIDDEN);
        welcomeTpl.registerViewTemplate(new ViewTemplate<WelcomeDocument, WelcomeView>(
                "Welcome",
                i -> new WelcomeView(i, mainWindow.getTabPane()),
                v -> mainWindow.addChildView(v),
                ViewTemplate.IS_DEFAULT | ViewTemplate.NOT_CLOSABLE));

        var textTpl = new DocumentTemplate<>(
                "Text Files",
                s -> s.toLowerCase().endsWith(".txt") || s.endsWith(".log") || s.endsWith(".c") || s.endsWith(".cpp")
                        || s.endsWith(".h") || s.endsWith(".java") || s.endsWith(".py"),
                TextDocument::new,
                "New Text",
                "Text Files|*.txt;Log Files|*.log;Source Files|*.c *.cpp *.h *.java *.py",
                DocumentTemplate.IS_DEFAULT);
        textTpl.registerViewTemplate(new ViewTemplate<TextDocument, TextView>(
                "Text",
                i -> new TextView(i, mainWindow.getTabPane()),
                v -> mainWindow.addChildView(v),
                ViewTemplate.IS_DEFAULT));

        var csvTpl = new DocumentTemplate<>(
                "CSV Files",
                s -> s.toLowerCase().endsWith(".csv"),
                CSVDocument::new,
                "New CSV Table",
                "CSV Files|*.csv",
                DocumentTemplate.NONE);
        csvTpl.registerViewTemplate(new ViewTemplate<CSVDocument, CSVView>(
                "CSV",
                i -> new CSVView(i, mainWindow.getTabPane()),
                v -> mainWindow.addChildView(v),
                ViewTemplate.IS_DEFAULT));

        DVManager.registerDocumentTemplate(welcomeTpl);
        DVManager.registerDocumentTemplate(textTpl);
        DVManager.registerDocumentTemplate(csvTpl);
    }

    private static void onIdle() {
        DVManager.updateEditActions();
    }

    private static class ExceptionHandlingEventQueue extends EventQueue {
        @Override
        protected void dispatchEvent(AWTEvent event) {
            try {
                super.dispatchEvent(event);
            } catch (Throwable t) {
                handleException(t);
            }

            if (peekEvent() == null)
                onIdle();
        }

        private void handleException(Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
