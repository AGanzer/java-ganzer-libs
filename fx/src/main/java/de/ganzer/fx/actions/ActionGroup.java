package de.ganzer.fx.actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class ActionGroup extends Action {
    private final List<ActionItemBuilder> actions = new ArrayList<>();

    public ActionGroup() {
    }

    public ActionGroup(String commandText) {
        super(commandText);
    }

    @Override
    public ActionGroup commandText(String commandText) {
        setCommandText(commandText);
        return this;
    }

    @Override
    public ActionGroup disabled(boolean disabled) {
        setDisabled(disabled);
        return this;
    }

    @Override
    public ActionGroup visible(boolean visible) {
        setVisible(visible);
        return this;
    }

    @Override
    public ActionGroup menuImage(Node image) {
        setMenuImage(image);
        return this;
    }

    @Override
    public ActionGroup buttonImage(Node image) {
        setButtonImage(image);
        return this;
    }

    @Override
    public ActionGroup onAction(EventHandler<ActionEvent> onAction) {
        setOnAction(onAction);
        return this;
    }

    @Override
    public ActionGroup notBindMenu(int notBindMenu) {
        setNotBindMenu(notBindMenu);
        return this;
    }

    @Override
    public ActionGroup notBindButton(int notBindButton) {
        setNotBindButton(notBindButton);
        return this;
    }

    @Override
    public ActionGroup tooltipText(String tooltipText) {
        setTooltipText(tooltipText);
        return this;
    }

    public ActionGroup addAll(ActionItemBuilder... actions) {
        Objects.requireNonNull(actions);

        this.actions.addAll(List.of(actions));
        return this;
    }

    public Menu createMenu() {
        Menu menu = new Menu();
        bindTo(menu);

        menu.getItems().addAll(createItems());

        return menu;
    }

    private Collection<? extends MenuItem> createItems() {
        List<MenuItem> items = new ArrayList<>();

        for (ActionItemBuilder item: actions)
            items.addAll(item.createMenuItems());

        return items;
    }
}
