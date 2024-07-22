package de.ganzer.fx.actions;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;

import java.util.List;

/**
 * Provides an action the is created as a separator.
 */
@SuppressWarnings("unused")
public class SeparatorAction implements ActionItemBuilder {
    /**
     * Creates the separator.
     *
     * @return The created separator of type {@link SeparatorMenuItem}.
     */
    @Override
    public List<MenuItem> createMenuItems() {
        return List.of(new SeparatorMenuItem());
    }

    /**
     * Creates the separator.
     *
     * @return The created separator of type {@link Separator}.
     */
    @Override
    public List<Node> createButtons(boolean ignored1, ImageSize ignored2) {
        return List.of(new Separator());
    }
}
