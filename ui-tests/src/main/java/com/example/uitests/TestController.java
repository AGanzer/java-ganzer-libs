package com.example.uitests;

import com.example.uitests.charts.ChartsController;
import de.ganzer.fx.dialogs.GAlert;
import de.ganzer.fx.dialogs.GButtonType;
import de.ganzer.fx.dialogs.GDialog;
import de.ganzer.fx.dialogs.ModalResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
        GDialog<TestDialogController, String> dialog = new GDialog<>(TestApplication.APP_TITLE, fxmlLoader, null);

        if (dialog.showAndWait() == ModalResult.OK)
            alert("Information", "OK Button Clicked");
    }

    public void showDialogAutoApply(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("test-dialog-view.fxml"));
        GDialog<TestDialogController, String> dialog = new GDialog<>(TestApplication.APP_TITLE, fxmlLoader, "");

        dialog.setApplyDataConsumer(data -> alert("Information", "Auto applied!"));

        dialog.showAndWait();
    }

    public void showDialogNonModal(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("test-dialog-view.fxml"));
        GDialog<TestDialogController, String> dialog = new GDialog<>(TestApplication.APP_TITLE, fxmlLoader, null);

        dialog.show(tabs.getScene().getWindow());
    }

    public void showDialogWithoutDecoration(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("test-dialog-view.fxml"));
        GDialog<TestDialogController, String> dialog = new GDialog<>(TestApplication.APP_TITLE, fxmlLoader, null);

        dialog.setStyle(StageStyle.UNDECORATED);
        dialog.showAndWait();
    }

    public void showDialogWithoutParent(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("test-dialog-view.fxml"));
        GDialog<TestDialogController, String> dialog = new GDialog<>(TestApplication.APP_TITLE, fxmlLoader, null);

        dialog.show();
    }

    public void showDialogWithApplyButton(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("test-dialog-view.fxml"));
        GDialog<TestDialogController, String> dialog = new GDialog<>(TestApplication.APP_TITLE, fxmlLoader, "Input some text");

        dialog.setApplyDataConsumer(data -> alert("Apply clicked!", "New text: " + data));

        dialog.show();
    }

    public void gAlertDialogClicked(ActionEvent ignored) {
        GAlert gAlert = new GAlert(Alert.AlertType.CONFIRMATION);
        gAlert.setTitle("Dialogs");
        gAlert.setHeaderText("Confirmation");
        gAlert.setContentText("Something went wrong.");
        gAlert.setExpandableContentText("Here are the details about the error ...");
        gAlert.setButtons(ButtonType.YES, GButtonType.YES_TO_ALL, ButtonType.NO, GButtonType.NO_TO_ALL, ButtonType.CANCEL);

        int result = gAlert.showAndWait(tabs.getScene().getWindow());

        alert("Information", "GAlert closed with: " + translate(result));
    }

    private String translate(int button) {
        switch (button) {
            case ModalResult.CANCEL:
                return "Cancel";

            case ModalResult.YES:
                return "Yes";

            case ModalResult.NO:
                return "No";

            case ModalResult.YES_TO_ALL:
                return "Yes to all";

            case ModalResult.NO_TO_ALL:
                return "No to all";

            default:
                return "Unknown";
        }
    }

    private void alert(String header, String message) {
        GAlert alert = new GAlert(Alert.AlertType.INFORMATION);
        alert.setTitle(TestApplication.APP_TITLE);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public void showCharts(ActionEvent ignored) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChartsController.class.getResource("charts-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();

        stage.setTitle("Charts");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}