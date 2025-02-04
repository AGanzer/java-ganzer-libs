package com.example.uitests.fx;

import de.ganzer.fx.util.UISettings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class FxTestApp extends Application {
    public static final String APP_TITLE = "Manual UI-Tests (ganzer-libs)";

    private static final String UI_KEY_MAINFRAME = "mainframe";
    private static final UISettings uiSettings = new UISettings("fx-ui-tests", "0.0.1");

    public static UISettings getUiSettings() {
        return uiSettings;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FxTestApp.class.getResource("test-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(APP_TITLE);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> uiSettings.write(UI_KEY_MAINFRAME, stage));
        stage.show();

        uiSettings.apply(UI_KEY_MAINFRAME, stage);

        stage.show();
    }

    public static void main(String[] args) {
        uiSettings.load();
        launch();
        uiSettings.save();
    }

    public static void alertInfo(String message) {
        alert(Alert.AlertType.INFORMATION, message);
    }

    public static void alertError(String message) {
        alert(Alert.AlertType.ERROR, message);
    }

    public static Optional<ButtonType> alertConfirm(String message, ButtonType... buttons) {
        var a = new Alert(Alert.AlertType.CONFIRMATION, message, buttons);
        a.setTitle(APP_TITLE);

        return a.showAndWait();
    }

    private static void alert(Alert.AlertType type, String message) {
        var a = new Alert(type);
        a.setTitle(APP_TITLE);
        a.setHeaderText(null);
        a.setContentText(message);

        a.showAndWait();
    }
}