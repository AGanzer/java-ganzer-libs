package de.ganzer.swing.actions;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Groups actions that are exclusively selectable.
 * <p>
 * This class helps to group radio buttons and exclusively checkable menu items
 * without explicitly define an {@link ButtonGroup} when the items are created
 * by this group.
 * <p>
 * This class does also enable handling of the enabled status of multiple equal
 * groups in menus and toolbars.
 */
@SuppressWarnings("unused")
public class GToggleActionGroup implements GActionItemBuilder, Iterable<GAction> {
    private final List<GAction> actions = new ArrayList<>();

    /**
     * Creates a group of the specified actions.
     *
     * @param actions The actions to add to the group.
     *
     * @return The created group.
     *
     * @throws NullPointerException {@code actions} is {@code null} or one of
     *         the elements in {@code actions} is {@code null}.
     */
    public static GToggleActionGroup of(GAction... actions) {
        Objects.requireNonNull(actions);

        GToggleActionGroup group = new GToggleActionGroup();

        for (var action: List.of(actions)) {
            Objects.requireNonNull(action, "actions must not contain null values.");

            action.addPropertyChangeListener(group.selectedListener);
            group.actions.add(action);
        }

        return group;
    }

    /**
     * Disables all actions of this group.
     *
     * @param disabled {@code true} to disable all contained actions.
     *
     * @return This group.
     */
    public GToggleActionGroup disabled(boolean disabled) {
        for (GAction action: actions)
            action.setEnabled(!disabled);

        return this;
    }

    /**
     * Gets a value that indicates whether the actions of this group are
     * disabled.
     *
     * @return {@code true} if all actions are disabled; otherwise, {@code false}.
     */
    public boolean isDisabled() {
        return actions.stream().noneMatch(GAction::isEnabled);
    }

    /**
     * Creates the menu items that visualizes this toggle group and inserts them
     * into the specified target.
     *
     * @param target The menu where to insert the menu items.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addMenuItems(JMenu target) {
        for (var action: actions)
            action.addMenuItems(target);
    }

    /**
     * Creates the buttons that visualizes this toggle group and inserts them
     * into the specified target.
     *
     * @param target The toolbar where to insert the buttons.
     * @param focusable Indicates whether the created button shall be focusable.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addButtons(JToolBar target, boolean focusable) {
        for (var action: actions)
            action.addButtons(target, focusable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<GAction> iterator() {
        return actions.iterator();
    }

    private final SelectedListener selectedListener = new SelectedListener();

    private class SelectedListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (!e.getPropertyName().equals(Action.SELECTED_KEY))
                return;

            if (e.getNewValue() != null && (Boolean) e.getNewValue()) {
                for (var action: actions) {
                    if (action != e.getSource())
                        action.selected(false);
                }
            }

        }
    }
}
