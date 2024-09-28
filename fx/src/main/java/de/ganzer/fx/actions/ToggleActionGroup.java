package de.ganzer.fx.actions;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;

import java.util.*;

/**
 * Groups actions that are exclusively selectable.
 * <p>
 * This class helps to group radio buttons and exclusively checkable menu items
 * without defining special {@link ToggleGroup}. This enables handling of the
 * action states of multiple equal groups in menus and toolbars.
 * <p>
 * This class does also enable handling of the enabled status of multiple equal
 * groups in menus and toolbars.
 */
@SuppressWarnings("unused")
public class ToggleActionGroup implements ActionItemBuilder, Iterable<Action> {
    private final List<Action> actions = new ArrayList<>();
    private final ObjectProperty<Action> selectedAction  = new SimpleObjectProperty<>();

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
     * Gets the currently selected action in this group.
     *
     * @return The action that is currently selected or {@code null} if no
     *         action is selected.
     */
    public Action getSelectedAction() {
        return selectedAction.get();
    }

    /**
     * Gets the property that stores the currently selected action.
     *
     * @return The property that holds the selected action.
     */
    public ReadOnlyObjectProperty<Action> selectedActionProperty() {
        return selectedAction;
    }

    /**
     * Adds the specified actions to the group.
     *
     * @param actions The actions to add. If this is selected, {@link #getSelectedAction()}
     *        is set to {@code action}. {@link Action#isExclusiveSelectable()}
     *        is set to {@code true}.
     *
     * @return This group.
     *
     * @throws NullPointerException {@code actions} is {@code null}.
     */
    public ToggleActionGroup addAll(Action... actions) {
        Objects.requireNonNull(actions);

        for (Action action: actions) {
            Objects.requireNonNull(action);

            action.setExclusiveSelectable(true);

            if (action.isSelected())
                this.selectedAction.set(action);

            this.actions.add(action);
            action.selectedProperty().addListener(selectedListener);
        }

        return this;
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
     * @param focusTraversable Indicates whether the button can get the keyboard
     *                         focus.
     * @param imageSize        The size of the image to bind.
     *
     * @return The created buttons.
     */
    @Override
    public List<Node> createButtons(boolean focusTraversable, ImageSize imageSize) {
        List<Node> buttons = new ArrayList<>();

        for (ActionItemBuilder button: actions)
            buttons.addAll(button.createButtons(focusTraversable, imageSize));

        return buttons;
    }

    private final SelectedListener selectedListener = new SelectedListener();

    private class SelectedListener implements ChangeListener<Boolean> {
        @Override
        public void changed(ObservableValue<? extends Boolean> value, Boolean oldValue, Boolean newValue) {
            if (newValue) {
                for (Action a : actions) {
                    if (a.selectedProperty() != value)
                        a.setSelected(false);
                    else
                        selectedAction.set(a);
                }
            }
        }
    }
}
