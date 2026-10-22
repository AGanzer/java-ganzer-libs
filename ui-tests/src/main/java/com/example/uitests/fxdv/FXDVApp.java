package com.example.uitests.fxdv;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXDVApp extends Application {
    public static final String TITLE = "FX Test Application";

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new MainView()));
        primaryStage.setTitle(TITLE);
        primaryStage.show();
    }
}
