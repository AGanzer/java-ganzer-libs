package de.ganzer.fx.actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.*;

/**
 * Groups several actions to enable the creation of menus and menu items.
 * <p>
 * See {@link Action} for an example.
 */
@SuppressWarnings("unused")
public class ActionGroup extends Action implements Iterable<ActionItemBuilder> {
    private final List<ActionItemBuilder> actions = new ArrayList<>();

    /**
     * Creates an empty action group with no menu text.
     */
    public ActionGroup() {
    }

    /**
     * Creates an empty action group with the specified menu text.
     *
     * @param commandText The text of the menu to set.
     */
    public ActionGroup(String commandText) {
        super(commandText);
    }

    /**
     * Calls {@link #setCommandText(String)}.
     *
     * @param commandText The command text to set.
     *
     * @return This action group.
     */
    @Override
    public ActionGroup commandText(String commandText) {
        setCommandText(commandText);
        return this;
    }

    /**
     * Calls {@link #setDisabled(boolean)}.
     *
     * @param disabled {@code true} to disable the action; otherwise, the action
     *                 is enabled.
     *
     * @return This action group.
     */
    @Override
    public ActionGroup disabled(boolean disabled) {
        setDisabled(disabled);
        return this;
    }

    /**
     * Calls {@link #setVisible(boolean)}.
     *
     * @param visible {@code false} to hide the controls that are bound to this
     *                action; otherwise, the controls are shown.
     *
     * @return This action group.
     */
    @Override
    public ActionGroup visible(boolean visible) {
        setVisible(visible);
        return this;
    }

    /**
     * Calls {@link #setSmallButtonImage(Node)}.
     *
     * @param image The image to set.
     *
     * @return This action group.
     */
    @Override
    public ActionGroup smallButtonImage(Node image) {
        setSmallButtonImage(image);
        return this;
    }

    /**
     * Calls {@link #setMediumButtonImage(Node)}.
     *
     * @param image The image to set.
     *
     * @return This action group.
     */
    @Override
    public ActionGroup mediumButtonImage(Node image) {
        setMediumButtonImage(image);
        return this;
    }

    /**
     * Calls {@link #setLargeButtonImage(Node)}.
     *
     * @param image The image to set.
     *
     * @return This action group.
     */
    @Override
    public ActionGroup largeButtonImage(Node image) {
        setLargeButtonImage(image);
        return this;
    }

    /**
     * Calls {@link #setOnAction(EventHandler)}.
     *
     * @param onAction The action handler to invoke when this action is fired.
     *
     * @return This action group.
     */
    @Override
    public ActionGroup onAction(EventHandler<ActionEvent> onAction) {
        setOnAction(onAction);
        return this;
    }

    /**
     * Calls {@link #setNotBindMenu(int)}.
     *
     * @param notBindMenu The properties that shall not be bound to the created
     *                    menu item.
     *
     * @return This action group.
     */
    @Override
    public ActionGroup notBindMenu(int notBindMenu) {
        setNotBindMenu(notBindMenu);
        return this;
    }

    /**
     * Calls {@link #setNotBindButton(int)}.
     *
     * @param notBindButton The properties that shall not be bound to the
     *                      created button.
     *
     * @return This action group.
     */
    @Override
    public ActionGroup notBindButton(int notBindButton) {
        setNotBindButton(notBindButton);
        return this;
    }

    /**
     * Calls {@link #setTooltipText(String)}.
     *
     * @param tooltipText The text to set.
     *
     * @return This action group.
     */
    @Override
    public ActionGroup tooltipText(String tooltipText) {
        setTooltipText(tooltipText);
        return this;
    }

    /**
     * Calls {@link #setTag(Object)}.
     *
     * @param tag The tag to set.
     *
     * @return This action.
     */
    public ActionGroup tag(Object tag) {
        setTag(tag);
        return this;
    }

    /**
     * Creates a menu that contains all actions of this group as menu items.
     *
     * @return A list with exactly one menu.
     */
    @Override
    public List<MenuItem> createMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        items.add(createMenu());

        bindTo(items.get(0), getNotBindMenu());

        return items;
    }

    /**
     * Creates for all actions of this group buttons.
     *
     * @param focusTraversable Indicates whether the button can get the keyboard
     *                         focus.
     * @param imageSize        The size of the image to bind.
     *
     * @return A list with the created buttons.
     */
    @Override
    public List<Node> createButtons(boolean focusTraversable, ImageSize imageSize) {
        List<Node> buttons = new ArrayList<>();

        for (ActionItemBuilder action: actions) {
            if (action instanceof ActionGroup)
                buttons.add(createMenuButton((ActionGroup)action, focusTraversable, imageSize));
            else
                buttons.addAll(action.createButtons(focusTraversable, imageSize));
        }

        return buttons;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<ActionItemBuilder> iterator() {
        return actions.iterator();
    }

    /**
     * Creates a menu with all containing actions.
     *
     * @return The created menu.
     */
    @Override
    public Menu createMenu() {
        Menu menu = new Menu();
        bindTo(menu, getNotBindMenu());

        menu.getItems().addAll(createItems());

        return menu;
    }

    /**
     * Creates a collection of menus that visualizes an action group.
     * <p>
     * If this group contains items of type {@link SeparatorAction} or
     * {@link Action}, these items will not be in the returned collection.
     *
     * @return A collection of menus.
     */
    @Override
    public List<Menu> createMenus() {
        List<Menu> menus = new ArrayList<>();

        for (var item: this)
            if (item instanceof ActionGroup)
                menus.add(item.createMenu());

        return menus;
    }

    /**
     * Adds the specified actions into this group.
     *
     * @param actions The actions to add.
     *
     * @return This action group.
     *
     * @throws NullPointerException {@code actions} is {@code null}.
     */
    public ActionGroup addAll(ActionItemBuilder... actions) {
        Objects.requireNonNull(actions);

        this.actions.addAll(List.of(actions));
        return this;
    }

    /**
     * Creates a menu button with all containing actions.
     *
     * @param focusTraversable Indicates whether the button can get the keyboard
     *                         focus.
     * @param imageSize        The size of the image to bind.
     *
     * @return The created menu.
     */
    public Node createMenuButton(boolean focusTraversable, ImageSize imageSize) {
        return createMenuButton(this, focusTraversable, imageSize);
    }

    private Node createMenuButton(ActionGroup action, boolean focusTraversable, ImageSize imageSize) {
        MenuButton button = new MenuButton();
        button.getItems().addAll(action.createItems());
        button.setFocusTraversable(focusTraversable);

        action.bindTo(button, imageSize, action.getNotBindButton() | BindNot.ACTION);

        return button;
    }

    private Collection<? extends MenuItem> createItems() {
        List<MenuItem> items = new ArrayList<>();

        for (ActionItemBuilder action: actions)
            items.addAll(action.createMenuItems());

        return items;
    }
}
