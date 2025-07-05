package de.ganzer.swing.controls;

import de.ganzer.core.util.Strings;
import de.ganzer.swing.internals.SwingMessages;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * Defines a pane with tab headers that contain an optional close button.
 * <p>
 * By default, all tabs are closable. To prevent a tab from closing invoke
 * {@link #setClosableAt(int, boolean)} with the second argument set to
 * {@code false}.
 * <p>
 * If no tab should generally be closable, construct an instance by using on
 * of the regarding constructors.
 *
 * @see TabCloseListener
 */
@SuppressWarnings("unused")
public class ClosableTabsPane extends JTabbedPane {
    private final boolean closable;

    /**
     * Creates an empty <code>TabbedPane</code> with a default
     * tab placement of <code>JTabbedPane.TOP</code> where all tabs
     * are generally closable.
     *
     * @see #addTab
     */
    public ClosableTabsPane() {
        this.closable = true;
    }

    /**
     * Creates an empty <code>TabbedPane</code> with a default
     * tab placement of <code>JTabbedPane.TOP</code>.
     *
     * @param closable Sets whether th tabs are generally closable. If this is
     *        {@code false}, invoking {@link #setClosableAt} has no effect.
     *
     * @see #addTab
     */
    public ClosableTabsPane(boolean closable) {
        this.closable = closable;
    }

    /**
     * Creates an empty <code>TabbedPane</code> where all tabs are generally
     * closable with the specified tab placement of either:
     * <code>JTabbedPane.TOP</code>, <code>JTabbedPane.BOTTOM</code>,
     * <code>JTabbedPane.LEFT</code>, or <code>JTabbedPane.RIGHT</code>.
     *
     * @param tabPlacement the placement for the tabs relative to the content
     *
     * @see #addTab
     */
    public ClosableTabsPane(int tabPlacement) {
        super(tabPlacement);
        this.closable = true;
    }

    /**
     * Creates an empty <code>TabbedPane</code> with the specified tab placement
     * of either: <code>JTabbedPane.TOP</code>, <code>JTabbedPane.BOTTOM</code>,
     * <code>JTabbedPane.LEFT</code>, or <code>JTabbedPane.RIGHT</code>.
     *
     * @param tabPlacement the placement for the tabs relative to the content
     * @param closable Sets whether th tabs are generally closable. If this is
     *        {@code false}, invoking {@link #setClosableAt} has no effect.
     *
     * @see #addTab
     */
    public ClosableTabsPane(int tabPlacement, boolean closable) {
        super(tabPlacement);
        this.closable = closable;
    }

    /**
     * Creates an empty <code>TabbedPane</code>  where all tabs are generally
     * closable with the specified tab placement and tab layout policy.
     * <p>
     * Tab placement may be either: <code>JTabbedPane.TOP</code>,
     * <code>JTabbedPane.BOTTOM</code>, <code>JTabbedPane.LEFT</code>, or
     * <code>JTabbedPane.RIGHT</code>. Tab layout policy may be either:
     * <code>JTabbedPane.WRAP_TAB_LAYOUT</code> or
     * <code>JTabbedPane.SCROLL_TAB_LAYOUT</code>.
     *
     * @param tabPlacement the placement for the tabs relative to the content
     * @param tabLayoutPolicy the policy for laying out tabs when all tabs will
     *        not fit on one run.
     *
     * @throws IllegalArgumentException if tab placement or tab layout policy are not
     *         one of the above supported values
     *
     * @see #addTab
     */
    public ClosableTabsPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
        this.closable = true;
    }

    /**
     * Creates an empty <code>TabbedPane</code> with the specified tab placement
     * and tab layout policy.  Tab placement may be either:
     * <code>JTabbedPane.TOP</code>, <code>JTabbedPane.BOTTOM</code>,
     * <code>JTabbedPane.LEFT</code>, or <code>JTabbedPane.RIGHT</code>.
     * Tab layout policy may be either: <code>JTabbedPane.WRAP_TAB_LAYOUT</code>
     * or <code>JTabbedPane.SCROLL_TAB_LAYOUT</code>.
     *
     * @param tabPlacement the placement for the tabs relative to the content
     * @param tabLayoutPolicy the policy for laying out tabs when all tabs will
     *        not fit on one run.
     * @param closable Sets whether th tabs are generally closable. If this is
     *        {@code false}, invoking {@link #setClosableAt} has no effect.
     *
     * @throws IllegalArgumentException if tab placement or tab layout policy are not
     *         one of the above supported values
     *
     * @see #addTab
     */
    public ClosableTabsPane(int tabPlacement, int tabLayoutPolicy, boolean closable) {
        super(tabPlacement, tabLayoutPolicy);
        this.closable = closable;
    }

    /**
     * Inserts a new tab for the given component, at the given index,
     * represented by the given title and/or icon, either of which may
     * be {@code null}.
     *
     * @param title the title to be displayed on the tab
     * @param icon the icon to be displayed on the tab
     * @param component the component to be displayed when this tab is clicked.
     * @param tip the tooltip to be displayed for this tab
     * @param index the position to insert this new tab
     *         ({@code > 0 and <= getTabCount()})
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     *         ({@code < 0 or > getTabCount()})
     *
     * @see #addTab
     * @see #removeTabAt
     */
    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
        setTabComponentAt(index, new TabHeaderPanel(this, component, title, icon));

        if (getTabCount() == 1)
            fireStateChanged();
    }

    /**
     * Sets the selected index for this tabbedpane. The index must be
     * a valid tab index or -1, which indicates that no tab should be selected
     * (can also be used when there are no tabs in the tabbedpane).  If a -1
     * value is specified when the tabbedpane contains one or more tabs, then
     * the results will be implementation defined.
     *
     * @param index the index to be selected
     *
     * @throws IndexOutOfBoundsException if index is out of range
     *         {@code (index < -1 || index >= tab count)}
     *
     * @see #getSelectedIndex
     * @see SingleSelectionModel#setSelectedIndex
     */
    @Override
    public void setSelectedIndex(int index) {
        var tab = getTabComponentAt(index);

        if (tab != null && tab.isEnabled())
            super.setSelectedIndex(index);
    }

    /**
     * Gets a value indicating whether the tabs in the pane are generally
     * closable or not.
     * <p>
     * If this returns {@code false}, invoking {@link #setClosableAt} has no
     * effect.
     *
     * @return {@code true} if the tabs are generally closable.
     */
    public boolean isClosable() {
        return closable;
    }

    /**
     * Gets a value indicating whether the tab at the specified index is
     * closable.
     *
     * @param index The tab index to query.
     *
     * @return {@code true} if the tab is closable.
     */
    public boolean isClosableAt(int index) {
        var tab = getTabComponentAt(index);
        return tab != null && ((TabHeaderPanel) tab).isClosable();
    }

    /**
     * Sets whether the tab at the specified index is closable.
     *
     * @param index The index of the tab.
     * @param closable {@link true} to make the tab closable.
     */
    public void setClosableAt(int index, boolean closable) {
        if (!this.closable)
            return;

        var tab = getTabComponentAt(index);

        if (tab != null)
            ((TabHeaderPanel)tab).setClosable(closable);
    }

    /**
     * Sets the title at <code>index</code> to <code>title</code> which
     * can be <code>null</code>.
     * The title is not shown if a tab component for this tab was specified.
     * An internal exception is raised if there is no tab at that index.
     *
     * @param index the tab index where the title should be set
     * @param title the title to be displayed in the tab
     *
     * @throws IndexOutOfBoundsException if index is out of range
     *         {@code (index < 0 || index >= tab count)}
     *
     * @see #getTitleAt
     * @see #setTabComponentAt
     */
    @Override
    public void setTitleAt(int index, String title) {
        var tab = getTabComponentAt(index);

        if (tab != null)
            ((TabHeaderPanel)tab).setTitle(title);

        super.setTitleAt(index, title);
    }

    /**
     * Sets the icon at <code>index</code> to <code>icon</code> which can be
     * <code>null</code>. This does not set disabled icon at <code>icon</code>.
     * If the new Icon is different than the current Icon and disabled icon
     * is not explicitly set, the LookAndFeel will be asked to generate a disabled
     * Icon. To explicitly set disabled icon, use <code>setDisableIconAt()</code>.
     * The icon is not shown if a tab component for this tab was specified.
     * An internal exception is raised if there is no tab at that index.
     *
     * @param index the tab index where the icon should be set
     * @param icon the icon to be displayed in the tab
     *
     * @throws IndexOutOfBoundsException if index is out of range
     *         {@code (index < 0 || index >= tab count)}
     *
     * @see #setDisabledIconAt
     * @see #getIconAt
     * @see #getDisabledIconAt
     * @see #setTabComponentAt
     */
    @Override
    public void setIconAt(int index, Icon icon) {
        var tab = getTabComponentAt(index);

        if (tab != null)
            ((TabHeaderPanel)tab).setIcon(icon);

        super.setIconAt(index, icon);
    }

    /**
     * Sets the disabled icon at <code>index</code> to <code>icon</code>
     * which can be <code>null</code>.
     * An internal exception is raised if there is no tab at that index.
     *
     * @param index the tab index where the disabled icon should be set
     * @param disabledIcon the icon to be displayed in the tab when disabled
     *
     * @throws IndexOutOfBoundsException if index is out of range
     *         {@code (index < 0 || index >= tab count)}
     *
     * @see #getDisabledIconAt
     */
    @Override
    public void setDisabledIconAt(int index, Icon disabledIcon) {
        var tab = getTabComponentAt(index);

        if (tab != null)
            ((TabHeaderPanel)tab).setDisabledIcon(disabledIcon);

        super.setDisabledIconAt(index, disabledIcon);
    }

    /**
     * Sets the tooltip text at <code>index</code> to <code>toolTipText</code>
     * which can be <code>null</code>.
     * An internal exception is raised if there is no tab at that index.
     *
     * @param index the tab index where the tooltip text should be set
     * @param toolTipText the tooltip text to be displayed for the tab
     *
     * @throws IndexOutOfBoundsException if index is out of range
     *         {@code (index < 0 || index >= tab count)}
     *
     * @see #getToolTipTextAt
     */
    @Override
    public void setToolTipTextAt(int index, String toolTipText) {
        var tab = getTabComponentAt(index);

        if (tab != null)
            ((TabHeaderPanel)tab).setToolTipText(toolTipText);

        super.setToolTipTextAt(index, toolTipText);
    }

    /**
     * Sets the background color at <code>index</code> to
     * <code>background</code>
     * which can be <code>null</code>, in which case the tab's background color
     * will default to the background color of the <code>tabbedpane</code>.
     * An internal exception is raised if there is no tab at that index.
     * <p>
     * It is up to the look and feel to honor this property, some may
     * choose to ignore it.
     *
     * @param index the tab index where the background should be set
     * @param background the color to be displayed in the tab's background
     *
     * @throws IndexOutOfBoundsException if index is out of range
     *         {@code (index < 0 || index >= tab count)}
     *
     * @see #getBackgroundAt
     */
    @Override
    public void setBackgroundAt(int index, Color background) {
        var tab = getTabComponentAt(index);

        if (tab != null)
            tab.setBackground(background);

        super.setBackgroundAt(index, background);
    }

    /**
     * Sets the foreground color at <code>index</code> to
     * <code>foreground</code> which can be
     * <code>null</code>, in which case the tab's foreground color
     * will default to the foreground color of this <code>tabbedpane</code>.
     * An internal exception is raised if there is no tab at that index.
     * <p>
     * It is up to the look and feel to honor this property, some may
     * choose to ignore it.
     *
     * @param index the tab index where the foreground should be set
     * @param foreground the color to be displayed as the tab's foreground
     *
     * @throws IndexOutOfBoundsException if index is out of range
     *         {@code (index < 0 || index >= tab count)}
     *
     * @see #getForegroundAt
     */
    @Override
    public void setForegroundAt(int index, Color foreground) {
        var tab = getTabComponentAt(index);

        if (tab != null)
            tab.setForeground(foreground);

        super.setForegroundAt(index, foreground);
    }

    /**
     * Sets whether or not the tab at <code>index</code> is enabled.
     * An internal exception is raised if there is no tab at that index.
     *
     * @param index the tab index which should be enabled/disabled
     * @param enabled whether or not the tab should be enabled
     *
     * @throws IndexOutOfBoundsException if index is out of range
     *         {@code (index < 0 || index >= tab count)}
     *
     * @see #isEnabledAt
     */
    @Override
    public void setEnabledAt(int index, boolean enabled) {
        var tab = getTabComponentAt(index);

        if (tab != null)
            tab.setEnabled(enabled);

        super.setEnabledAt(index, enabled);
    }

    /**
     * Provides a hint to the look and feel as to which character in the
     * text should be decorated to represent the mnemonic. Not all look and
     * feels may support this. A value of -1 indicates either there is
     * no mnemonic for this tab, or you do not wish the mnemonic to be
     * displayed for this tab.
     * <p>
     * The value of this is updated as the properties relating to the
     * mnemonic change (such as the mnemonic itself, the text...).
     * You should only ever have to call this if
     * you do not wish the default character to be underlined. For example, if
     * the text at tab index 3 was 'Apple Price', with a mnemonic of 'p',
     * and you wanted the 'P'
     * to be decorated, as 'Apple <u>P</u>rice', you would have to invoke
     * <code>setDisplayedMnemonicIndex(3, 6)</code> after invoking
     * <code>setMnemonicAt(3, KeyEvent.VK_P)</code>.
     * <p>Note that it is the programmer's responsibility to ensure
     * that each tab has a unique mnemonic or unpredictable results may
     * occur.
     *
     * @param tabIndex the index of the tab that the mnemonic refers to
     * @param mnemonicIndex index into the <code>String</code> to underline
     *
     * @throws IndexOutOfBoundsException if <code>tabIndex</code> is
     *         out of range ({@code tabIndex < 0 || tabIndex >= tab
     *         count})
     * @throws IllegalArgumentException will be thrown if
     *         <code>mnemonicIndex</code> is &gt;= length of the tab
     *         title , or &lt; -1
     *
     * @see #setMnemonicAt(int, int)
     * @see #getDisplayedMnemonicIndexAt(int)
     */
    @Override
    public void setDisplayedMnemonicIndexAt(int tabIndex, int mnemonicIndex) {
        var tab = getTabComponentAt(tabIndex);

        if (tab != null)
            ((TabHeaderPanel)tab).setDisplayedMnemonicIndex(mnemonicIndex);

        super.setDisplayedMnemonicIndexAt(tabIndex, mnemonicIndex);
    }

    /**
     * Sets the keyboard mnemonic for accessing the specified tab.
     * The mnemonic is the key which when combined with the look and feel's
     * mouseless modifier (usually Alt) will activate the specified
     * tab.
     * <p>
     * A mnemonic must correspond to a single key on the keyboard
     * and should be specified using one of the <code>VK_XXX</code>
     * keycodes defined in <code>java.awt.event.KeyEvent</code>
     * or one of the extended keycodes obtained through
     * <code>java.awt.event.KeyEvent.getExtendedKeyCodeForChar</code>.
     * Mnemonics are case-insensitive, therefore a key event
     * with the corresponding keycode would cause the button to be
     * activated whether or not the Shift modifier was pressed.
     * <p>
     * This will update the displayed mnemonic property for the specified
     * tab.
     *
     * @param tabIndex the index of the tab that the mnemonic refers to
     * @param mnemonic the key code which represents the mnemonic
     *
     * @throws IndexOutOfBoundsException if <code>tabIndex</code> is out
     *         of range ({@code tabIndex < 0 || tabIndex >= tab count})
     *
     * @see #getMnemonicAt(int)
     * @see #setDisplayedMnemonicIndexAt(int, int)
     */
    @Override
    public void setMnemonicAt(int tabIndex, int mnemonic) {
        var tab = getTabComponentAt(tabIndex);

        if (tab != null)
            ((TabHeaderPanel)tab).setMnemonic(mnemonic);

        super.setMnemonicAt(tabIndex, mnemonic);
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
    protected void fireCloseTabEvent(int tabIndex) {
        var closeListeners = getListeners(TabCloseListener.class);

        for (var closeListener : closeListeners) {
            closeListener.closeTabPerformed(tabIndex, getComponentAt(tabIndex));
        }
    }

    private static class TabHeaderPanel extends JPanel {
        private final ClosableTabsPane pane;
        private final Component component;

        private JLabel titleLabel;
        private JLabel iconLabel;
        private TabButton closeButton;
        private Icon icon;
        private Icon disabledIcon;
        private int mnemonic;
        private int displayedMnemonicIndex = -1;

        public TabHeaderPanel(ClosableTabsPane pane, Component component, String title, Icon icon) {
            super(new BorderLayout(5, 0));

            Objects.requireNonNull(pane, "pane must not be null");
            Objects.requireNonNull(component, "component must not be null");

            this.pane = pane;
            this.component = component;

            setOpaque(false);
            setTitle(title);
            setIcon(icon);
            setClosable(pane.isClosable());

//            setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

            this.pane.addChangeListener(e -> adjustColors());

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isEnabled())
                        pane.setSelectedComponent(component);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (isEnabled())
                        titleLabel.setForeground(UIManager.getColor("TabbedPane.hoverForeground"));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (isEnabled())
                        adjustColors();
                }
            });
        }

        @Override
        public Color getForeground() {
            Color color = super.getForeground();
            return color != null ? color : UIManager.getColor("TabbedPane.foreground");
        }

        @Override
        public void setForeground(Color fg) {
            super.setForeground(fg);

            if (pane != null)
                adjustColors();
        }

        @Override
        public void setFont(Font font) {
            super.setFont(font);

            if (titleLabel != null)
                titleLabel.setFont(font);
        }

        @Override
        public void setEnabled(boolean enabled) {
            if (isEnabled() == enabled)
                return;

            super.setEnabled(enabled);
            adjustColors();

            if (titleLabel != null)
                titleLabel.setEnabled(enabled);

            if (iconLabel != null) {
                if (disabledIcon != null)
                    iconLabel.setIcon(enabled ? icon : disabledIcon);
                else
                    iconLabel.setEnabled(enabled);
            }

            if (closeButton != null)
                closeButton.setVisible(enabled);
        }

        public int getDisplayedMnemonicIndex() {
            return displayedMnemonicIndex;
        }

        public void setDisplayedMnemonicIndex(int displayedMnemonicIndex) {
            this.displayedMnemonicIndex = displayedMnemonicIndex;

            if (titleLabel != null)
                titleLabel.setDisplayedMnemonicIndex(displayedMnemonicIndex);
        }

        public int getMnemonic() {
            return mnemonic;
        }

        public void setMnemonic(int mnemonic) {
            this.mnemonic = mnemonic;

            if (titleLabel != null)
                titleLabel.setDisplayedMnemonic(mnemonic);
        }

        public boolean isClosable() {
            return closeButton != null;
        }

        public void setClosable(boolean closable) {
            if (isClosable() == closable)
                return;

            if (closable) {
                closeButton = new TabHeaderPanel.TabButton();
                add(closeButton, BorderLayout.EAST);
            } else {
                remove(closeButton);
                closeButton = null;
            }
        }

        public String getTitle() {
            return titleLabel != null ? titleLabel.getText() : null;
        }

        public void setTitle(String title) {
            if (Strings.isNullOrEmpty(title)) {
                if (titleLabel != null) {
                    remove(titleLabel);
                    titleLabel = null;
                }
            } else if (titleLabel == null) {
                    titleLabel = new JLabel(title);
                    titleLabel.setOpaque(false);
                    titleLabel.setFont(pane.getFont());

                    if (displayedMnemonicIndex >= 0)
                        titleLabel.setDisplayedMnemonicIndex(displayedMnemonicIndex);

                    if (mnemonic != 0)
                        titleLabel.setDisplayedMnemonic(mnemonic);

                    add(titleLabel, BorderLayout.CENTER);
            } else {
                titleLabel.setText(title);
            }
        }

        public Icon getIcon() {
            return icon;
        }

        public void setIcon(Icon icon) {
            this.icon = icon;

            if (icon == null) {
                if (iconLabel != null) {
                    remove(iconLabel);
                    iconLabel = null;
                }
            } else if (iconLabel == null) {
                iconLabel = new JLabel(!isEnabled() && disabledIcon != null ? disabledIcon : icon);
                iconLabel.setOpaque(false);

                add(iconLabel, BorderLayout.WEST);
            } else {
                iconLabel.setIcon(!isEnabled() && disabledIcon != null ? disabledIcon : icon);
            }
        }

        public Icon getDisabledIcon() {
            return disabledIcon;
        }

        public void setDisabledIcon(Icon disabledIcon) {
            this.disabledIcon = disabledIcon;

            if (iconLabel != null)
                iconLabel.setIcon(!isEnabled() && disabledIcon != null ? disabledIcon : icon);
        }

        private void adjustColors() {
            if (!isEnabled()) {
                if (titleLabel != null)
                    titleLabel.setForeground(UIManager.getColor("TabbedPane.disabledForeground"));
            } else {
                if (pane.getSelectedComponent() == component) {
                    if (titleLabel != null)
                        titleLabel.setForeground(UIManager.getColor("TabbedPane.selectedForeground"));
                } else {
                    if (titleLabel != null)
                        titleLabel.setForeground(getForeground());
                }
            }
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

            public void updateUI() {
                // Don't update UI for this button.
            }

            public void actionPerformed(ActionEvent e) {
                int i = pane.indexOfTabComponent(TabHeaderPanel.this);

                if (i != -1)
                    pane.fireCloseTabEvent(i);
            }

            protected void paintComponent(Graphics g) {
                //noinspection DuplicatedCode - TabbedClosablePane
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                if (getModel().isPressed())
                    g2.translate(1, 1);

                g2.setStroke(new BasicStroke(2));

                setVisible(isEnabled());

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
