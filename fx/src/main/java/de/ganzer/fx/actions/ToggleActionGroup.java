package de.ganzer.fx.actions;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;

import java.util.*;
import java.util.function.Consumer;

/**
 * Groups actions that are exclusively selectable.
 * <p>
 * This class helps to group radio buttons and exclusively checkable menu items
 * without defining special {@link ToggleGroup}. This enables handling of the
 * action states of multiple equal groups in menus and toolbars.
 */
@SuppressWarnings("unused")
public class ToggleActionGroup implements ActionItemBuilder, Iterable<Action> {
    private final List<Action> actions = new ArrayList<>();

    /**
     * Creates a group of the specified actions.
     *
     * @param actions The actions to add to the group.
     *
     * @return The created group.
     *
     * @throws NullPointerException {@code actions} is {@code null}.
     */
    public static ToggleActionGroup of(Action... actions) {
        Objects.requireNonNull(actions);

        ToggleActionGroup group = new ToggleActionGroup();
        group.actions.addAll(List.of(actions));

        return group;
    }

    /**
     * Calls {@link #setDisabled(boolean)}.
     *
     * @param disabled {@code true} to disable all contained actions.
     *
     * @return This group.
     */
    public ToggleActionGroup disabled(boolean disabled) {
        setDisabled(disabled);
        return this;
    }

    /**
     * Calls {@link #setVisible(boolean)}.
     *
     * @param visible {@code false} to hide all contained actions.
     *
     * @return This group.
     */
    public ToggleActionGroup visible(boolean visible) {
        setVisible(visible);
        return this;
    }

    /**
     * Gets a value that indicates whether the actions of this group are
     * disabled.
     *
     * @return {@code true} if all actions are disabled; otherwise, {@code false}.
     */
    public boolean isDisabled() {
        return actions.stream().allMatch(Action::isDisabled);
    }

    /**
     * Sets a value that indicates whether the actions of this group are
     * disabled.
     *
     * @param disabled {@code true} to disable all contained actions.
     */
    public void setDisabled(boolean disabled) {
        for (Action action: actions)
            action.setDisabled(disabled);
    }

    /**
     * Gets a value that indicates whether the actions of this group are
     * visible.
     *
     * @return {@code true} if all actions are visible; otherwise, {@code false}.
     */
    public boolean isVisible() {
        return actions.stream().allMatch(Action::isVisible);
    }

    /**
     * Sets a value that indicates whether the actions of this group are
     * visible.
     *
     * @param visible {@code false} to hide all contained actions.
     */
    public void setVisible(boolean visible) {
        for (Action action: actions)
            action.setVisible(visible);
    }

    /**
     * Adds the specified action to the group.
     *
     * @param action The action to add.
     *
     * @throws NullPointerException {@code action} is {@code null}.
     */
    public void add(Action action) {
        Objects.requireNonNull(action);

        actions.add(action);
        action.selectedProperty().addListener(selectedListener);
    }

    /**
     * Adds the specified actions to the group.
     *
     * @param actions The actions to add.
     *
     * @return This group.
     *
     * @throws NullPointerException {@code actions} is {@code null}.
     */
    public ActionItemBuilder addAll(Action... actions) {
        Objects.requireNonNull(actions);

        for (Action action: actions)
            add(action);

        return this;
    }

    /**
     * Removes the specified action.
     *
     * @param action The action to remove.
     *
     * @return {@code true} if the action is found and removed; otherwise,
     *         {@code false}.
     */
    public boolean remove(Action action) {
        if (action != null)
            action.selectedProperty().removeListener(selectedListener);

        return actions.remove(action);
    }

    /**
     * Gets the number of contained actions.
     *
     * @return The size of the group.
     */
    public int size() {
        return actions.size();
    }

    /**
     * Gets a value that indicates whether the group is empty.
     *
     * @return {@code true} if the group is empty; otherwise, {@code false}.
     */
    public boolean isEmpty() {
        return actions.isEmpty();
    }

    /**
     * Gets the action at the specified index.
     *
     * @param index The index of the action to get.
     *
     * @return The action at {@code index}.
     *
     * @throws IndexOutOfBoundsException {@code index} is not in the range from
     *         0 to "{@link #size()} - 1".
     */
    public Action getAt(int index) {
        return actions.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Action> iterator() {
        return actions.iterator();
    }

    /**
     * Creates menu items for each contained action.
     *
     * @return The created items.
     */
    @Override
    public List<MenuItem> createMenuItems() {
        List<MenuItem> items = new ArrayList<>();

        for (ActionItemBuilder item: actions)
            items.addAll(item.createMenuItems());

        return items;
    }

    /**
     * Creates buttons for each contained action.
     *
     * @return The created buttons.
     */
    @Override
    public List<Node> createButtons(boolean focusTraversable) {
        List<Node> buttons = new ArrayList<>();

        for (ActionItemBuilder button: actions)
            buttons.addAll(button.createButtons(focusTraversable));

        return buttons;
    }

    private final SelectedListener selectedListener = new SelectedListener();

    private class SelectedListener implements ChangeListener<Boolean> {
        @Override
        public void changed(ObservableValue<? extends Boolean> value, Boolean oldValue, Boolean newValue) {
            if (newValue)
                for (Action a: actions)
                    if (a.selectedProperty() != value)
                        a.setSelected(false);
        }
    }
}
