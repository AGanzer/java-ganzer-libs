package de.ganzer.swing.actions;

import javax.swing.*;
import javax.swing.event.EventListenerList;
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
    private final EventListenerList selectedActionChangedListeners = new EventListenerList();
    private final List<GAction> actions = new ArrayList<>();
    private GAction selectedAction;
    private boolean forceSelection;

    /**
     * Creates a new toggle group with {@link #isForceSelection()} set to
     * {@code true}.
     */
    public GToggleActionGroup() {
        this(true);
    }

    /**
     * Creates a new toggle group.
     *
     * @param forceSelection If this is {true}, it is not allowed that no
     *        contained action is selected.
     */
    public GToggleActionGroup(boolean forceSelection) {
        this.forceSelection = forceSelection;
    }

    /**
     * Sets a value that indicates whether no selected action is prohibited in
     * this group or if deselecting all actions is allowed.
     *
     * @param forceSelection {@code true} to allow that no action is selected.
     *
     * @return {@code this}.
     */
    public GToggleActionGroup forceSelection(boolean forceSelection) {
        this.forceSelection = forceSelection;
        return this;
    }

    /**
     * Gets a value that indicates whether no selected action is prohibited in
     * this group or if deselecting all actions is allowed.
     *
     * @return {@code true} if the selection of no action forbidden; otherwise,
     *         {@code false}.
     */
    public boolean isForceSelection() {
        return forceSelection;
    }

    /**
     * Adds the specified listener that is called when the selected action has
     * changed.
     *
     * @param listener The listener to add.
     *
     * @return {@code this}.
     *
     * @throws NullPointerException {@code listener} is {@code null}.
     */
    public GToggleActionGroup onSelectedActionChanged(GSelectedActionChangedListener listener) {
        addSelectedActionChangedListener(listener);
        return this;
    }

    /**
     * Adds the specified actions into this group.
     *
     * @param actions The actions to add to the group.
     *
     * @return {@code this}.
     *
     * @throws NullPointerException {@code actions} is {@code null} or one of
     *         the elements in {@code actions} is {@code null}.
     */
    public GToggleActionGroup addAll(GAction... actions) {
        Objects.requireNonNull(actions);

        for (var action: List.of(actions)) {
            Objects.requireNonNull(action, "actions must not contain null values.");

            if (action.isSelected())
                selectedAction = action;

            action.addActionListener(actionListener);
            this.actions.add(action);
        }

        return this;
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
     * Adds the specified listener that is called when the selected action has
     * changed.
     *
     * @param listener The listener to add.
     *
     * @throws NullPointerException {@code listener} is {@code null}.
     */
    public void addSelectedActionChangedListener(GSelectedActionChangedListener listener) {
        Objects.requireNonNull(listener);
        selectedActionChangedListeners.add(GSelectedActionChangedListener.class, listener);
    }

    /**
     * Gets the installed action listeners to be called when the action is
     * performed.
     *
     * @return The listeners. This may be empty if no listener is installed.
     */
    public GSelectedActionChangedListener[] getSelectedActionChangedListeners() {
        return selectedActionChangedListeners.getListeners(GSelectedActionChangedListener.class);
    }

    /**
     * Removes the specified listener from the action listener list.
     *
     * @param listener The listener to remove.
     */
    public void removeSelectedActionChangedListener(GSelectedActionChangedListener listener) {
        selectedActionChangedListeners.remove(GSelectedActionChangedListener.class, listener);
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

    private boolean hasNoSelection() {
        for (var action: actions) {
            if (action.isSelected())
                return false;
        }

        return true;
    }

    private void fireSelectedActionChanged(GAction source) {
        Object[] listeners = selectedActionChangedListeners.getListenerList();
        GSelectedActionChangedEvent e = null;

        // Process the listeners last to first, like AbstractButton does:
        //
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == GSelectedActionChangedListener.class) {
                // Lazily create the event:
                //
                if (e == null) {
                    GAction selected = source != null && source.isSelected() ? source : null;
                    e = new GSelectedActionChangedEvent(this, selected);
                }

                ((GSelectedActionChangedListener)listeners[i + 1]).selectedActionChanged(e);
            }
        }
    }

    private final GActionListener actionListener = new GActionListener() {
        @Override
        public void actionPerformed(GActionEvent event) {
            for (var action: actions) {
                if (action != event.getSource())
                    action.selected(false);
                else if (selectedAction != event.getSource()) {
                    selectedAction = event.getSource();
                    fireSelectedActionChanged(selectedAction);
                }
            }

            if (hasNoSelection()) {
                if (forceSelection)
                    event.getSource().selected(true);
                else {
                    selectedAction = null;
                    fireSelectedActionChanged(null);
                }
            }
        }
    };
}
