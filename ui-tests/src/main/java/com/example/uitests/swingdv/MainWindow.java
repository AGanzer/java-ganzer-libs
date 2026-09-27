package com.example.uitests.swingdv;

import de.ganzer.dv.DVManager;
import de.ganzer.swing.controls.ClosableTabsPane;
import org.jdesktop.swingx.JXFrame;

import javax.swing.JMenuBar;
import java.awt.event.WindowEvent;

public class MainWindow extends JXFrame {
    private ClosableTabsPane tabPane;

    public MainWindow() {
        super(SwingDVApp.TITLE, true);

        initMenuBar();
        initTabPane();
        setSize(800, 600);
        setLocationRelativeTo(null);
        SwingDVApp.uiSettings.apply(getClass().getSimpleName(), this);
    }

    public ClosableTabsPane getTabPane() {
        return tabPane;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (!DVManager.canClose())
                return;

            SwingDVApp.saveSettings();
        }

        super.processWindowEvent(e);
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        Actions.allActions.addMenus(menuBar);
    }

    private void initTabPane() {
        tabPane = new ClosableTabsPane();
        getContentPane().add(tabPane);
    }
}
