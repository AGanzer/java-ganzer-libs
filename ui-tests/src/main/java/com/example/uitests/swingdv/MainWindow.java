package com.example.uitests.swingdv;

import com.example.uitests.swingdv.doc.thumbnail.ThumbnailPanel;
import com.example.uitests.swingdv.doc.thumbnail.ThumbnailView;
import de.ganzer.dv.View;
import de.ganzer.swing.controls.ClosableTabsPane;
import de.ganzer.swing.dv.ClosableTabsPaneDVMSupport;
import de.ganzer.swing.dv.DVManager;
import org.jdesktop.swingx.JXFrame;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.WindowEvent;

public class MainWindow extends JXFrame {
    private ClosableTabsPane tabPane;
    private ThumbnailPanel thumbnails;

    public MainWindow() {
        super(SwingDVApp.TITLE, true);

        setSize(800, 600);
        setLocationRelativeTo(null);

        LocalSettings.ui.apply(getClass().getSimpleName(), this);

        initMenuBar();
        initToolBar();
        initSplitPane();
    }

    public ClosableTabsPane getTabPane() {
        return tabPane;
    }

    public void addChildView(View<?> view) {
        tabPane.addTab(view.getDocument().getName(), (Component) view);
        tabPane.setClosableAt(tabPane.getTabCount() - 1, view.getTemplate().isClosable());
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
    }

    public void addThumbnailView(ThumbnailView<?> view) {
        thumbnails.addThumbnail(view);
    }

    public ThumbnailPanel getThumbnails() {
        return thumbnails;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (!DVManager.canClose())
                return;

            LocalSettings.ui.write(getClass().getSimpleName(), this);
            DVManager.saveRecentFiles(LocalSettings.user);

            LocalSettings.save();
        }

        super.processWindowEvent(e);
    }

    private void initSplitPane() {
        tabPane = new ClosableTabsPane();
        DVManager.registerSupport(new ClosableTabsPaneDVMSupport(tabPane, getWindowMenu(), getRecentFilesMenu()));

        thumbnails = new ThumbnailPanel();
        var scroller = new JScrollPane(thumbnails, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabPane, scroller);
        splitPane.setDividerLocation(LocalSettings.ui.read(getClass().getSimpleName() + "splitter", getWidth() - 150));
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);
        splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, evt -> {
            LocalSettings.ui.write(getClass().getSimpleName() + "splitter", splitPane.getDividerLocation());
            thumbnails.setWidth(scroller.getViewport().getWidth());
        });

        getContentPane().add(splitPane);
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

    private JMenu getWindowMenu() {
        for (int i = 0; i < getJMenuBar().getMenuCount(); i++) {
            var menu = getJMenuBar().getMenu(i);

            if (menu.getAction() == Actions.windowActions)
                return menu;
        }

        return null;
    }

    private JMenu getRecentFilesMenu() {
        for (int i = 0; i < getJMenuBar().getMenuCount(); i++) {
            var menu = getJMenuBar().getMenu(i);

            if (menu.getAction() == DVManager.recentFilesActions)
                return menu;
        }

        return null;
    }
}
