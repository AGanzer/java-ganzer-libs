package com.example.uitests.fxdv;

import de.ganzer.fx.actions.SeparatorAction;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController {
    @FXML private TabPane tabPane;
    @FXML private MenuBar menuBar;
//    @FXML private URL location;
//    @FXML private ResourceBundle resources;

    @FXML
    private void initialize() {
        menuBar.getMenus().addAll(
                Actions.fileActions.createMenu(),
                Actions.editActions.createMenu(),
                Actions.windowActions.createMenu(),
                Actions.extrasActions.createMenu(),
                Actions.helpActions.createMenu());
        menuBar.setUseSystemMenuBar(true);
    }
}
