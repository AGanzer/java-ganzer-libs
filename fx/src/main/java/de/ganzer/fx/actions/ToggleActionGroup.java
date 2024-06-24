package de.ganzer.fx.actions;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ToggleActionGroup implements ActionItemBuilder, Iterable<Action> {
    private final List<Action> actions = new ArrayList<>();

    public static ToggleActionGroup of(Action... actions) {
        ToggleActionGroup group = new ToggleActionGroup();
        group.actions.addAll(List.of(actions));

        return group;
    }

    public ToggleActionGroup disabled(boolean disabled) {
        setDisabled(disabled);
        return this;
    }

    public ToggleActionGroup visible(boolean visible) {
        setVisible(visible);
        return this;
    }

    public boolean isDisabled() {
        return actions.stream().allMatch(Action::isDisabled);
    }

    public void setDisabled(boolean disabled) {
        for (Action action: actions)
            action.setDisabled(disabled);
    }

    public boolean isVisible() {
        return actions.stream().allMatch(Action::isVisible);
    }

    public void setVisible(boolean visible) {
        for (Action action: actions)
            action.setVisible(visible);
    }

    public void add(Action action) {
        Objects.requireNonNull(action);

        actions.add(action);
        action.selectedProperty().addListener(selectedListener);
    }

    public ActionItemBuilder addAll(Action... actions) {
        Objects.requireNonNull(actions);

        for (Action action: actions)
            add(action);

        return this;
    }

    public boolean remove(Action action) {
        if (action != null)
            action.selectedProperty().removeListener(selectedListener);

        return actions.remove(action);
    }

    public int size() {
        return actions.size();
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }

    public Action getAt(int index) {
        return actions.get(index);
    }

    @Override
    public Iterator<Action> iterator() {
        return actions.iterator();
    }

    @Override
    public void forEach(Consumer<? super Action> action) {
        actions.forEach(action);
    }

    @Override
    public Spliterator<Action> spliterator() {
        return actions.spliterator();
    }

    private final SelectedListener selectedListener = new SelectedListener();

    @Override
    public List<MenuItem> createMenuItems() {
        List<MenuItem> items = new ArrayList<>();

        for (ActionItemBuilder item: actions)
            items.addAll(item.createMenuItems());

        return items;
    }

    @Override
    public List<Node> createButtons(boolean focusTraversable) {
        List<Node> buttons = new ArrayList<>();

        for (ActionItemBuilder button: actions)
            buttons.addAll(button.createButtons(focusTraversable));

        return buttons;
    }

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
