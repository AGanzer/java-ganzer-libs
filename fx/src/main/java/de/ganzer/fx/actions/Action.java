package de.ganzer.fx.actions;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;

import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Action implements ActionItemBuilder {
    private final StringProperty commandText;
    private final ObjectProperty<KeyCombination> accelerator;
    private final StringProperty tooltipText;
    private final ObjectProperty<Node> menuImage;
    private final ObjectProperty<Node> buttonImage;
    private final BooleanProperty disabled;
    private final BooleanProperty selected;
    private final BooleanProperty visible;
    private final ObjectProperty<EventHandler<ActionEvent>> onAction;
    private boolean selectable;
    private boolean exclusiveSelectable;
    private int notBindMenu;
    private int notBindButton;
    private Function<Node, Node> cloneImage;

    public Action() {
        this(null);
    }

    public Action(String commandText) {
        this.commandText = new SimpleStringProperty(commandText);
        this.accelerator = new SimpleObjectProperty<>();
        this.tooltipText = new SimpleStringProperty();
        this.menuImage = new SimpleObjectProperty<>();
        this.buttonImage = new SimpleObjectProperty<>();
        this.disabled = new SimpleBooleanProperty(false);
        this.selected = new SimpleBooleanProperty(false);
        this.visible = new SimpleBooleanProperty(true);
        this.onAction = new SimpleObjectProperty<>();
    }

    public Action commandText(String commandText) {
        setCommandText(commandText);
        return this;
    }

    public Action tooltipText(String tooltipText) {
        setTooltipText(tooltipText);
        return this;
    }

    public Action accelerator(KeyCombination accelerator) {
        setAccelerator(accelerator);
        return this;
    }

    public Action menuImage(Node image) {
        setMenuImage(image);
        return this;
    }

    public Action buttonImage(Node image) {
        setButtonImage(image);
        return this;
    }

    public Action disabled(boolean disabled) {
        setDisabled(disabled);
        return this;
    }

    public Action visible(boolean visible) {
        setVisible(visible);
        return this;
    }

    public Action selected(boolean selected) {
        setSelected(selected);
        return this;
    }

    public Action onAction(EventHandler<ActionEvent> onAction) {
        setOnAction(onAction);
        return this;
    }

    public Action selectable(boolean selectable) {
        setSelectable(selectable);
        return this;
    }

    public Action exclusiveSelectable(boolean exclusiveSelectable) {
        setExclusiveSelectable(exclusiveSelectable);
        return this;
    }

    public Action notBindMenu(int notBindMenu) {
        setNotBindMenu(notBindMenu);
        return this;
    }

    public Action notBindButton(int notBindButton) {
        setNotBindButton(notBindButton);
        return this;
    }

    public Action cloneImage(Function<Node, Node> cloneImage) {
        setCloneImage(cloneImage);
        return this;
    }

    public Action bindTo(MenuItem item) {
        return bindTo(item, 0);
    }

    public Action bindTo(MenuItem item, int not) {
        item.onActionProperty().bind(onActionProperty());

        if ((not & BindNot.COMMAND_TEXT) == 0)
            item.textProperty().bind(commandTextProperty());

        if ((not & BindNot.ACCELERATOR) == 0)
            item.acceleratorProperty().bind(acceleratorProperty());

        if ((not & BindNot.DISABLED) == 0)
            item.disableProperty().bind(disabledProperty());

        if ((not & BindNot.VISIBLE) == 0)
            item.visibleProperty().bind(visibleProperty());

        if ((not & BindNot.SELECTED) == 0) {
            if (item instanceof RadioMenuItem)
                ((RadioMenuItem)item).selectedProperty().bindBidirectional(selectedProperty());
            else if (item instanceof CheckMenuItem)
                ((CheckMenuItem)item).selectedProperty().bindBidirectional(selectedProperty());
        }

        if ((not & BindNot.IMAGE) == 0) {
            item.graphicProperty().bind(new ObjectBinding<>() {
                {
                    this.bind(menuImageProperty());
                }

                protected Node computeValue() {
                    return cloneImage(getMenuImage());
                }

                public void removeListener(InvalidationListener listener) {
                    super.removeListener(listener);
                    this.unbind(menuImageProperty());
                }
            });
        }

        return this;
    }

    public Action bindTo(ButtonBase button) {
        return bindTo(button, 0);
    }

    public Action bindTo(ButtonBase button, int not) {
        button.onActionProperty().bind(onActionProperty());

        if ((not & BindNot.COMMAND_TEXT) == 0)
            button.textProperty().bind(commandTextProperty());

        if ((not & BindNot.DISABLED) == 0)
            button.disableProperty().bind(disabledProperty());

        if ((not & BindNot.VISIBLE) == 0)
            button.visibleProperty().bind(visibleProperty());

        if ((not & BindNot.TOOLTIP_TEXT) == 0) {
            String ttText = getTooltipText();

            if (ttText == null) {
                if (getAccelerator() == null)
                    ttText = removeMnemonic(getCommandText());
                else
                    ttText = String.format("%s (%s)", removeMnemonic(getCommandText()), accelerator.get());

                setTooltipText(ttText);
            }

            Tooltip tooltip = new Tooltip(getTooltipText());
            tooltip.textProperty().bind(tooltipTextProperty());
            button.setTooltip(tooltip);
        }

        if ((not & BindNot.SELECTED) == 0 && (button instanceof ToggleButton))
            ((ToggleButton)button).selectedProperty().bindBidirectional(selectedProperty());

        if ((not & BindNot.IMAGE) == 0) {
            button.graphicProperty().bind(new ObjectBinding<>() {
                {
                    this.bind(buttonImageProperty());
                }

                protected Node computeValue() {
                    return cloneImage(getButtonImage());
                }

                public void removeListener(InvalidationListener listener) {
                    super.removeListener(listener);
                    this.unbind(buttonImageProperty());
                }
            });
        }

        return this;
    }

    public String getCommandText() {
        return commandText.get();
    }

    public void setCommandText(String commandText) {
        this.commandText.set(commandText);
    }

    public String getTooltipText() {
        return tooltipText.get();
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText.set(tooltipText);
    }

    public Node getMenuImage() {
        return menuImage.get();
    }

    public void setMenuImage(Node menuImage) {
        this.menuImage.set(menuImage);
    }

    public Node getButtonImage() {
        return buttonImage.get();
    }

    public void setButtonImage(Node buttonImage) {
        this.buttonImage.set(buttonImage);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public boolean isDisabled() {
        return disabled.get();
    }

    public void setDisabled(boolean disabled) {
        this.disabled.set(disabled);
    }

    public boolean isVisible() {
        return visible.get();
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }

    public KeyCombination getAccelerator() {
        return accelerator.get();
    }

    public void setAccelerator(KeyCombination accelerator) {
        this.accelerator.set(accelerator);
    }

    public EventHandler<ActionEvent> getOnAction() {
        return onAction.get();
    }

    public void setOnAction(EventHandler<ActionEvent> onAction) {
        this.onAction.set(onAction);
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isExclusiveSelectable() {
        return exclusiveSelectable;
    }

    public void setExclusiveSelectable(boolean exclusiveSelectable) {
        this.exclusiveSelectable = exclusiveSelectable;
    }

    public int getNotBindMenu() {
        return notBindMenu;
    }

    public void setNotBindMenu(int notBindMenu) {
        this.notBindMenu = notBindMenu;
    }

    public int getNotBindButton() {
        return notBindButton;
    }

    public void setNotBindButton(int notBindButton) {
        this.notBindButton = notBindButton;
    }

    public Function<Node, Node> getCloneImage() {
        return cloneImage;
    }

    public void setCloneImage(Function<Node, Node> cloneImage) {
        this.cloneImage = cloneImage;
    }

    public StringProperty commandTextProperty() {
        return commandText;
    }

    public ObjectProperty<KeyCombination> acceleratorProperty() {
        return accelerator;
    }

    public StringProperty tooltipTextProperty() {
        return tooltipText;
    }

    public ObjectProperty<Node> menuImageProperty() {
        return menuImage;
    }

    public ObjectProperty<Node> buttonImageProperty() {
        return buttonImage;
    }

    public BooleanProperty disabledProperty() {
        return disabled;
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public BooleanProperty visibleProperty() {
        return visible;
    }

    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onAction;
    }

    @Override
    public List<MenuItem> createMenuItems() {
        MenuItem item;

        if (exclusiveSelectable)
            item = new RadioMenuItem();
        else if (selectable)
            item = new CheckMenuItem();
        else
            item = new MenuItem();

        bindTo(item, notBindMenu);

        return List.of(item);
    }

    @Override
    public List<Node> createButtons(boolean focusTraversable) {
        ButtonBase button;

        if (exclusiveSelectable || selectable)
            button = new ToggleButton();
        else
            button = new Button();

        bindTo(button, notBindButton);
        button.setFocusTraversable(focusTraversable);

        return List.of(button);
    }

    private Node cloneImage(Node image) {
        if (cloneImage != null)
            return cloneImage.apply(image);

        if (image instanceof ImageView)
            return new ImageView(((ImageView)image).getImage());

        return null;
    }

    private static String removeMnemonic(String text) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); ++i) {
            if (text.charAt(i) != '_')
                sb.append(text.charAt(i));
            else {
                int j = i + 1;

                if (j == text.length() || text.charAt(j) == '_')
                    sb.append(text.charAt(i++));
                else {
                    sb.append(text.substring(j));
                    break;
                }
            }
        }

        return sb.toString();
    }
}
