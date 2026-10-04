package de.ganzer.swing.dv;

import de.ganzer.dv.Document;
import de.ganzer.dv.View;
import de.ganzer.swing.controls.ClosableTabsPane;
import de.ganzer.swing.controls.TabCloseListener;

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
 * @since 6.0.0
 */
public class ClosableTabsPaneDVMSupport implements DVManagerSupport {
    private ClosableTabsPane tabPane;

    /**
     * Creates a new instance of {@link ClosableTabsPaneDVMSupport}.
     *
     * @param tabPane The tabbed pane to use or {@code null} to set the tabbed
     *         pane later.
     *
     * @see #setTabPane(ClosableTabsPane)
     * @see DVManager#registerSupport(DVManagerSupport)
     */
    public ClosableTabsPaneDVMSupport(ClosableTabsPane tabPane) {
        setTabPane(tabPane);
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
