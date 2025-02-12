package de.ganzer.swing.actions;

import de.ganzer.swing.controls.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Groups several actions to enable the creation of menus and menu items.
 * <p>
 * See {@link GAction} for an example.
 */
public class GActionGroup extends GAction implements Iterable<GActionItemBuilder> {
    private final List<GActionItemBuilder> actions = new ArrayList<>();

    /**
     * Creates an empty action group with no menu text.
     */
    public GActionGroup() {
    }

    /**
     * Creates an empty action group with the specified menu text.
     *
     * @param name The text of the menu to set.
     */
    public GActionGroup(String name) {
        super(name);
    }

    /**
     * Adds the specified actions into this group.
     *
     * @param actions The actions to add.
     *
     * @return {@code this}.
     *
     * @throws NullPointerException {@code actions} is {@code null} or one of
     *         the elements in {@code actions} is {@code null}.
     */
    public GActionGroup addAll(GActionItemBuilder... actions) {
        Objects.requireNonNull(actions, "actions must not be null.");

        for (GActionItemBuilder action: actions) {
            Objects.requireNonNull(action, "actions must not contain null values.");
            this.actions.add(action);
        }

        return this;
    }

    /**
     * Calls {@link GAction#name(String)}.
     *
     * @param name The name to set.
     *
     * @return {@code this}.
     */
    @Override
    public GActionGroup name(String name) {
        super.name(name);
        return this;
    }

    /**
     * Calls {@link GAction#command(String)}.
     *
     * @param command The command to set.
     *
     * @return {@code this}.
     */
    @Override
    public GActionGroup command(String command) {
        super.command(command);
        return this;
    }

    /**
     * Calls {@link GAction#largeIcon(Icon)}.
     *
     * @param icon The icon to set.
     *
     * @return {@code this}.
     */
    @Override
    public GActionGroup largeIcon(Icon icon) {
        super.largeIcon(icon);
        return this;
    }

    /**
     * Calls {@link GAction#mnemonic(Integer)}.
     *
     * @param keyCode The mnemonic to set.
     *
     * @return {@code this}.
     */
    @Override
    public GActionGroup mnemonic(Integer keyCode) {
        super.mnemonic(keyCode);
        return this;
    }

    /**
     * Calls {@link GAction#displayedMnemonicIndex(Integer)}.
     *
     * @param index The index to set.
     *
     * @return {@code this}.
     */
    @Override
    public GActionGroup displayedMnemonicIndex(Integer index) {
        super.displayedMnemonicIndex(index);
        return this;
    }

    /**
     * Calls {@link GAction#shortDescription(String)}.
     *
     * @param description The description to set.
     *
     * @return {@code this}.
     */
    @Override
    public GActionGroup shortDescription(String description) {
        super.shortDescription(description);
        return this;
    }

    /**
     * Calls {@link GAction#longDescription(String)}.
     *
     * @param description The description to set.
     *
     * @return {@code this}.
     */
    @Override
    public GActionGroup longDescription(String description) {
        super.longDescription(description);
        return this;
    }

    /**
     * Calls {@link GAction#visible(boolean)}.
     *
     * @param visible The visibility to set.
     *
     * @return {@code this}.
     */
    @Override
    public GActionGroup visible(boolean visible) {
        super.visible(visible);
        return this;
    }

    /**
     * Calls {@link Action#setEnabled(boolean)}.
     *
     * @param enabled The enabled status to set.
     *
     * @return {@code this}.
     */
    @Override
    public GActionGroup enabled(boolean enabled) {
        super.enabled(enabled);
        return this;
    }

    /**
     * Calls {@link GAction#onAction(ActionListener)}.
     *
     * @param listener The listener to add.
     *
     * @return {@code this}.
     *
     * @throws NullPointerException {@code listener} is {@code null}.
     */
    @Override
    public GActionGroup onAction(ActionListener listener) {
        super.onAction(listener);
        return this;
    }

    /**
     * Creates a single menu that visualizes an action group.
     *
     * @return A single menu that contains all actions and action groups of this
     *         group.
     */
    @Override
    public JMenu createMenu() {
        GMenu menu = new GMenu(this);
        addMenuItems(menu);

        return menu;
    }

    /**
     * Creates the menus that visualizes this action group and inserts them into
     * the specified target.
     * <p>
     * This implementation does only create menus for items that are implementing
     * {@link GActionItemBuilder#createMenu()}.
     *
     * @param target The menu bar where to insert the menus.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addMenus(JMenuBar target) {
        Objects.requireNonNull(target, "target must not be null.");

        for (GActionItemBuilder builder : actions) {
            JMenu menu = builder.createMenu();

            if (menu != null)
                target.add(menu);
        }
    }

    /**
     * Creates the menu items that visualizes this action group and inserts them
     * into the specified target.
     * <p>
     * This implementation calls {@link #createMenu()} for all items that are
     * of type {@link GActionGroup}. For all others,
     * {@link GActionItemBuilder#addMenuItems(JMenu)} is called.
     *
     * @param target The menu where to insert the menu items.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addMenuItems(JMenu target) {
        Objects.requireNonNull(target, "target must not be null.");

        for (GActionItemBuilder builder : actions) {
            if (builder instanceof GActionGroup)
                target.add(builder.createMenu());
            else
                builder.addMenuItems(target);
        }
    }

    /**
     * Creates the menu items that visualizes this action group and inserts them
     * into the specified target.
     * <p>
     * This implementation calls {@link #createMenu()} for all items that are
     * of type {@link GActionGroup}. For all others,
     * {@link GActionItemBuilder#addMenuItems(JMenu)} is called.
     *
     * @param target The menu where to insert the menu items.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addMenuItems(JPopupMenu target) {
        for (GActionItemBuilder builder : actions) {
            if (builder instanceof GActionGroup)
                target.add(builder.createMenu());
            else if (builder instanceof GToggleActionGroup)
                builder.addMenuItems(target);
            else if (builder instanceof GSeparatorAction)
                target.addSeparator();
            else
                target.add(builder.createMenuItem());
        }
    }

    /**
     * Creates a single button with a popup menu that contains the actions of
     * this group.
     *
     * @param options The options to use. This is any combination of the
     *        {@link CreateOptions} values.
     *
     * @return The created button of type {@link JButton}.
     */
    @Override
    public AbstractButton createButton(int options) {
        JPopupMenu menu = createPopupMenu();
        AbstractButton button = super.createButton(options);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    menu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        return button;
    }

    /**
     * Creates the buttons that visualizes this action group and inserts them
     * into the specified target.
     * <p>
     * For each item of type {@link GActionGroup} a button with a popup menu is
     * created.
     *
     * @param target The toolbar where to insert the buttons.
     * @param options The options to use. This is any combination of the
     *        {@link CreateOptions} values.
     *
     * @throws NullPointerException {@code target} is {@code null}.
     */
    @Override
    public void addButtons(JToolBar target, int options) {
        Objects.requireNonNull(target, "target must not be null.");

        for (GActionItemBuilder builder : actions) {
            if (builder instanceof GActionGroup)
                target.add(builder.createButton(options));
            else
                builder.addButtons(target, options);
        }
    }

    private JPopupMenu createPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        addMenuItems(menu);

        return menu;
    }

    @Override
    public Iterator<GActionItemBuilder> iterator() {
        return actions.iterator();
    }
}
