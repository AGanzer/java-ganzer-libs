package com.example.uitests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class TestApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TestApplication.class.getResource("test-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Manual UI-Tests (ganzer-libs)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void alert(String message) {
        var a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Manual UI-Tests (ganzer-libs)");
        a.setContentText(message);

        a.showAndWait();
    }

    public static Optional<ButtonType> alert(String message, ButtonType... buttons) {
        var a = new Alert(Alert.AlertType.CONFIRMATION, message, buttons);
        a.setTitle("Manual UI-Tests (ganzer-libs)");

        return a.showAndWait();
    }
}