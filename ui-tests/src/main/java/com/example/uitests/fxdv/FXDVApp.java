package com.example.uitests.fxdv;

import com.example.uitests.fxdv.util.ImageProvider;
import com.example.uitests.fxdv.util.SVGProvider;
import de.ganzer.core.OS;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.Taskbar;

public class FXDVApp extends Application {
    public static final String TITLE = "FX Test Application";

    @Override
    public void start(Stage primaryStage) throws Exception {
        if (OS.isMac()) {
            System.setProperty("apple.awt.application.name", TITLE);
            Taskbar.getTaskbar().setIconImage(com.example.uitests.swing.SVGProvider.get("multimeter_analog", 64).getImage());
        }

//        FxmlKit.enableDevelopmentMode();

        primaryStage.getIcons().addAll(ImageProvider.getApplicationIcons());

        primaryStage.setScene(new Scene(new MainView()));
        primaryStage.setTitle(TITLE);
        primaryStage.show();
    }
}
