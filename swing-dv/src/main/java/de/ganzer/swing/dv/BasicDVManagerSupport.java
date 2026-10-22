package de.ganzer.swing.dv;

import javax.swing.JFrame;
import javax.swing.JMenu;

/**
 * Basic implementation of {@link DVManagerSupport} that takes care of the
 * Window menu and the Recent Files menu.
 *
 * @see DVManager#registerSupport(DVManagerSupport)
 *
 * @since 6.0.0
 */
public abstract class BasicDVManagerSupport implements DVManagerSupport {
    private JFrame mainWindow;
    private String appTitle;
    private JMenu windowMenu;
    private JMenu recentFilesMenu;
    private int numDefaultItems;

    /**
     * Creates a new instance of {@link ClosableTabsPaneDVMSupport}.
     *
     * @param mainWindow The main window to manage or {@code null} to set it
     *        later.
     * @param appTitle The application's title to display in the main window or
     *        {@code null} to unset the title.
     * @param windowMenu The window menu to manage or {@code null} to set it
     *        later.
     * @param recentFilesMenu The recent files menu to manage or {@code null}
     *        to set it later.
     *
     * @see #setWindowMenu(JMenu)
     * @see #setRecentFilesMenu(JMenu)
     * @see DVManager#registerSupport(DVManagerSupport)
     */
    public BasicDVManagerSupport(JFrame mainWindow, String appTitle, JMenu windowMenu, JMenu recentFilesMenu) {
        setMainWindow(mainWindow);
        setAppTitle(appTitle);
        setWindowMenu(windowMenu);
        setRecentFilesMenu(recentFilesMenu);
    }

    /**
     * Gets the application's main window that's title shall be updated.
     *
     * @return The main window or {@code null} if there is no main window set.
     */
    public JFrame getMainWindow() {
        return mainWindow;
    }

    /**
     * Sets the main window that's title shall be updated.
     *
     * @param mainWindow The main window or {@code null} to unset the main
     *        window.
     */
    public void setMainWindow(JFrame mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Gets the application's title to display in the main window.
     *
     * @return The application's title or {@code null} if there is no title set.
     */
    @Override
    public String getAppTitle() {
        return appTitle;
    }

    /**
     * Sets the application's title to display in the main window.
     *
     * @param appTitle The application's title or {@code null} to unset the
     *        title.
     */
    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    /**
     * Gets the window menu that holds the open documents menu items that
     * shall be managed by this support.
     *
     * @return The managed window menu or {@code null} if there is no menu set.
     */
    public JMenu getWindowMenu() {
        return windowMenu;
    }

    /**
     * Sets the window menu that holds the open documents menu items that
     * shall be managed by this support.
     *
     * @param windowMenu The window menu to manage or {@code null} if there is
     *        no menu to manage.
     */
    public void setWindowMenu(JMenu windowMenu) {
        this.windowMenu = windowMenu;

        if (windowMenu != null)
            numDefaultItems = windowMenu.getItemCount();
    }

    /**
     * Gets the recent files menu that holds the recently opened files menu
     * items that shall be managed by this support.
     *
     * @return The managed recent files menu or {@code null} if there is no
     *         menu set.
     */
    public JMenu getRecentFilesMenu() {
        return recentFilesMenu;
    }

    /**
     * Sets the recent files menu that holds the recently opened files menu
     * items that shall be managed by this support.
     *
     * @param recentFilesMenu The recent files menu to manage or {@code null}
     *        if there is no menu to manage.
     */
    public void setRecentFilesMenu(JMenu recentFilesMenu) {
        this.recentFilesMenu = recentFilesMenu;
    }

    /**
     * Invoked to set the title of the main window.
     *
     * @param tile The title of the main window.
     */
    @Override
    public void setMainWindowTitle(String tile) {
        if (mainWindow != null)
            mainWindow.setTitle(tile);
    }

    /**
     * Invoked to update the menu items in the "Window" menu with the currently
     * available actions in {@link DVManager#openDocumentsActions}.
     */
    @Override
    public void updateOpenDocumentsMenu() {
        if (windowMenu == null)
            return;

        for (int i = windowMenu.getItemCount(); i > numDefaultItems; i--)
            windowMenu.remove(i - 1);

        DVManager.openDocumentsActions.forEach(action -> windowMenu.add(action.createMenuItem()));

        if (windowMenu.getItemCount() > numDefaultItems)
            windowMenu.insertSeparator(numDefaultItems);
    }

    /**
     * Invoked to update the menu items in the recent files menu with the
     * currently available actions in {@link DVManager#recentFilesActions}.
     */
    @Override
    public void updateRecentFilesMenu() {
        if (recentFilesMenu == null)
            return;

        recentFilesMenu.removeAll();
        DVManager.recentFilesActions.forEach(action -> recentFilesMenu.add(action.createMenuItem()));
    }
}
