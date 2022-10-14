package com.example.uitests;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;

public class TestController {

    public TabPane tabs;
    public Tab validatorTab;
    public Tab copyTab;

    @FXML
    private void initialize() {
        initializeTab(validatorTab, "validator-test-view.fxml");
        initializeTab(copyTab, "copy-test-view.fxml");
    }

    private void initializeTab(Tab tab, String viewName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource(viewName));
            Parent parent = fxmlLoader.load();

            tab.setContent(parent);
        } catch (IOException e) {
            TestApplication.alert(String.format("Cannot load %s.", viewName));
        }
    }

    public void startTestClicked(ActionEvent actionEvent) {
        TestProvider provider = (TestProvider)tabs.getSelectionModel().getSelectedItem().getContent().getUserData();
        provider.test();
    }
}