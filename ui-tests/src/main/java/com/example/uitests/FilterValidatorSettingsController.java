package com.example.uitests;

import de.ganzer.core.validation.FilterValidator;
import de.ganzer.core.validation.NumberValidator;
import de.ganzer.core.validation.Validator;
import de.ganzer.fx.validation.ValidatorTextFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.text.ParseException;

public class FilterValidatorSettingsController implements TestValidatorController {
    //region fields
    private final NumberValidator inputLengthValidator = new NumberValidator(0, Short.MAX_VALUE);
    private FilterValidator testValidator;
    //endregion

    //region controls
    public TextField validMask;
    public TextField invalidMask;
    public TextField minInputLength;
    public TextField maxInputLength;
    //endregion

    //region getter/setter
    public Validator getTestValidator() {
        return testValidator;
    }

    public void setTestValidator(Validator testValidator) {
        this.testValidator = (FilterValidator)testValidator;

        minInputLength.setText(String.format("%,d", this.testValidator.getMinLength()));
        maxInputLength.setText(String.format("%,d", this.testValidator.getMaxLength()));
    }
    //endregion

    //region init
    @FXML
    private void initialize() {
        new ValidatorTextFormatter(inputLengthValidator, minInputLength);
        new ValidatorTextFormatter(inputLengthValidator, maxInputLength);

        initializeListeners();
    }

    @SuppressWarnings("DuplicatedCode")
    private void initializeListeners() {
        validMask.textProperty().addListener((p, o, n) -> testValidator.setValidMask(validMask.getText()));
        invalidMask.textProperty().addListener((p, o, n) -> testValidator.setInvalidMask(invalidMask.getText()));
        minInputLength.textProperty().addListener((p, o, n) -> {
            try {
                if (minInputLength.getText().length() == 0)
                    return;

                testValidator.setMinLength(NumberFormat.getInstance().parse(minInputLength.getText()).intValue());
                maxInputLength.setText(String.format("%d", testValidator.getMaxLength()));
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        });
        maxInputLength.textProperty().addListener((p, o, n) -> {
            try {
                if (maxInputLength.getText().length() == 0)
                    return;

                testValidator.setMaxLength(NumberFormat.getInstance().parse(maxInputLength.getText()).intValue());
                minInputLength.setText(String.format("%d", testValidator.getMinLength()));
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        });
    }
    //endregion
}
