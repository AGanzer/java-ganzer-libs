package com.example.uitests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
}