package de.ganzer.swing.dv;

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
    private JMenu windowMenu;
    private JMenu recentFilesMenu;
    private int numDefaultItems;

    /**
     * Creates a new instance of {@link ClosableTabsPaneDVMSupport}.
     *
     * @param windowMenu The window menu to manage or {@code null} to set it
     *        later.
     * @param recentFilesMenu The recent files menu to manage or {@code null}
     *        to set it later.
     *
     * @see #setWindowMenu(JMenu)
     * @see #setRecentFilesMenu(JMenu)
     * @see DVManager#registerSupport(DVManagerSupport)
     */
    public BasicDVManagerSupport(JMenu windowMenu, JMenu recentFilesMenu) {
        setWindowMenu(windowMenu);
        setRecentFilesMenu(recentFilesMenu);
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
     * Invoked to update the menu items in the "Window" menu with the currently
     * opened documents.
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
     * recently opened files.
     */
    @Override
    public void updateRecentFilesMenu() {
        if (recentFilesMenu == null)
            return;

        recentFilesMenu.removeAll();
        DVManager.recentFilesActions.forEach(action -> recentFilesMenu.add(action.createMenuItem()));
    }
}
