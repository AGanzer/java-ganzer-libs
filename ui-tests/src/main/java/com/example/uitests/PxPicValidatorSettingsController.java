package com.example.uitests;

import de.ganzer.core.validation.PxPicValidator;
import de.ganzer.core.validation.Validator;
import de.ganzer.core.validation.ValidatorExceptionRef;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PxPicValidatorSettingsController implements TestValidatorController {
    //region fields
    private PxPicValidator testValidator;
    //endregion

    //region controls
    public TextField picture;
    //endregion

    //region properties
    private final StringProperty pictureInfo = new SimpleStringProperty();

    public String getPictureInfo() {
        return pictureInfo.get();
    }

    public StringProperty pictureInfoProperty() {
        return pictureInfo;
    }

    public void setPictureInfo(String pictureInfo) {
        this.pictureInfo.set(pictureInfo);
    }
    //endregion

    //region getter/setter
    public Validator getTestValidator() {
        return testValidator;
    }

    public void setTestValidator(Validator testValidator) {
        this.testValidator = (PxPicValidator)testValidator;
    }
    //endregion

    @Override
    public boolean validateSettings(ValidatorExceptionRef ref) {
        return true;
    }

    //region init
    @FXML
    private void initialize() {
        initializeListeners();
        updatePictureHint(true);
    }

    private void initializeListeners() {
        picture.textProperty().addListener((p, o, n) -> {
            var pic = picture.getText();
            var ok = testValidator.checkSyntax(pic);

            if (ok)
                testValidator.setPicture(pic);

            updatePictureHint(ok);
        });
    }

    private void updatePictureHint(boolean valid) {
        setPictureInfo(valid ? "The picture is valid." : "The picture is invalid yet.");
    }
    //endregion
}
