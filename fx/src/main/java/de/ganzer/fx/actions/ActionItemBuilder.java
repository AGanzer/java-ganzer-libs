package de.ganzer.fx.actions;

import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.List;

/**
 * Must be implemented by all actions that shall be grouped into an instance
 * of {@link ActionGroup}
 */
public interface ActionItemBuilder {
    /**
     * Creates a  single menu that visualizes an action group.
     *
     * @return A single menu or {@code null} if the implementing class does not
     * support this.
     */
    default Menu createMenu() {
        return null;
    }

    /**
     * Creates the menu items that visualizes an action or an action group.
     *
     * @return A list with the created items.
     */
    List<MenuItem> createMenuItems();

    /**
     * Creates the buttons that visualizes an action or an action group.
     *
     * @return A list with the created buttons.
     */
    List<Node> createButtons(boolean focusTraversable);
}
