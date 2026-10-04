package de.ganzer.swing.dv;

import javax.swing.JMenu;

public abstract class BasicDVManagerSupport implements DVManagerSupport {
    private JMenu windowMenu;
    private int numDefaultItems;

    /**
     * Creates a new instance of {@link ClosableTabsPaneDVMSupport}.
     *
     * @param windowMenu The window menu to manage or {@code null} to set it
     *        later.
     */
    public BasicDVManagerSupport(JMenu windowMenu) {
        setWindowMenu(windowMenu);
    }

    /**
     * Getter for the window menu that holds the open documents menu items that
     * shall be managed by this support.
     *
     * @return The managed window menu or {@code null} if there is no menu set.
     */
    public JMenu getWindowMenu() {
        return windowMenu;
    }

    /**
     * Setter for the window menu that holds the open documents menu items that
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
}
