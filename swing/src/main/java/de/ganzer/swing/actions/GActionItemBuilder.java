package de.ganzer.swing.actions;

import javax.swing.*;

/**
 * Must be implemented by all actions that shall be grouped into an instance
 * of {@link GActionGroup}
 */
@SuppressWarnings("unused")
public interface GActionItemBuilder {
	/**
	 * Creates a single menu that visualizes an action group.
	 *
	 * @return A single menu or {@code null} if the implementing class does not
	 * support this.
	 */
	default JMenu createMenu() {
		return null;
	}

	/**
	 * Creates the menus that visualizes an action group and inserts them into
	 * the specified target.
	 * <p>
	 * This does nothing if the implementing class does not support this.
	 *
	 * @param target The menu bar where to insert the menus.
	 *
	 * @throws NullPointerException {@code target} is {@code null}.
	 */
	default void addMenus(JMenuBar target) {
	}

	/**
	 * Creates the menu items that visualizes an action or an action group and
	 * inserts them into the specified target.
	 *
	 * @param target The menu where to insert the items.
	 *
	 * @throws NullPointerException {@code target} is {@code null}.
	 */
	void addMenuItems(JMenu target);

	/**
	 * Creates the menu items that visualizes an action or an action group and
	 * inserts them into the specified target.
	 *
	 * @param target The menu where to insert the items.
	 *
	 * @throws NullPointerException {@code target} is {@code null}.
	 */
	void addMenuItems(JPopupMenu target);

	/**
	 * Creates a single menu item that visualizes an action.
	 *
	 * @return A single menu item or {@code null} if the implementing class does
	 * not support this.
	 */
	default JMenuItem createMenuItem() {
		return null;
	}

	/**
	 * Creates a single button that visualizes an action.
	 * <p>
	 * This implementation calls {@link #createButton(int)} with
	 * {@link CreateOptions#DEFAULT}.
	 *
	 * @return A single button or {@code null} if the implementing class does
	 * not support this.
	 */
	default AbstractButton createButton() {
		return createButton(CreateOptions.DEFAULT);
	}

	/**
	 * Creates a single button that visualizes an action.
	 *
	 * @param options The options to use. This is any combination of the
	 *        {@link CreateOptions} values.
	 *
	 * @return A single button or {@code null} if the implementing class does
	 * not support this.
	 */
	default AbstractButton createButton(int options) {
		return null;
	}

	/**
	 * Creates the buttons that visualizes an action or an action group and
	 * inserts them into the specified target.
	 * <p>
	 * This implementation calls {@link #addButtons(JToolBar, int)} with
	 * {@link CreateOptions#DEFAULT}.
	 *
	 * @param target The toolbar where to insert the buttons.
	 *
	 * @throws NullPointerException {@code target} is {@code null}.
	 */
	default void addButtons(JToolBar target) {
		addButtons(target, CreateOptions.DEFAULT);
	}

	/**
	 * Creates the buttons that visualizes an action or an action group and
	 * inserts them into the specified target.
	 *
	 * @param target The toolbar where to insert the buttons.
	 * @param options The options to use. This is any combination of the
	 *        {@link CreateOptions} values.
	 *
	 * @throws NullPointerException {@code target} is {@code null}.
	 */
	void addButtons(JToolBar target, int options);
}
