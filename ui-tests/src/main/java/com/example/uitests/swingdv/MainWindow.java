package com.example.uitests.swingdv;

import de.ganzer.dv.swing.DVManager;
import de.ganzer.dv.View;
import de.ganzer.swing.controls.ClosableTabsPane;
import org.jdesktop.swingx.JXFrame;

import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import java.awt.Component;
import java.awt.event.WindowEvent;

public class MainWindow extends JXFrame {
    private ClosableTabsPane tabPane;

    public MainWindow() {
        super(SwingDVApp.TITLE, true);

        initMenuBar();
        initToolBar();
        initTabPane();
        setSize(800, 600);
        setLocationRelativeTo(null);
        SwingDVApp.uiSettings.apply(getClass().getSimpleName(), this);
    }

    public ClosableTabsPane getTabPane() {
        return tabPane;
    }

    public void addChildView(View<?> view) {
        tabPane.addTab(view.getDocument().getName(), (Component) view);
        tabPane.setClosableAt(tabPane.getTabCount() - 1, view.getTemplate().isClosable());
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
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

    private void initToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        setToolBar(toolBar);

        toolBar.add(Actions.newAction.createButton());
        toolBar.add(Actions.openAction.createButton());
        toolBar.addSeparator();
        toolBar.add(DVManager.saveAction.createButton());
        toolBar.add(DVManager.saveAsAction.createButton());
        toolBar.add(DVManager.saveAllAction.createButton());
        toolBar.addSeparator();
        toolBar.add(DVManager.undoAction.createButton());
        toolBar.add(DVManager.redoAction.createButton());
        toolBar.addSeparator();
        toolBar.add(DVManager.cutAction.createButton());
        toolBar.add(DVManager.copyAction.createButton());
        toolBar.add(DVManager.pasteAction.createButton());
        toolBar.add(DVManager.deleteAction.createButton());
        toolBar.addSeparator();
        toolBar.add(DVManager.closeWindowAction.createButton());
        toolBar.add(DVManager.closeAllWindowsAction.createButton());
    }

    private void initTabPane() {
        tabPane = new ClosableTabsPane();

        tabPane.addCloseListener((i, c) -> {
            if (c instanceof View<?> v) {
                if (!v.getDocument().canCloseView(v))
                    return;

                v.getDocument().removeView(v);
            }

            tabPane.removeTabAt(i);
        });

        tabPane.addChangeListener(e -> {
            var enableAll = false;

            for (int i = 0; i < tabPane.getTabCount(); i++) {
                if (tabPane.isClosableAt(i)) {
                    enableAll = true;
                    break;
                }
            }

            DVManager.closeWindowAction.setEnabled(tabPane.getSelectedIndex() >= 0 && tabPane.isClosableAt(tabPane.getSelectedIndex()));
            DVManager.closeAllWindowsAction.setEnabled(enableAll);
            DVManager.setActiveView(tabPane.getSelectedComponent() instanceof View<?> v ? v : null);
        });

        getContentPane().add(tabPane);
    }
}
