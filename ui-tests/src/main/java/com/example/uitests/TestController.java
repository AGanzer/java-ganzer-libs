package com.example.uitests;

import de.ganzer.fx.dialogs.GDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.StageStyle;

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

    public void startTestClicked(ActionEvent ignored) {
        TestProvider provider = (TestProvider)tabs.getSelectionModel().getSelectedItem().getContent().getUserData();
        provider.test();
    }

    public void exitApplication(ActionEvent ignored) {
        tabs.getScene().getWindow().hide();
    }

    public void showDialogModal(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("test-dialog-view.fxml"));
        GDialog<TestDialogController, Object> dialog = new GDialog<>(fxmlLoader, null);

        int result = dialog.showAndWait(tabs.getScene().getWindow());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(TestApplication.APP_TITLE);
        alert.setHeaderText("Result of Dialog");
        alert.setContentText("Result: " + result);

        alert.showAndWait();
    }

    public void showDialogNonModal(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("test-dialog-view.fxml"));
        GDialog<TestDialogController, Object> dialog = new GDialog<>(fxmlLoader, null);

        dialog.show(tabs.getScene().getWindow());
    }

    public void showDialogWithoutDecoration(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("test-dialog-view.fxml"));
        GDialog<TestDialogController, Object> dialog = new GDialog<>(fxmlLoader, null);

        dialog.setStyle(StageStyle.UNDECORATED);
        dialog.showAndWait(tabs.getScene().getWindow());
    }

    public void showDialogWithoutParent(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("test-dialog-view.fxml"));
        GDialog<TestDialogController, Object> dialog = new GDialog<>(fxmlLoader, null);

        dialog.show(null);
    }
}