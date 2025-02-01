package com.example.uitests.fx;

import com.example.uitests.fx.charts.ChartsController;
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
            FXMLLoader fxmlLoader = new FXMLLoader(FxTestApp.class.getResource(viewName));
            Parent parent = fxmlLoader.load();

            tab.setContent(parent);
        } catch (IOException e) {
            FxTestApp.alertError(String.format("Cannot load %s.", viewName));
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
        GDialog<TestDialogController, String> dialog = createDialog(null);

        if (dialog.showAndWait() == ModalResult.OK)
            FxTestApp.alertInfo("OK Button Clicked");
    }

    public void showDialogAutoApply(ActionEvent ignored) throws IOException {
        GDialog<TestDialogController, String> dialog = createDialog("");
        dialog.setApplyDataConsumer(data -> FxTestApp.alertInfo("Automatically applied!"));

        dialog.showAndWait();
    }

    public void showDialogNonModal(ActionEvent ignored) throws IOException {
        GDialog<TestDialogController, String> dialog = createDialog(null);
        dialog.show(tabs.getScene().getWindow());
    }

    public void showDialogWithoutDecoration(ActionEvent ignored) throws IOException {
        GDialog<TestDialogController, String> dialog = createDialog(null);

        dialog.setStyle(StageStyle.UNDECORATED);
        dialog.showAndWait();
    }

    public void showDialogWithoutParent(ActionEvent ignored) throws IOException {
        GDialog<TestDialogController, String> dialog = createDialog(null);
        dialog.show();
    }

    public void showDialogWithApplyButton(ActionEvent ignored) throws IOException {
        GDialog<TestDialogController, String> dialog = createDialog("Input some text");
        dialog.setApplyDataConsumer(data -> FxTestApp.alertInfo("Apply clicked!\n\nNew text: " + data));

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

        FxTestApp.alertInfo("GAlert closed with: " + translate(result));
    }

    private static GDialog<TestDialogController, String> createDialog(String data) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FxTestApp.class.getResource("test-dialog-view.fxml"));
        return new GDialog<>(FxTestApp.APP_TITLE, fxmlLoader, data);
    }

    private String translate(int button) {
        return switch (button) {
            case ModalResult.CANCEL -> "Cancel";
            case ModalResult.YES -> "Yes";
            case ModalResult.NO -> "No";
            case ModalResult.YES_TO_ALL -> "Yes to all";
            case ModalResult.NO_TO_ALL -> "No to all";
            default -> "Unknown";
        };
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