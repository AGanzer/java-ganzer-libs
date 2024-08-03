package de.ganzer.swing.actions;

import javax.swing.*;

/**
 * Must be implemented by all actions that shall be grouped into an instance
 * of {@link GActionGroup}
 */
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
	 *
	 * @param focusable Indicates whether the created button shall be focusable.
	 *
	 * @return A single button or {@code null} if the implementing class does
	 * not support this.
	 */
	default AbstractButton createButton(boolean focusable) {
		return null;
	}

	/**
	 * Creates the buttons that visualizes an action or an action group and
	 * inserts them into the specified target.
	 *
	 * @param target The toolbar where to insert the buttons.
	 * @param focusable Indicates whether the created button shall be focusable.
	 *
	 * @throws NullPointerException {@code target} is {@code null}.
	 */
	void addButtons(JToolBar target, boolean focusable);
}
