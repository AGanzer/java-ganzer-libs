package com.example.uitests;

import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorException;
import de.ganzer.fx.validation.ValidatorTextFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CopyTestController implements TestProvider {
    public TextField sourcePath;
    public TextField targetPath;
    public TextArea output;
    public ProgressBar progressBar;
    public Label progressLabel;

    @FXML
    private void initialize() {
        new ValidatorTextFormatter(new Validator(), sourcePath);
        new ValidatorTextFormatter(new Validator(), targetPath);
    }

    @Override
    public void test() {
        if (validate(sourcePath) && validate(targetPath)) {
            // TODO: FileCopy
        }
    }

    public void chooseSourceClicked(ActionEvent actionEvent) {

    }

    public void chooseTargetClicked(ActionEvent actionEvent) {

    }

    private boolean validate(TextField field) {
        try {
            ((ValidatorTextFormatter)field.getTextFormatter()).validate();
        } catch (ValidatorException e) {
            TestApplication.alert(e.getMessage());

            field.requestFocus();
            field.selectAll();

            return false;
        }

        return true;
    }
}
