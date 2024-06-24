package de.ganzer.fx.actions;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;

import java.util.List;

@SuppressWarnings("unused")
public class SeparatorAction implements ActionItemBuilder {
    @Override
    public List<MenuItem> createMenuItems() {
        return List.of(new SeparatorMenuItem());
    }

    @Override
    public List<Node> createButtons(boolean ignored) {
        return List.of(new Separator());
    }
}
