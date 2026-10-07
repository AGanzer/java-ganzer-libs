package com.example.uitests.swingdv;

import com.example.uitests.swing.SVGProvider;
import com.example.uitests.swingdv.doc.db.PersonDocument;
import com.example.uitests.swingdv.doc.db.PersonView;
import com.example.uitests.swingdv.doc.image.ImageDocument;
import com.example.uitests.swingdv.doc.image.ImageView;
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
import de.ganzer.dv.DVLoadException;
import de.ganzer.dv.DocumentTemplate;
import de.ganzer.dv.ViewTemplate;
import de.ganzer.dv.services.DVNavigationService;
import de.ganzer.swing.dlgfw.AbstractPanel;
import de.ganzer.swing.dlgfw.services.DFWNavigationService;
import de.ganzer.swing.dv.DVManager;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;

public class SwingDVApp {
    public static final String NAME = "SwingDV";
    public static final String TITLE = "Swing Document View";
    public static final String VERSION = "1.0.0";

    private static MainWindow mainWindow;
    private static DocumentTemplate<WelcomeDocument> welcomeTpl;

    public static void main(String[] args) {
        if (OS.isMac()) {
            System.setProperty("apple.awt.application.name", TITLE);
            System.setProperty("apple.laf.useScreenMenuBar", "true");

            Taskbar.getTaskbar().setIconImage(SVGProvider.get("hamburger", 64).getImage());
        }

        CommandLineParser.parse(args);

        Toolkit.getDefaultToolkit()
                .getSystemEventQueue()
                .push(new ExceptionHandlingEventQueue());

        setupLaF();
        registerServices();
        registerTemplates();
        DVManager.restoreRecentFiles(LocalSettings.user);
        
        SwingUtilities.invokeLater(() -> {
            Desktop desktop = Desktop.getDesktop();

            if (desktop.isSupported(Desktop.Action.APP_ABOUT))
                desktop.setAboutHandler(e -> NavigationService.getInstance().showAboutInfo());

            if (desktop.isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
                desktop.setQuitHandler((quitEvent, quitResponse) -> {
                    if (DVManager.canClose())
                        quitResponse.performQuit();
                    else
                        quitResponse.cancelQuit();
                });
            }

            if (desktop.isSupported(Desktop.Action.APP_OPEN_FILE)) {
                desktop.setOpenFileHandler(e -> {
                    try {
                        DVManager.openDocuments(
                                null,
                                e.getFiles().stream().map(File::getAbsolutePath).toList(),
                                false);
                    } catch (DVLoadException ex) {
                        NavigationService.getInstance().showError(ex.getLocalizedMessage(), ex);
                    }
                });
            }

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

    private static void setupLaF() {
        if (!CommandLineParser.isLafSet()) {
            if (OS.isMac())
                FlatMacLightLaf.setup();
            else
                FlatIntelliJLaf.setup();

            try {
                String value = LocalSettings.ui.read("lookAndFeel", "");

                if (!value.isEmpty())
                    UIManager.setLookAndFeel(value);
            } catch (Exception ex) {
                System.err.println("Failed to initialize LaF");
                ex.printStackTrace(System.err);
            }
        }

        UIManager.put("TitlePane.menuBarEmbedded", false);
        UIManager.put("Table.intercellSpacing", new Dimension(1, 1));

        Color panelBackgroundColor = UIManager.getColor("Panel.background");
        Color titleForegroundColor = Colors.getContrastingColor(panelBackgroundColor);
        Color titleBackgroundColor = Colors.getContrastingColor(titleForegroundColor);

        AbstractPanel.setTitleBackground(titleBackgroundColor);
        AbstractPanel.setTitleForeground(titleForegroundColor);

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
                "txt",
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
                "csv",
                DocumentTemplate.NONE);
        csvTpl.registerViewTemplate(new ViewTemplate<CSVDocument, CSVView<CSVDocument>>(
                "CSV",
                i -> new CSVView<>(i, mainWindow.getTabPane()),
                v -> mainWindow.addChildView(v),
                ViewTemplate.IS_DEFAULT));

        var imageTpl = new DocumentTemplate<>(
                "Image Files",
                s -> s.toLowerCase().endsWith(".bmp") || s.toLowerCase().endsWith(".gif") || s.toLowerCase().endsWith(".jpg")
                        || s.toLowerCase().endsWith(".jpeg") || s.toLowerCase().endsWith(".png") || s.toLowerCase().endsWith(".tiff"),
                ImageDocument::new,
                "",
                "Image Files|*.bmp *.gif *.jpg *.jpeg *.png *.tiff",
                null,
                DocumentTemplate.IS_HIDDEN);
        imageTpl.registerViewTemplate(new ViewTemplate<ImageDocument, ImageView>(
                "Image",
                i -> new ImageView(i, mainWindow.getTabPane()),
                v -> mainWindow.addChildView(v),
                ViewTemplate.IS_DEFAULT));

        var personTpl = new DocumentTemplate<>(
                "Person Files",
                s -> s.toLowerCase().endsWith(".person"),
                PersonDocument::new,
                "New Person Table",
                "Person Files|*.person",
                "person",
                DocumentTemplate.NONE);
        personTpl.registerViewTemplate(new ViewTemplate<PersonDocument, PersonView>(
                "Person Table",
                i -> new PersonView(i, mainWindow.getTabPane()),
                v -> mainWindow.addChildView(v),
                ViewTemplate.IS_DEFAULT));
        personTpl.registerViewTemplate(new ViewTemplate<PersonDocument, CSVView<PersonDocument>>(
                "CSV",
                i -> new CSVView<>(i, mainWindow.getTabPane()),
                v -> mainWindow.addChildView(v),
                ViewTemplate.NONE));

        DVManager.registerDocumentTemplate(welcomeTpl);
        DVManager.registerDocumentTemplate(textTpl);
        DVManager.registerDocumentTemplate(csvTpl);
        DVManager.registerDocumentTemplate(imageTpl);
        DVManager.registerDocumentTemplate(personTpl);
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
