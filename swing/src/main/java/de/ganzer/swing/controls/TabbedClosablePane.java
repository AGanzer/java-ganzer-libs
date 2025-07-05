package de.ganzer.swing.controls;

import de.ganzer.swing.internals.SwingMessages;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * Defines a pane with tab headers that contain a close button.
 *
 * @deprecated Use {@link ClosableTabsPane} instead.
 *
 * @see TabCloseListener
 */
@SuppressWarnings("unused")
@Deprecated(since = "5.2", forRemoval = true)
public class TabbedClosablePane extends JTabbedPane {
    private boolean closable = true;

    /**
     * {@inheritDoc}
     */
    public TabbedClosablePane() {
    }

    /**
     * {@inheritDoc}
     */
    public TabbedClosablePane(int tabPlacement) {
        super(tabPlacement);
    }

    /**
     * {@inheritDoc}
     */
    public TabbedClosablePane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
    }

    /**
     * Gets a value that indicates whether the tab headers of this pane contain
     * close buttons.
     *
     * @return {@code true} if the close buttons are visible.
     */
    public boolean isClosable() {
        return closable;
    }

    /**
     * Shows or hides the close buttons on the tab headers.
     *
     * @param closable {@code false} to hide the close buttons.
     */
    public void setClosable(boolean closable) {
        if (this.closable == closable)
            return;

        this.closable = closable;

        for (int i = 0; i < getTabCount(); i++)
            ((TabHeaderPanel)getTabComponentAt(i)).adjustClosable();
    }

    /**
     * Adds a close listener that is called when a close button of one of the
     * tab headers is clicked.
     *
     * @param l The listener to install.
     */
    public synchronized void addCloseListener(TabCloseListener l) {
        listenerList.add(TabCloseListener.class, l);
    }

    /**
     * Removes a previously installed listener.
     *
     * @param l The listener to remove.
     */
    public synchronized void removeCloseListener(TabCloseListener l) {
        listenerList.remove(TabCloseListener.class, l);
    }

    /**
     * Fires the event that indicates that the close button of a tab header is
     * clicked.
     *
     * @param tabIndex The zero-based index of the header.
     */
    public void fireCloseTabEvent(int tabIndex) {
        var closeListeners = getListeners(TabCloseListener.class);

        for (var closeListener : closeListeners) {
            closeListener.closeTabPerformed(tabIndex, getComponentAt(tabIndex));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIconAt(int index, Icon icon) {
        super.setIconAt(index, icon);
        ((TabHeaderPanel)getTabComponentAt(index)).setIcon(icon);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTitleAt(int index, String title) {
        super.setTitleAt(index, title);
        ((TabHeaderPanel)getTabComponentAt(index)).setTitle(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
        setTabComponentAt(index, new TabHeaderPanel(this, component, title, icon));

        if (getTabCount() == 1)
            fireStateChanged();
    }

    private static class TabHeaderPanel extends JPanel implements ChangeListener {
        private final TabbedClosablePane pane;
        private final Component component;
        private final JLabel label;
        private JLabel icon;
        private TabButton button;

        public TabHeaderPanel(TabbedClosablePane pane, Component component, String title, Icon icon) {
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));

            Objects.requireNonNull(pane, "pane must not be null");
            Objects.requireNonNull(component, "component must not be null");

            this.pane = pane;
            this.component = component;
            this.pane.addChangeListener(this);

            setOpaque(false);
            setIcon(icon);

            label = new JLabel(title);
            label.setForeground(UIManager.getColor("TabbedPane.foreground"));
            add(label);

            adjustClosable();
            setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    pane.setSelectedComponent(component);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    label.setForeground(UIManager.getColor("TabbedPane.hoverForeground"));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    label.setForeground(UIManager.getColor("TabbedPane.foreground"));
                }
            });
        }

        public void setTitle(String title) {
            label.setText(title);
        }

        public void setIcon(Icon icon) {
            if (icon != null) {
                if (this.icon != null)
                    this.icon.setIcon(icon);
                else {
                    if (label != null)
                        remove(label);

                    if (button != null)
                        remove(button);

                    this.icon = new JLabel(icon);
                    this.icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
                    add(this.icon);

                    if (label != null)
                        add(label);

                    if (button != null)
                        add(button);
                }
            } else if (this.icon != null) {
                remove(this.icon);
                this.icon = null;
            }
        }

        public void adjustClosable() {
            adjustLabelBorder();

            if (pane.closable) {
                button = new TabButton();
                add(button);
            } else if (button != null) {
                remove(button);
                button = null;
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (pane.getSelectedComponent() == component) {
                label.setForeground(UIManager.getColor("TabbedPane.selectedForeground"));
            } else {
                label.setForeground(UIManager.getColor("TabbedPane.foreground"));
            }
        }

        private void adjustLabelBorder() {
            if (pane.closable)
                label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            else
                label.setBorder(null);
        }

        private class TabButton extends JButton implements ActionListener {
            public TabButton() {
                int size = 17;

                setPreferredSize(new Dimension(size, size));
                setToolTipText(SwingMessages.get(("closableTabbedPane.action.close")));
                setUI(new BasicButtonUI());
                setContentAreaFilled(false);
                setFocusable(false);
                setBorder(BorderFactory.createEtchedBorder());
                setBorderPainted(false);
                setRolloverEnabled(true);

                addActionListener(this);
            }

            //we don't want to update UI for this button
            public void updateUI() {
            }

            public void actionPerformed(ActionEvent e) {
                int i = pane.indexOfTabComponent(TabHeaderPanel.this);

                if (i != -1)
                    pane.fireCloseTabEvent(i);
            }

            protected void paintComponent(Graphics g) {
                //noinspection DuplicatedCode - ClosableTabsPane
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                if (getModel().isPressed())
                    g2.translate(1, 1);

                g2.setStroke(new BasicStroke(2));

                if (getModel().isRollover())
                    g2.setColor(Color.MAGENTA);
                else if (pane.getSelectedComponent() == component)
                    g2.setColor(UIManager.getColor("TabbedPane.selectedForeground"));
                else
                    g2.setColor(UIManager.getColor("TabbedPane.foreground"));

                int delta = 6;

                g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
                g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);

                g2.dispose();
            }
        }
    }
}
