package de.ganzer.fx.actions;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;

import java.util.List;

public interface ActionItemBuilder {
    List<MenuItem> createMenuItems();
    List<Node> createButtons(boolean focusTraversable);
}
