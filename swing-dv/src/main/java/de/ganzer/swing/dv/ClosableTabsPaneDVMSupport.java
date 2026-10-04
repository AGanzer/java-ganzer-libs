package de.ganzer.swing.dv;

import de.ganzer.dv.View;
import de.ganzer.swing.controls.ClosableTabsPane;
import de.ganzer.swing.controls.TabCloseListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Component;

/**
 * A default singleton support implementation for the {@link DVManager} that
 * uses a {@link ClosableTabsPane} to inform the {@link DVManager} about
 * changed views.
 */
public class ClosableTabsPaneDVMSupport implements DVManagerSupport {
    /**
     * Gets the instance of the {@link ClosableTabsPaneDVMSupport}.
     *
     * @return The only instance of the {@link ClosableTabsPaneDVMSupport}.
     */
    public static ClosableTabsPaneDVMSupport getInstance() {
        if (instance == null)
            instance = new ClosableTabsPaneDVMSupport();

        return instance;
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

    private static ClosableTabsPaneDVMSupport instance;

    private final TabCloseListener tabCloseListener = new TabCloseListener() {
        @Override
        public void closeTabPerformed(int index, Component component) {
            if (component instanceof View<?> v) {
                if (!v.getDocument().canCloseView(v))
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

    private ClosableTabsPane tabPane;

    private ClosableTabsPaneDVMSupport() {
    }
}
