package de.ganzer.swing.dv;

import de.ganzer.dv.Document;
import de.ganzer.dv.View;
import de.ganzer.swing.controls.ClosableTabsPane;
import de.ganzer.swing.controls.TabCloseListener;

import javax.swing.JMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * A default implementation for the {@link DVManager} that uses a
 * {@link ClosableTabsPane} to inform the {@link DVManager} about
 * changed views.
 *
 * @see DVManager#registerSupport(DVManagerSupport)
 *
 * @since 6.0.0
 */
public class ClosableTabsPaneDVMSupport extends BasicDVManagerSupport {
    private ClosableTabsPane tabPane;

    /**
     * Creates a new instance of {@link ClosableTabsPaneDVMSupport}.
     *
     * @param tabPane The tabbed pane to use or {@code null} to set the tabbed
     *         pane later.
     * @param windowMenu The window menu to manage or {@code null} to set it
     *        later.
     * @param recentFilesMenu The recent files menu to manage or {@code null}
     *        to set it later.
     *
     * @see #setTabPane(ClosableTabsPane)
     * @see #setWindowMenu(JMenu)
     * @see #setRecentFilesMenu(JMenu)
     * @see DVManager#registerSupport(DVManagerSupport)
     */
    public ClosableTabsPaneDVMSupport(ClosableTabsPane tabPane, JMenu windowMenu, JMenu recentFilesMenu) {
        super(windowMenu, recentFilesMenu);
        setTabPane(tabPane);
    }

    /**
     * Getter for the {@link ClosableTabsPane} that is used to hold the MDI
     * subviews of the application.
     *
     * @return The managed tabbed pane or {@code null} if there is no tabbed
     *         set.
     */
    public ClosableTabsPane getTabPane() {
        return tabPane;
    }

    /**
     * Sets the {@link ClosableTabsPane} that is used to hold the MDI subviews
     * of the application.
     *
     * @param tabPane The tabbed pane to set or {@code null} to set no pane.
     */
    public void setTabPane(ClosableTabsPane tabPane) {
        if (this.tabPane == tabPane)
            return;

        if (this.tabPane != null) {
            this.tabPane.removeCloseListener(tabCloseListener);
            this.tabPane.removeChangeListener(tabSelectionListener);
        }

        this.tabPane = tabPane;

        if (this.tabPane != null) {
            this.tabPane.addCloseListener(tabCloseListener);
            this.tabPane.addChangeListener(tabSelectionListener);
        }
    }

    /**
     * Invoked to get the active MDI subview.
     *
     * @return The active MDI subview or {@code null} if there is no active MDI
     *         subview.
     */
    @Override
    public View<? extends Document> getActiveMDISubView() {
        return tabPane.getSelectedComponent() instanceof View<?> v ? v : null;
    }

    /**
     * Invoked to get a list of all open MDI subviews.
     *
     * @return All open MDI subviews or an empty list if there are no open MDI
     *         subviews.
     */
    @Override
    public List<View<? extends Document>> getAllMDISubViews() {
        var views = new ArrayList<View<? extends Document>>();

        for (int i = 0; i < tabPane.getTabCount(); i++) {
            var component = tabPane.getComponentAt(i);

            if (component instanceof View<?> v)
                views.add(v);
        }

        return views;
    }

    private final TabCloseListener tabCloseListener = new TabCloseListener() {
        @Override
        public void closeTabPerformed(int index, Component component) {
            if (component instanceof View<?> v) {
                if (!v.getTemplate().isClosable() || !v.getDocument().canCloseView(v))
                    return;

                v.getDocument().removeView(v);
            }

            tabPane.removeTabAt(index);
        }
    };
    private final ChangeListener tabSelectionListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
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
        }
    };
}
