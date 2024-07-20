package de.ganzer.fx.actions;

import de.ganzer.fx.internals.FXMessages;
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

/*
Was noch fehlt:
- tag, um beliebige Benutzerdaten mit der Action zu verbinden.
- small, medium(?) und and large button images.
- boolean, um zu entscheiden, ob shortcuts in die tooltips sollen
  (ist es besser, dieses global festzulegen?).
- boolean, um zu entscheiden, ob shortcut in die Schalterbeschriftung
  soll (ist es besser, dieses global festzulegen?).
- createMenus() für das Erzeugen einer Liste von Menu-Objekten, die
  über MenuBar.getMenus().addAll() eingefügt werden können.

Möglichkeiten der Umsetzung:
- tooltip bekommt intern ein eigenes Property für Buttons. Dieses wird
  intern gesetzt über das bereits vorhandene Property (für die shortcuts).
- commandText bekommt intern ein eigenes Property für Buttons. Dieses wird
  intern gesetzt über das bereits vorhandene Property (für die shortcuts).
- small und large images über das Binding setzen. Weiterer Parameter:
  Bind (enum Bind mit SMALL, MEDIUM(?) und LARGE). Dieses löst nicht das
  Problem mit createButtons(), denn welches image soll verwendet werden?
  Hier ebenfalls den Parameter Bind einfügen?
*/

/**
 * The {@code Action} class provides a simplification to create menus and
 * buttons and to handle the status of the controls that are linked with
 * this action.
 * <p>
 * The following example creates a File and a Format menu and shows how a group
 * of radio menu items can be created and how the buttons can be inserted into
 * a toolbar:
 * <p>
 * {@code
public class MainWindowController implements Initializable {
    @FXML private MenuBar menuBar;
    @FXML private ToolBar toolbar;

    private Action saveAction;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createMenu();
    }

    private void createMenu() {
        Action newAction;
        Action openAction;
        menuBar.getMenus().add(new ActionGroup("_File").addAll(
                newAction = new Action("_New")
                        .accelerator(KeyCombination.valueOf("Ctrl+N"))
                        .menuImage(new ImageView(new Image("images/new16.png")))
                        .buttonImage(new ImageView(new Image("images/new22.png")))
                        .notBindButton(BindNot.COMMAND_TEXT)
                        .onAction(e -> newFile()),
                openAction = new Action("_Open...")
                        .accelerator(KeyCombination.valueOf("Ctrl+O"))
                        .menuImage(new ImageView(new Image("images/open16.png")))
                        .buttonImage(new ImageView(new Image("images/open22.png")))
                        .notBindButton(BindNot.COMMAND_TEXT)
                        .onAction(e -> openFile()),
                saveAction = new Action("_Save")
                        .accelerator(KeyCombination.valueOf("Ctrl+N"))
                        .menuImage(new ImageView(new Image("images/save16.png")))
                        .buttonImage(new ImageView(new Image("images/save22.png")))
                        .notBindButton(BindNot.COMMAND_TEXT)
                        .onAction(e -> saveFile()),
                new Action("Save _As...")
                        .menuImage(new ImageView(new Image("images/saveAs16.png")))
                        .buttonImage(new ImageView(new Image("images/saveAs22.png")))
                        .notBindButton(BindNot.COMMAND_TEXT)
                        .onAction(e -> saveFileAs()),
                new SeparatorAction(),
                new Action("E_xit")
                        .accelerator(KeyCombination.valueOf("Alt+F4"))
                        .menuImage(new ImageView(new Image("images/exit16.png")))
                        .buttonImage(new ImageView(new Image("images/exit22.png")))
                        .notBindButton(BindNot.COMMAND_TEXT)
                        .onAction(e -> exitApp())
        ).createMenu());

        ToggleActionGroup taGroup = new ToggleActionGroup();
        menuBar.getMenus().add(new ActionGroup("_Format").addAll(
                // We want a group of exclusive toggle actions:
                taGroup.addAll(
                        new Action("_Left")
                                .exclusiveSelectable(true)
                                .selected(true)
                                .menuImage(new ImageView(new Image("images/left16.png")))
                                .buttonImage(new ImageView(new Image("images/left22.png")))
                                .notBindButton(BindNot.COMMAND_TEXT)
                                .onAction(e -> adjustLeft()),
                        new Action("_Center")
                                .exclusiveSelectable(true)
                                .menuImage(new ImageView(new Image("images/center16.png")))
                                .buttonImage(new ImageView(new Image("images/center22.png")))
                                .notBindButton(BindNot.COMMAND_TEXT)
                                .onAction(e -> adjustCenter()),
                        new Action("_Right")
                                .exclusiveSelectable(true)
                                .menuImage(new ImageView(new Image("images/right16.png")))
                                .buttonImage(new ImageView(new Image("images/right22.png")))
                                .notBindButton(BindNot.COMMAND_TEXT)
                                .onAction(e -> adjustRight())
                )
        ).createMenu());

        // Add the File buttons separately, because we don't want all in the toolbar:
        toolbar.getItems().addAll(newAction.createButtons(false));
        toolbar.getItems().addAll(openAction.createButtons(false));
        toolbar.getItems().addAll(saveAction.createButtons(false));
        toolbar.getItems().add(new Separator());
        // Add all the format toggle buttons:
        toolbar.getItems().addAll(taGroup.createButtons(false));
    }

    private void newFile() {
        // New ...
        saveAction.setDisabled(true); // <- Disables menu item and button.
    }

    private void openFile() {
        // Open ...
        saveAction.setDisabled(true);
    }

    private void saveFile() {
        // Save ...
        saveAction.setDisabled(true);
    }

    private void editorChanged() {
        saveAction.setDisabled(false); // <- Enables menu item and button.
    }

    // Further methods and handlers here.
}
 * }
 *
 * Another example shows how to use this class with already existing menu items
 * and buttons:
 * {@code
public class MainWindowController implements Initializable {
    @FXML private MenuItem saveItem;
    @FXML private RadioMenuItem leftItem;
    @FXML private RadioMenuItem centerItem;
    @FXML private RadioMenuItem rightItem;
    @FXML private Button saveButton;
    @FXML private ToggleButton leftButton;
    @FXML private ToggleButton centerButton;
    @FXML private ToggleButton rightButton;

    private Action saveAction;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveAction = new Action("_Save")
                .accelerator(KeyCombination.valueOf("Ctrl+N"))
                .menuImage(new ImageView(new Image("images/save16.png")))
                .buttonImage(new ImageView(new Image("images/save22.png")))
                .onAction(e -> saveFile())
                .bindTo(saveItem)
                .bindTo(saveButton, BindNot.COMMAND_TEXT);

        new ToggleActionGroup.addAll(
                new Action("_Left")
                        .exclusiveSelectable(true)
                        .selected(true),
                        .menuImage(new ImageView(new Image("images/left16.png")))
                        .buttonImage(new ImageView(new Image("images/left22.png")))
                        .onAction(e -> adjustLeft())
                        .bindTo(leftItem)
                        .bindTo(leftButton, BindNot.COMMAND_TEXT),
                new Action("_Center")
                        .exclusiveSelectable(true)
                        .menuImage(new ImageView(new Image("images/center16.png")))
                        .buttonImage(new ImageView(new Image("images/center22.png")))
                        .onAction(e -> adjustCenter())
                        .bindTo(centerItem)
                        .bindTo(centerButton, BindNot.COMMAND_TEXT),
                new Action("_Right")
                        .exclusiveSelectable(true)
                        .menuImage(new ImageView(new Image("images/right16.png")))
                        .buttonImage(new ImageView(new Image("images/right22.png")))
                        .onAction(e -> adjustRight())
                        .bindTo(rightItem)
                        .bindTo(rightButton, BindNot.COMMAND_TEXT)
        );
    }

    // Further methods and handlers here.
}
 * }
 */
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

    /**
     * Creates an action with a {@code null} command text.
     */
    public Action() {
        this(null);
    }

    /**
     * Creates an action with the specified command text.
     *
     * @param commandText The command text of the action.
     *
     * @see #setCommandText(String)
     */
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

    /**
     * Calls {@link #setCommandText(String)}.
     *
     * @param commandText The command text to set.
     *
     * @return This action.
     */
    public Action commandText(String commandText) {
        setCommandText(commandText);
        return this;
    }

    /**
     * Calls {@link #setTooltipText(String)}.
     *
     * @param tooltipText The text to set.
     *
     * @return This action.
     */
    public Action tooltipText(String tooltipText) {
        setTooltipText(tooltipText);
        return this;
    }

    /**
     * Calls {@link #setAccelerator(KeyCombination)}.
     *
     * @param accelerator The accelerator key combination to set.
     *
     * @return This action.
     */
    public Action accelerator(KeyCombination accelerator) {
        setAccelerator(accelerator);
        return this;
    }

    /**
     * Calls {@link #setMenuImage(Node)}.
     *
     * @param image The image to set.
     *
     * @return This action.
     */
    public Action menuImage(Node image) {
        setMenuImage(image);
        return this;
    }

    /**
     * Calls {@link #setButtonImage(Node)}.
     *
     * @param image The image to set.
     *
     * @return This action.
     */
    public Action buttonImage(Node image) {
        setButtonImage(image);
        return this;
    }

    /**
     * Calls {@link #setDisabled(boolean)}.
     *
     * @param disabled {@code true} to disable the action; otherwise, the action
     *                 is enabled.
     *
     * @return This action.
     */
    public Action disabled(boolean disabled) {
        setDisabled(disabled);
        return this;
    }

    /**
     * Calls {@link #setVisible(boolean)}.
     *
     * @param visible {@code false} to hide the controls that are bound to this
     *                action; otherwise, the controls are shown.
     *
     * @return This action.
     */
    public Action visible(boolean visible) {
        setVisible(visible);
        return this;
    }

    /**
     * Calls {@link #setSelected(boolean)}.
     *
     * @param selected {@code true} to select the controls that are bound to
     *                 this action; otherwise, the controls are unselected.
     *
     * @return This action.
     */
    public Action selected(boolean selected) {
        setSelected(selected);
        return this;
    }

    /**
     * Calls {@link #setOnAction(EventHandler)}.
     *
     * @param onAction The action handler to invoke when this action is fired.
     *
     * @return This action.
     */
    public Action onAction(EventHandler<ActionEvent> onAction) {
        setOnAction(onAction);
        return this;
    }

    /**
     * Calls {@link #setSelectable(boolean)}.
     *
     * @param selectable {@code true} to create selectable controls; otherwise
     *                   the created controls are not selectable.
     *
     * @return This action.
     */
    public Action selectable(boolean selectable) {
        setSelectable(selectable);
        return this;
    }

    /**
     * Calls {@link #setExclusiveSelectable(boolean)}.
     *
     * @param exclusiveSelectable {@code true} to create exclusively selectable
     *                            controls; otherwise the created controls are
     *                            not selectable.
     *
     * @return This action.
     */
    public Action exclusiveSelectable(boolean exclusiveSelectable) {
        setExclusiveSelectable(exclusiveSelectable);
        return this;
    }

    /**
     * Calls {@link #setNotBindMenu(int)}.
     *
     * @param notBindMenu The properties that shall not be bound to the created
     *                    menu item.
     *
     * @return This action.
     */
    public Action notBindMenu(int notBindMenu) {
        setNotBindMenu(notBindMenu);
        return this;
    }

    /**
     * Calls {@link #setNotBindButton(int)}.
     *
     * @param notBindButton The properties that shall not be bound to the
     *                      created button.
     *
     * @return This action.
     */
    public Action notBindButton(int notBindButton) {
        setNotBindButton(notBindButton);
        return this;
    }

    /**
     * Calls {@link #setCloneImage(Function)}.
     *
     * @param cloneImage The handler to call when an image has to be cloned.
     *
     * @return This action.
     */
    public Action cloneImage(Function<Node, Node> cloneImage) {
        setCloneImage(cloneImage);
        return this;
    }

    /**
     * Binds this action to the specified menu item.
     * <p>
     * This method has to be called for already existing menu items that shall
     * be bound to this action. For all items that are created by this action
     * this is called automatically.
     *
     * @param item The item to bind.
     *
     * @return This action.
     *
     * @see #createMenuItems()
     */
    public Action bindTo(MenuItem item) {
        return bindTo(item, 0);
    }

    /**
     * Binds this action to the specified menu item.
     * <p>
     * This method has to be called for already existing menu items that shall
     * be bound to this action. For all items that are created by this action
     * this is called automatically.
     *
     * @param item The item to bind.
     * @param not The properties that shall not be bound to the item. This may
     *            be any combination of the {@link BindNot} values. The tooltip
     *            is never bound to a menu item.
     *
     * @return This action.
     *
     * @see #createMenuItems()
     */
    public Action bindTo(MenuItem item, int not) {
        if (shouldBind(not, BindNot.ACTION))
            item.onActionProperty().bind(onActionProperty());

        if (shouldBind(not, BindNot.COMMAND_TEXT))
            item.textProperty().bind(commandTextProperty());

        if (shouldBind(not, BindNot.ACCELERATOR))
            item.acceleratorProperty().bind(acceleratorProperty());

        if (shouldBind(not, BindNot.DISABLED))
            item.disableProperty().bind(disabledProperty());

        if (shouldBind(not, BindNot.VISIBLE))
            item.visibleProperty().bind(visibleProperty());

        if (shouldBind(not, BindNot.SELECTED)) {
            if (item instanceof RadioMenuItem)
                ((RadioMenuItem)item).selectedProperty().bindBidirectional(selectedProperty());
            else if (item instanceof CheckMenuItem)
                ((CheckMenuItem)item).selectedProperty().bindBidirectional(selectedProperty());
        }

        if (shouldBind(not, BindNot.IMAGE)) {
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

    /**
     * Binds this action to the specified button.
     * <p>
     * This method has to be called for already existing buttons that shall
     * be bound to this action. For all buttons that are created by this action
     * this is called automatically.
     *
     * @param button The item to bind.
     *
     * @return This action.
     *
     * @see #createButtons(boolean)
     */
    public Action bindTo(ButtonBase button) {
        return bindTo(button, 0);
    }

    /**
     * Binds this action to the specified button.
     * <p>
     * This method has to be called for already existing buttons that shall
     * be bound to this action. For all buttons that are created by this action
     * this is called automatically.
     *
     * @param button The item to bind.
     * @param not The properties that shall not be bound to the button. This may
     *            be any combination of the {@link BindNot} values. The accelerator
     *            is never bound to a button.
     *
     * @return This action.
     *
     * @see #createButtons(boolean)
     */
    public Action bindTo(ButtonBase button, int not) {
        if (shouldBind(not, BindNot.ACTION))
            button.onActionProperty().bind(onActionProperty());

        if (shouldBind(not, BindNot.COMMAND_TEXT))
            button.textProperty().bind(commandTextProperty());

        if (shouldBind(not, BindNot.DISABLED))
            button.disableProperty().bind(disabledProperty());

        if (shouldBind(not, BindNot.VISIBLE))
            button.visibleProperty().bind(visibleProperty());

        if (shouldBind(not, BindNot.TOOLTIP_TEXT)) {
            String ttText = getTooltipText();

            if (ttText == null) {
                if (getAccelerator() == null)
                    ttText = removeMnemonic(getCommandText());
                else
                    ttText = String.format(FXMessages.get("tooltipFormat"), removeMnemonic(getCommandText()), accelerator.get().getDisplayText());

                setTooltipText(ttText);
            }

            Tooltip tooltip = new Tooltip(getTooltipText());
            tooltip.textProperty().bind(tooltipTextProperty());
            button.setTooltip(tooltip);
        }

        if (shouldBind(not, BindNot.SELECTED) && (button instanceof ToggleButton))
            ((ToggleButton)button).selectedProperty().bindBidirectional(selectedProperty());

        if (shouldBind(not, BindNot.IMAGE)) {
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

    /**
     * Gets the command text of the action.
     *
     * @return The command text. The default is {@code null} if no other
     *         text is set.
     */
    public String getCommandText() {
        return commandText.get();
    }

    /**
     * Sets the command text of the action.
     * <p>
     * The command text is the text that is displayed in the bound menu items
     * and buttons.
     *
     * @param commandText The command text to set. This may contain mnemonics.
     */
    public void setCommandText(String commandText) {
        this.commandText.set(commandText);
    }

    /**
     * Gets the text of the tooltip.
     *
     * @return The text of the tooltip. The default is {@code null} if no other
     *         text is set.
     */
    public String getTooltipText() {
        return tooltipText.get();
    }

    /**
     * Sets the text of the tooltip to show.
     * <p>
     * If no tooltip text is set and the tooltip shall be bound, a tooltip is
     * created from the command text and the set accelerator.
     *
     * @param tooltipText The text of the tooltip to set.
     */
    public void setTooltipText(String tooltipText) {
        this.tooltipText.set(tooltipText);
    }

    /**
     * Gets the image to use for the menu item.
     *
     * @return The image to set. The default is {@code null}.
     */
    public Node getMenuImage() {
        return menuImage.get();
    }

    /**
     * Sets the image to use for menu items.
     *
     * @param menuImage The image to use. If this is not of type {@link ImageView},
     *                  a cloning function must be set.
     *
     * @see #cloneImage(Function)
     */
    public void setMenuImage(Node menuImage) {
        this.menuImage.set(menuImage);
    }

    /**
     * Gets the image to use for buttons.
     *
     * @return The image to use. The default is {@code null}.
     */
    public Node getButtonImage() {
        return buttonImage.get();
    }

    /**
     * Sets the image to use for buttons.
     *
     * @param buttonImage The image to use. If this is not of type {@link ImageView},
     *                    a cloning function must be set.
     *
     * @see #cloneImage(Function)
     */
    public void setButtonImage(Node buttonImage) {
        this.buttonImage.set(buttonImage);
    }

    /**
     * Gets the selection status of the action.
     *
     * @return {@code true} if the bound controls are selected; otherwise,
     *         {@code false}. The default is {@code false}.
     */
    public boolean isSelected() {
        return selected.get();
    }

    /**
     * Sets the selection status of the action.
     *
     * @param selected {@code true} to select the bound controls; otherwise,
     *                 {@code false}.
     */
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    /**
     * Gets a value that indicates whether the action is disabled.
     *
     * @return {@code true} if the bound controls are disabled; otherwise,
     *         {@code false}. The default is {@code false}.
     */
    public boolean isDisabled() {
        return disabled.get();
    }

    /**
     * Enables or disables the action.
     *
     * @param disabled {@code true} to disable the bound controls; otherwise,
     *                 {@code false}.
     */
    public void setDisabled(boolean disabled) {
        this.disabled.set(disabled);
    }

    /**
     * Gets a value that indicates whether the action is visible.
     *
     * @return {@code true} if the bound controls are visible; otherwise,
     *         {@code false}. The default is {@code true}.
     */
    public boolean isVisible() {
        return visible.get();
    }

    /**
     * Sets the visibility of the action.
     *
     * @param visible {@code true} to show the bound controls; otherwise,
     *                {@code false}.
     */
    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }

    /**
     * Gets the accelerator of the action.
     *
     * @return The accelerator. The default is {@code null}.
     */
    public KeyCombination getAccelerator() {
        return accelerator.get();
    }

    /**
     * Sets the accelerator.
     *
     * @param accelerator The accelerator to set.
     */
    public void setAccelerator(KeyCombination accelerator) {
        this.accelerator.set(accelerator);
    }

    /**
     * Gets the event handler that is bound to the action.
     *
     * @return The handler to call when the action is fired. The default is
     *         {@code null}.
     */
    public EventHandler<ActionEvent> getOnAction() {
        return onAction.get();
    }

    /**
     * Sets the event handler to call when the action is fired.
     *
     * @param onAction The handler to set.
     */
    public void setOnAction(EventHandler<ActionEvent> onAction) {
        this.onAction.set(onAction);
    }

    /**
     * Gets a value that indicates whether the action is selectable.
     * <p>
     * This is only used for creating menu items and buttons, to detect the
     * type if the controls to create. If this is {@code true}, the created
     * controls are {@link CheckMenuItem} and {@link ToggleButton} instead of
     * {@link MenuItem} and {@link Button}.
     *
     * @return {@code true} if the action is selectable; otherwise,
     *         {@code false}. The default is {@code false}. This is
     *         ignored if {@link #isExclusiveSelectable()} is {@code true}.
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * Sets a value that indicates whether the action is selectable.
     * <p>
     * This is only used for creating menu items and buttons, to detect the
     * type if the controls to create. If this is {@code true}, the created
     * controls are {@link CheckMenuItem} and {@link ToggleButton} instead of
     * {@link MenuItem} and {@link Button}.
     *
     * @param selectable {@code true} to make the action selectable. This
     *                   is ignored if {@link #isExclusiveSelectable()} is
     *                   {@code true}.
     */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * Gets a value that indicates whether the action is exclusively selectable.
     * <p>
     * This is only used for creating menu items and buttons, to detect the
     * type if the controls to create. If this is {@code true}, the created
     * controls are {@link RadioMenuItem} and {@link ToggleButton} instead of
     * {@link MenuItem} and {@link Button}.
     *
     * @return {@code true} if the action is selectable; otherwise, {@code false}.
     *         The default is {@code false}.
     */
    public boolean isExclusiveSelectable() {
        return exclusiveSelectable;
    }

    /**
     * Sets a value that indicates whether the action is exclusively selectable.
     * <p>
     * This is only used for creating menu items and buttons, to detect the
     * type if the controls to create. If this is {@code true}, the created
     * controls are {@link RadioMenuItem} and {@link ToggleButton} instead of
     * {@link MenuItem} and {@link Button}.
     *
     * @param exclusiveSelectable {@code true} to make the action exclusively
     *                            selectable.
     */
    public void setExclusiveSelectable(boolean exclusiveSelectable) {
        this.exclusiveSelectable = exclusiveSelectable;
    }

    /**
     * Gets the properties that shall not be bound to a created menu item.
     * <p>
     * This is used only when the controls are created by this action. If
     * already existing controls shall be bound, use {@link #bindTo(MenuItem, int)}.
     *
     * @return The properties that shall not be bound. This may be any
     *         combination of the {@link BindNot} values.
     */
    public int getNotBindMenu() {
        return notBindMenu;
    }

    /**
     * Sets the properties that shall not be bound to a created menu item.
     * <p>
     * This is used only when the controls are created by this action. If
     * already existing controls shall be bound, use {@link #bindTo(MenuItem, int)}.
     *
     * @param notBindMenu The properties that shall not be bound. This may be
     *                    any combination of the {@link BindNot} values.
     */
    public void setNotBindMenu(int notBindMenu) {
        this.notBindMenu = notBindMenu;
    }

    /**
     * Gets the properties that shall not be bound to a created button.
     * <p>
     * This is used only when the controls are created by this action. If
     * already existing controls shall be bound, use {@link #bindTo(ButtonBase, int)}.
     *
     * @return The properties that shall not be bound. This may be any
     *         combination of the {@link BindNot} values.
     */
    public int getNotBindButton() {
        return notBindButton;
    }

    /**
     * Sets the properties that shall not be bound to a created button.
     * <p>
     * This is used only when the controls are created by this action. If
     * already existing controls shall be bound, use {@link #bindTo(ButtonBase, int)}.
     *
     * @param notBindButton The properties that shall not be bound. This may be
     *                      any combination of the {@link BindNot} values.
     */
    public void setNotBindButton(int notBindButton) {
        this.notBindButton = notBindButton;
    }

    /**
     * Gets the function to call when an image has to be cloned.
     * <p>
     * Image nodes are derived from {@link Node}. Whenever an image is shown
     * in a parent node, it cannot be shown in another node. Therefor it is
     * necessary to clone the image node.
     * <p>
     * If the images to use for menu items or buttons are not of type
     * {@link ImageView}, a cloning function must be provided.
     *
     * @return The function to call.
     */
    public Function<Node, Node> getCloneImage() {
        return cloneImage;
    }

    /**
     * Sets the function to use for cloning the images.
     * <p>
     * Image nodes are derived from {@link Node}. Whenever an image is shown
     * in a parent node, it cannot be shown in another node. Therefor it is
     * necessary to clone the image node.
     * <p>
     * If the images to use for menu items or buttons are not of type
     * {@link ImageView}, a cloning function must be provided.
     *
     * @param cloneImage The function to use.
     */
    public void setCloneImage(Function<Node, Node> cloneImage) {
        this.cloneImage = cloneImage;
    }

    /**
     * The property that holds the command text.
     *
     * @return The property that is bound to the controls.
     */
    public StringProperty commandTextProperty() {
        return commandText;
    }

    /**
     * The property that holds the accelerator.
     *
     * @return The property that is bound to the controls.
     */
    public ObjectProperty<KeyCombination> acceleratorProperty() {
        return accelerator;
    }

    /**
     * The property that holds the tooltip text.
     *
     * @return The property that is bound to the controls.
     */
    public StringProperty tooltipTextProperty() {
        return tooltipText;
    }

    /**
     * The property that holds the image to use for menu items.
     *
     * @return The property that is bound to the controls.
     */
    public ObjectProperty<Node> menuImageProperty() {
        return menuImage;
    }

    /**
     * The property that holds the image to use for buttons.
     *
     * @return The property that is bound to the controls.
     */
    public ObjectProperty<Node> buttonImageProperty() {
        return buttonImage;
    }

    /**
     * The property that holds the enabled status.
     *
     * @return The property that is bound to the controls.
     */
    public BooleanProperty disabledProperty() {
        return disabled;
    }

    /**
     * The property that holds the selection status.
     *
     * @return The property that is bound to the controls.
     */
    public BooleanProperty selectedProperty() {
        return selected;
    }

    /**
     * The property that holds the visibility.
     *
     * @return The property that is bound to the controls.
     */
    public BooleanProperty visibleProperty() {
        return visible;
    }

    /**
     * The property that holds the action handler.
     *
     * @return The property that is bound to the controls.
     */
    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onAction;
    }

    /**
     * Creates a menu item that visualizes the action.
     *
     * @return A list with exactly one item.
     */
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

    /**
     * Creates a button that visualizes the action.
     *
     * @return A list with exactly one button.
     */
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

    private static boolean shouldBind(int value, int bindNot) {
        return (value & bindNot) == 0;
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
