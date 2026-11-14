package de.ganzer.swing.controls;

import de.ganzer.swing.internals.Images;
import de.ganzer.swing.internals.SwingMessages;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A custom toolbar component that provides an overflow menu for items that do
 * not fit on the toolbar.
 * <p>
 * Items are kept whole and overflow at the right or bottom edge. The popup
 * temporarily hosts the original components, preserving their state and
 * listeners. Closing it restores their toolbar order. Create and modify
 * this component on the event dispatch thread.
 * <p>
 * Button actions complete before the overflow closes. If an action opens
 * another popup, the overflow remains available until that popup closes.
 *
 * @since 6.6.0
 */
public class GToolBar extends JToolBar {
    private final JButton overflowButton;
    private final JPopupMenu overflowPopup = new JPopupMenu() {
        @Override
        public void menuSelectionChanged(boolean included) {
            if (included) {
                super.menuSelectionChanged(true);
            } else {
                // Swing clears the old path before selecting a child popup.
                SwingUtilities.invokeLater(() -> {
                    if (!hasSelectedOverflowPopup()) {
                        super.menuSelectionChanged(false);
                    }
                });
            }
        }
    };
    private final List<Component> overflowItems = new ArrayList<>();
    private final List<PopupItem> popupItems = new ArrayList<>();
    private final ActionListener overflowActionListener = event -> SwingUtilities.invokeLater(this::finishOverflowAction);

    private boolean movingItems;
    private Dimension popupToolbarSize;
    private int popupOrientation;

    private record PopupItem(Component component, int index) {
    }

    /**
     * Creates a new toolbar; orientation defaults to {@link #HORIZONTAL}.
     */
    public GToolBar() {
        this(null, HORIZONTAL);
    }

    /**
     * Creates a new toolbar with the specified {@code orientation}.
     * <p>
     * The {@code orientation} must be either {@link #HORIZONTAL}
     * or {@link #VERTICAL}.
     *
     * @param orientation The initial orientation. It must be either
     *         {@link #HORIZONTAL} or {@link #VERTICAL}.
     *
     * @throws IllegalArgumentException if orientation is neither
     *         {@link #HORIZONTAL} nor {@link #VERTICAL}
     */
    public GToolBar(int orientation) {
        this(null, orientation);
    }

    /**
     * Creates a new toolbar with the specified {@code name}.
     * <p>
     * The name is used as the title of the undocked toolbar. The default
     * orientation is {@link #HORIZONTAL}.
     *
     * @param name The name of the toolbar.
     */
    public GToolBar(String name) {
        this(name, HORIZONTAL);
    }

    /**
     * Creates a new toolbar with a specified {@code name} and
     * {@code orientation}.
     * <p>
     * All other constructors call this constructor. If {@code orientation} is
     * an invalid value, an exception will be thrown.
     *
     * @param name The name of the toolbar.
     * @param orientation The initial orientation. It must be either
     *         {@link #HORIZONTAL} or {@link #VERTICAL}.
     *
     * @throws IllegalArgumentException if orientation is neither
     *         {@link #HORIZONTAL} nor {@link #VERTICAL}
     */
    public GToolBar(String name, int orientation) {
        super(name, orientation);

        overflowButton = new JButton(Images.load("toggle-arrow-closed-16"));
        overflowButton.setMargin(new Insets(2, 2, 2, 2));
        overflowButton.setToolTipText(SwingMessages.get("toolBar.overflow"));
        overflowButton.setVisible(false);
        overflowButton.addActionListener(event -> showOverflow());

        super.addImpl(overflowButton, null, -1);

        overflowPopup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
                restoreItems();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent event) {
                restoreItems();
            }
        });
    }

    /**
     * Returns the size needed to display all visible items without overflow.
     *
     * @return The preferred toolbar size.
     */
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet())
            return super.getPreferredSize();

        Insets insets = getInsets();
        int length = 0;
        int breadth = 0;

        for (Component component : getItems()) {
            if (component.isVisible()) {
                Dimension size = component.getPreferredSize();
                length += getLength(size);
                breadth = Math.max(breadth, getBreadth(size));
            }
        }

        return getOrientation() == HORIZONTAL
                ? new Dimension(length + insets.left + insets.right, breadth + insets.top + insets.bottom)
                : new Dimension(breadth + insets.left + insets.right, length + insets.top + insets.bottom);
    }

    /**
     * Returns the size needed for the overflow button alone.
     *
     * @return The minimum toolbar size.
     */
    @Override
    public Dimension getMinimumSize() {
        if (overflowButton == null || isMinimumSizeSet())
            return super.getMinimumSize();

        Insets insets = getInsets();
        Dimension size = overflowButton.getPreferredSize();

        return new Dimension(size.width + insets.left + insets.right, size.height + insets.top + insets.bottom);
    }

    /**
     * Lays out whole items and reserves space for the overflow button when
     * the visible items exceed the available length.
     */
    @Override
    public void doLayout() {
        if (overflowPopup.isVisible() && getSize().equals(popupToolbarSize) && getOrientation() == popupOrientation)
            return;

        closeOverflow();
        Insets insets = getInsets();
        boolean horizontal = getOrientation() == HORIZONTAL;
        int available = Math.max(0, horizontal ? getWidth() - insets.left - insets.right : getHeight() - insets.top - insets.bottom);
        int breadth = Math.max(0, horizontal ? getHeight() - insets.top - insets.bottom : getWidth() - insets.left - insets.right);
        List<Component> items = getItems();
        int total = 0;

        for (Component component : items) {
            if (component.isVisible()) {
                total += getLength(component.getPreferredSize());
            }
        }

        boolean overflow = total > available;
        int buttonLength = overflow ? Math.min(available, getLength(overflowButton.getPreferredSize())) : 0;
        int limit = available - buttonLength;
        int position = 0;
        boolean full = false;

        overflowItems.clear();

        for (Component component : items) {
            if (!component.isVisible())
                continue;

            Dimension size = component.getPreferredSize();
            int length = getLength(size);
            full = full || position + length > limit;

            if (full) {
                component.setBounds(0, 0, 0, 0);
                overflowItems.add(component);
            } else {
                int itemBreadth = Math.min(breadth, getBreadth(size));
                setItemBounds(component, insets, position, (breadth - itemBreadth) / 2, length, itemBreadth);
                position += length;
            }
        }

        overflowButton.setVisible(overflow);
        setItemBounds(overflowButton, insets, limit, 0, buttonLength, breadth);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addImpl(Component component, Object constraints, int index) {
        if (!movingItems)
            closeOverflow();

        if (index < 0 && overflowButton != null && overflowButton.getParent() == this)
            index = getComponentZOrder(overflowButton);

        super.addImpl(component, constraints, index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(int index) {
        if (!movingItems)
            closeOverflow();

        super.remove(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Component component) {
        if (!movingItems)
            closeOverflow();

        super.remove(component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll() {
        closeOverflow();
        super.removeAll();

        if (overflowButton != null)
            super.addImpl(overflowButton, null, -1);
    }

    private List<Component> getItems() {
        List<Component> items = new ArrayList<>(List.of(getComponents()));

        for (PopupItem item : popupItems)
            items.add(Math.min(item.index(), items.size()), item.component());

        items.remove(overflowButton);
        return items;
    }

    private int getLength(Dimension size) {
        return getOrientation() == HORIZONTAL ? size.width : size.height;
    }

    private int getBreadth(Dimension size) {
        return getOrientation() == HORIZONTAL ? size.height : size.width;
    }

    private void setItemBounds(Component component, Insets insets, int position, int offset, int length, int breadth) {
        if (getOrientation() == HORIZONTAL)
            component.setBounds(insets.left + position, insets.top + offset, length, breadth);
        else
            component.setBounds(insets.left + offset, insets.top + position, breadth, length);
    }

    private void showOverflow() {
        if (overflowPopup.isVisible()) {
            closeOverflow();
            return;
        }

        doLayout();

        if (overflowItems.isEmpty())
            return;

        List<Component> items = List.of(getComponents());
        popupToolbarSize = getSize();
        popupOrientation = getOrientation();
        movingItems = true;

        try {
            for (Component component : overflowItems) {
                popupItems.add(new PopupItem(component, items.indexOf(component)));
                overflowPopup.add(component);

                if (component instanceof AbstractButton button) {
                    button.addActionListener(overflowActionListener);
                }
            }

            int x = getOrientation() == HORIZONTAL ? 0 : overflowButton.getWidth();
            int y = getOrientation() == HORIZONTAL ? overflowButton.getHeight() : 0;

            overflowPopup.show(overflowButton, x, y);
        } finally {
            movingItems = false;

            if (!overflowPopup.isVisible())
                restoreItems();
        }
    }

    private boolean hasSelectedOverflowPopup() {
        for (MenuElement element : MenuSelectionManager.defaultManager().getSelectedPath()) {
            if (element == overflowPopup || element instanceof JPopupMenu popup && isOverflowChild(popup)) {
                return true;
            }
        }

        return false;
    }

    private boolean isOverflowChild(JPopupMenu popup) {
        Component invoker = popup.getInvoker();

        while (invoker != null) {
            if (SwingUtilities.isDescendingFrom(invoker, overflowPopup)) {
                return true;
            }

            JPopupMenu parent = (JPopupMenu) SwingUtilities.getAncestorOfClass(JPopupMenu.class, invoker);
            invoker = parent == null ? null : parent.getInvoker();
        }

        return false;
    }

    private void finishOverflowAction() {
        if (!overflowPopup.isVisible())
            return;

        for (MenuElement element : MenuSelectionManager.defaultManager().getSelectedPath()) {
            if (element instanceof JPopupMenu popup && isOverflowChild(popup)) {
                popup.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
                        popup.removePopupMenuListener(this);
                        SwingUtilities.invokeLater(GToolBar.this::closeOverflow);
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent event) {
                    }
                });
                return;
            }
        }

        closeOverflow();
    }

    private void closeOverflow() {
        if (overflowPopup == null || movingItems)
            return;

        overflowPopup.setVisible(false);
        restoreItems();
    }

    private void restoreItems() {
        if (popupItems.isEmpty())
            return;

        movingItems = true;

        try {
            for (PopupItem item : popupItems) {
                if (item.component() instanceof AbstractButton button) {
                    button.removeActionListener(overflowActionListener);
                }

                item.component().setBounds(0, 0, 0, 0);
                super.addImpl(item.component(), null, Math.min(item.index(), getComponentCount()));
            }

            popupItems.clear();
        } finally {
            movingItems = false;
        }

        revalidate();
        repaint();
    }
}
