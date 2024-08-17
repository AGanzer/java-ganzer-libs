package de.ganzer.swing.actions;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * The {@code GAction} class provides a simplification to create menus and
 * buttons and to handle the status of the controls that are linked with
 * this action.
 * <p>
 * The following example shows how the {@code GAction} classes can be used
 * to create menus and toolbar buttons:
 * {@code
public class MainFrame extends JFrame {
    GActionGroup mainMenu;
    GActionGroup fileMenu;
    GActionGroup buttonsMenu;

    @Override
    protected void frameInit() {
        super.frameInit();

        setTitle("Swing Application");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initLayout();
        initActions();
        initMenu();
        initToolBar();
    }

    private void initLayout() {
        var pane = getContentPane();
        pane.setLayout(new BorderLayout());
    }

    private void initActions() {
        mainMenu = new GActionGroup().addAll(
                fileMenu = new GActionGroup("File").addAll(
                        new GAction("Exit")
                                .accelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK))
                                .onAction(this::onExit)
                ),
                buttonsMenu = new GActionGroup("Buttons").addAll(
                        new GAction("Any Option")
                                .selectable(true)
                                .onAction(this::onAnyOption),
                        new GAction("Another Option")
                                .selectable(true)
                                .selected(true)
                                .onAction(this::onAnotherOption),
                        new GSeparatorAction(),
                        new GToggleActionGroup()
                                .onSelectedActionChanged(this::onChooseChanged)
                                .addAll(
                                        new GAction("Choose 1")
                                                .exclusivelySelectable(true)
                                                .selected(true),
                                        new GAction("Choose 2")
                                                .exclusivelySelectable(true),
                                        new GAction("Choose 3")
                                                .exclusivelySelectable(true)
                                )
                )
        );
    }

    private void onAnyOption(ActionEvent event) {
        System.out.format(
                "Any Option is %s!\n",
                event.getSource().isSelected() ? "selected" : "deselected");
    }

    private void onAnotherOption(ActionEvent event) {
        System.out.format(
            "Another Option is %s!\n",
            event.getSource().isSelected() ? "selected" : "deselected");
    }

    private void onChooseChanged(GSelectedActionChangedEvent event) {
        System.out.format(
                "%s action is selected!\n",
                event.getSelectedAction() == null ? "No" : event.getSelectedAction().getName());
    }

    private void onExit(ActionEvent event) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void initMenu() {
        var menuBar = new JMenuBar();
        mainMenu.addMenus(menuBar);
        getContentPane().add(menuBar, BorderLayout.PAGE_START);
    }

    private void initToolBar() {
        var toolBar = new JToolBar();
        fileMenu.addButtons(toolBar, false);
        toolBar.addSeparator();
        buttonsMenu.addButtons(toolBar, false);
        getContentPane().add(toolBar, BorderLayout.PAGE_END);
    }
}
 * }
 */
@SuppressWarnings("unused")
public class GAction extends AbstractAction implements GActionItemBuilder {
    private final EventListenerList actionListeners = new EventListenerList();
    private String command;
    private boolean selectable;
    private boolean exclusivelySelectable;

    /**
     * {@inheritDoc}
     */
    public GAction() {
        putValue(SELECTED_KEY, false);
    }

    /**
     * {@inheritDoc}
     */
    public GAction(String name) {
        super(name);
        putValue(SELECTED_KEY, false);
    }

    /**
     * {@inheritDoc}
     */
    public GAction(String name, Icon icon) {
        super(name, icon);
    }

    /**
     * Calls {@link #putValue}{@code (NAME, name)}.
     *
     * @param name The name to set.
     *
     * @return {@code this}.
     */
    public GAction name(String name) {
        putValue(NAME, name);
        return this;
    }

    /**
     * Gets the name of the action.
     *
     * @return The name or {@code null} if it is not set.
     */
    public String getName() {
        return (String) getValue(NAME);
    }

    /**
     * Sets the action command.
     * <p>
     * NOTE: The action system in Swing does not provide action commands for
     * actions (because using actions does make it unnecessary). When buttons
     * or menu items are created by this action, the button's and item's action
     * commands are automatically set. If the buttons and items are already
     * created or created by applying this action, the item's and button's
     * action commands must be explicitly set to this action's command.
     * <p>
     * If this is set after buttons or menu items are created, the already
     * created component's action commands won't be updated.
     *
     * @param command The command to set.
     *
     * @return {@code this}.
     */
    public GAction command(String command) {
        this.command = command;
        return this;
    }

    /**
     * Gets the action command.
     *
     * @return The set command or {@code null} if no command is set.
     *
     * @see #command(String)
     */
    public String getCommand() {
        return command;
    }

    /**
     * Calls {@link #putValue}{@code (SMALL_ICON, icon)}.
     *
     * @param icon The icon to set.
     *
     * @return {@code this}.
     */
    public GAction smallIcon(Icon icon) {
        putValue(SMALL_ICON, icon);
        return this;
    }

    /**
     * Gets the small icon of the action.
     *
     * @return The icon or {@code null} if it is not set.
     */
    public Icon getSmallIcon() {
        return (Icon) getValue(SMALL_ICON);
    }

    /**
     * Calls {@link #putValue}{@code (LARGE_ICON_KEY, icon)}.
     *
     * @param icon The icon to set.
     *
     * @return {@code this}.
     */
    public GAction largeIcon(Icon icon) {
        putValue(LARGE_ICON_KEY, icon);
        return this;
    }

    /**
     * Gets the large icon of the action.
     *
     * @return The icon or {@code null} if it is not set.
     */
    public Icon getLargeIcon() {
        return (Icon) getValue(LARGE_ICON_KEY);
    }

    /**
     * Calls {@link #putValue}{@code (ACCELERATOR_KEY, keyStroke)}.
     *
     * @param keyStroke The accelerator to set.
     *
     * @return {@code this}.
     */
    public GAction accelerator(KeyStroke keyStroke) {
        putValue(ACCELERATOR_KEY, keyStroke);
        return this;
    }

    /**
     * Gets the accelerator of the action.
     *
     * @return The accelerator or {@code null} if it is not set.
     */
    public KeyStroke getAccelerator() {
        return (KeyStroke) getValue(ACCELERATOR_KEY);
    }

    /**
     * Calls {@link #putValue}{@code (MNEMONIC_KEY, keyCode)}.
     *
     * @param keyCode The mnemonic to set.
     *
     * @return {@code this}.
     */
    public GAction mnemonic(Integer keyCode) {
        putValue(MNEMONIC_KEY, keyCode);
        return this;
    }

    /**
     * Gets the mnemonic of the action.
     *
     * @return The mnemonic or {@code null} if it is not set.
     */
    public Integer getMnemonic() {
        return (Integer) getValue(MNEMONIC_KEY);
    }

    /**
     * Calls {@link #putValue}{@code (DISPLAYED_MNEMONIC_INDEX_KEY, index)}.
     *
     * @param index The index to set.
     *
     * @return {@code this}.
     */
    public GAction displayedMnemonicIndex(Integer index) {
        putValue(DISPLAYED_MNEMONIC_INDEX_KEY, index);
        return this;
    }

    /**
     * Gets the displayed mnemonic index of the action.
     *
     * @return The index or {@code null} if it is not set.
     */
    public Integer getDisplayedMnemonicIndex() {
        return (Integer) getValue(DISPLAYED_MNEMONIC_INDEX_KEY);
    }

    /**
     * Calls {@link #putValue}{@code (SHORT_DESCRIPTION, description)}.
     *
     * @param description The description to set.
     *
     * @return {@code this}.
     */
    public GAction shortDescription(String description) {
        putValue(SHORT_DESCRIPTION, description);
        return this;
    }

    /**
     * Gets the short description of the action.
     *
     * @return The description or {@code null} if it is not set.
     */
    public String getShortDescription() {
        return (String) getValue(SHORT_DESCRIPTION);
    }

    /**
     * Calls {@link #putValue}{@code (LONG_DESCRIPTION, description)}.
     *
     * @param description The description to set.
     *
     * @return {@code this}.
     */
    public GAction longDescription(String description) {
        putValue(LONG_DESCRIPTION, description);
        return this;
    }

    /**
     * Gets the long description of the action.
     *
     * @return The description or {@code null} if it is not set.
     */
    public String getLongDescription() {
        return (String) getValue(LONG_DESCRIPTION);
    }

    /**
     * Sets a value that indicates whether the action is selectable.
     * <p>
     * This is used only for the creation of buttons and menu items.
     *
     * @param selectable {@code true} to make the action selectable.
     *
     * @return {@code this}.
     *
     * @see #createMenuItem()
     * @see #createButton(int)
     */
    public GAction selectable(boolean selectable) {
        this.selectable = selectable;
        return this;
    }

    /**
     * Gets a value that indicates whether the action is selectable.
     *
     * @return {@code true} if the action is selectable; otherwise,
     *         {@code false}.
     *
     * @see #selectable(boolean)
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * Sets a value that indicates whether the action is exclusively selectable.
     * <p>
     * This is used only for the creation of buttons and menu items.
     *
     * @param selectable {@code true} to make the action exclusively selectable.
     *
     * @return {@code this}.
     *
     * @see #createMenuItem()
     * @see #createButton(int)
     */
    public GAction exclusivelySelectable(boolean selectable) {
        this.exclusivelySelectable = selectable;
        return this;
    }

    /**
     * Gets a value that indicates whether the action is exclusively selectable.
     *
     * @return {@code true} if the action is exclusively selectable; otherwise,
     *         {@code false}.
     *
     * @see #exclusivelySelectable(boolean)
     */
    public boolean isExclusivelySelectable() {
        return exclusivelySelectable;
    }

    /**
     * Calls {@link #putValue}{@code (SELECTED_KEY, selected)}.
     *
     * @param selected The selection status to set.
     *
     * @return {@code this}.
     */
    public GAction selected(boolean selected) {
        putValue(SELECTED_KEY, selected);
        return this;
    }

    /**
     * Gets the selection status of the action.
     *
     * @return The status.
     */
    public boolean isSelected() {
        Boolean value = (Boolean) getValue(SELECTED_KEY);
        return value != null && value;
    }

    /**
     * Calls {@link Action#setEnabled(boolean)}.
     *
     * @param enabled The enabled status to set.
     *
     * @return {@code this}.
     */
    public GAction enabled(boolean enabled) {
        setEnabled(enabled);
        return this;
    }

    /**
     * Adds the specified action listener that is called when this action is
     * performed.
     *
     * @param listener The listener to add.
     *
     * @return {@code this}.
     *
     * @throws NullPointerException {@code listener} is {@code null}.
     */
    public GAction onAction(ActionListener listener) {
        addActionListener(listener);
        return this;
    }

    /**
     * Adds the specified action listener that is called when this action is
     * performed.
     *
     * @param listener The listener to add.
     *
     * @throws NullPointerException {@code listener} is {@code null}.
     */
    public void addActionListener(ActionListener listener) {
        Objects.requireNonNull(listener);
        actionListeners.add(ActionListener.class, listener);
    }

    /**
     * Gets the installed action listeners to be called when the action is
     * performed.
     *
     * @return The listeners. This may be empty if no listener is installed.
     */
    public ActionListener[] getActionListeners() {
        return actionListeners.getListeners(ActionListener.class);
    }

    /**
     * Removes the specified listener from the action listener list.
     *
     * @param listener The listener to remove.
     */
    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(ActionListener.class, listener);
    }

    /**
     * This implementation calls the installed action listeners.
     *
     * @param event the event to be processed.
     *
     * @throws NullPointerException {@code event} is {@code null}.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Objects.requireNonNull(event);

        Object[] listeners = actionListeners.getListenerList();
        ActionEvent ex = null;

        // Process the listeners last to first, like AbstractButton does:
        //
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                // Lazily create the event:
                //
                if (ex == null) {
                    String actionCommand = event.getActionCommand();

                    if(actionCommand == null)
                        actionCommand = command;

                    ex = new ActionEvent(this, ActionEvent.ACTION_FIRST, actionCommand);
                }

                ((ActionListener)listeners[i + 1]).actionPerformed(ex);
            }
        }
    }

    /**
     * Creates a single menu item depending on the select ability of the action.
     *
     * @return The created menu item. This is {@link JRadioButtonMenuItem} if
     *         {@link #isExclusivelySelectable()} is {@code true}. This is
     *         {@link JCheckBoxMenuItem} if {@link #isSelectable()} is
     *         {@code true}. In all other cases it is {@link JMenuItem}.
     */
    @Override
    public JMenuItem createMenuItem() {
        JMenuItem item;

        if (exclusivelySelectable)
            item = new JRadioButtonMenuItem(this);
        else if (selectable)
            item = new JCheckBoxMenuItem(this);
        else
            item = new JMenuItem(this);

        item.setActionCommand(command);

        return item;
    }

    /**
     * The creates and inserts a single menu item into the specified target.
     * <p>
     * The created menu item is {@link JRadioButtonMenuItem} if
     * {@link #isExclusivelySelectable()} is {@code true}. It is
     * {@link JCheckBoxMenuItem} if {@link #isSelectable()} is {@code true}.
     * In all other cases it is {@link JMenuItem}.
     *
     * @param target The menu where to insert the separator.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addMenuItems(JMenu target) {
        Objects.requireNonNull(target);
        target.add(createMenuItem());
    }

    /**
     * The creates and inserts a single menu item into the specified target.
     * <p>
     * The created menu item is {@link JRadioButtonMenuItem} if
     * {@link #isExclusivelySelectable()} is {@code true}. It is
     * {@link JCheckBoxMenuItem} if {@link #isSelectable()} is {@code true}.
     * In all other cases it is {@link JMenuItem}.
     *
     * @param target The menu where to insert the separator.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addMenuItems(JPopupMenu target) {
        Objects.requireNonNull(target);
        target.add(createMenuItem());
    }

    /**
     * Creates a single button depending on the select ability of the action.
     *
     * @param options The options to use. This is any combination of the
     *        {@link CreateOptions} values.
     *
     * @return The created button. This is {@link JToggleButton} if
     *         {@link #isExclusivelySelectable()} or {@link #isSelectable()} is
     *         {@code true}. Otherwise, it is {@link JButton}. If any image is
     *         set, the button's action text is hidden.
     */
    @Override
    public AbstractButton createButton(int options) {
        AbstractButton button;

        if (exclusivelySelectable || selectable)
            button = new JToggleButton(this);
        else
            button = new JButton(this);

        button.setActionCommand(command);
        button.setFocusable(CreateOptions.isSet(options, CreateOptions.FOCUSABLE));
        button.setHideActionText(shouldHideText(options));
        button.setVerticalTextPosition(getVerticalTextPosition(options));
        button.setHorizontalTextPosition(getHorizontalTextPosition(options));
        button.setBorderPainted(!CreateOptions.isSet(options, CreateOptions.NO_BORDER));

        return button;
    }

    /**
     * The creates and inserts a single button into the specified target.
     * <p>
     * The created button is {@link JRadioButton} if
     * {@link #isExclusivelySelectable()} is {@code true}. It is {@link JCheckBox}
     * if {@link #isSelectable()} is {@code true}. In all other cases it is
     * {@link JButton}. If any image is set, the buttons action texts are hidden.
     *
     * @param target The toolbar where to insert the separator.
     * @param options The options to use. This is any combination of the
     *        {@link CreateOptions} values.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addButtons(JToolBar target, int options) {
        Objects.requireNonNull(target);
        target.add(createButton(options));
    }

    protected final boolean shouldHideText(int options) {
        if (CreateOptions.isSet(options, CreateOptions.SHOW_TEXT))
            return false;

        return getSmallIcon() != null || getLargeIcon() != null;
    }

    protected final int getVerticalTextPosition(int options) {
        return !CreateOptions.isSet(options, CreateOptions.IMAGE_LEADING)
                ? SwingConstants.BOTTOM
                : SwingConstants.CENTER;
    }

    protected final int getHorizontalTextPosition(int options) {
        return !CreateOptions.isSet(options, CreateOptions.IMAGE_LEADING)
                ? SwingConstants.CENTER
                : SwingConstants.TRAILING;
    }
}
